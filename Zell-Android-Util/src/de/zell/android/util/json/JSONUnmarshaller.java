/*
 * Copyright (C) 2015 Christopher Zell <zelldon91@googlemail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.zell.android.util.json;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * The JSONUnmarshaller unmarshalls the given JSONObject object to an object.
 * Which object corresponds to the JSONObject is identified via a class object.
 * The given class should contain JSONElement annotations. If the class contains
 * such an annotation the corresponding field will be filled with the
 * corresponding JSON value.
 *
 * @see JSONElement
 * @see JSONMarshaller
 * @author Christopher Zell <zelldon91@googlemail.com>
 */
public class JSONUnmarshaller {

  private static final String EXCEPTION_CAST_MESSAGE = "Value for field '%1$s' can not be cast from %2$s to %3$s";

  /**
   * Returns all fields of a given class, use recursion to get also
   * the inherited fields.
   * 
   * @param c the class which contains the fields
   * @return all declared fields of the given class
   */
  private static Field[] getDeclaredFields(Class c) {
    ArrayList<Field> fields = new ArrayList<Field>();
    fields.addAll(Arrays.asList(c.getDeclaredFields()));
    
    Class superClass = c.getSuperclass();
    if ( superClass != null && superClass != Object.class) {
      fields.addAll(Arrays.asList(getDeclaredFields(c.getSuperclass())));
    }
    
    return fields.toArray(new Field[fields.size()]);
  }
  
  /**
   * Unmarshalls the given JSON object and creates with the given values and
   * class the corresponding object instance which contains the JSON values. The
   * class fields must be annotated with the JSONElement annotation to get the
   * corresponding JSON object values.
   *
   * @see JSONElement
   * @param <O> the class type of the instance which will be returned
   * @param json the JSON object which contains the values
   * @param c the class of the object
   * @return the instance with the JSON values from type O
   */
  public static <O> O unmarshall(JSONObject json, Class<O> c) {
    O instance = null;
    try {
      instance = c.newInstance();
      Field fields[] = getDeclaredFields(c);
      for (Field field : fields) {
        field.setAccessible(true);
        JSONElement eleAnno = field.getAnnotation(JSONElement.class);
        if (eleAnno != null) {
          Object value = json.opt(eleAnno.name());
          if (value != null) {
            if (value instanceof JSONArray) {
              value = unmarshallJSONArray((JSONArray) value, field, instance);
            } else if (value instanceof JSONObject) {
              value = unmarshall((JSONObject) value, field.getType());
            }
            setValueToField(field, value, instance);
          }
        }
        field.setAccessible(false);
      }
    } catch (InstantiationException ex) {
      Logger.getLogger(JSONUnmarshaller.class.getName()).log(Level.SEVERE, null, ex);
    } catch (IllegalAccessException ex) {
      Logger.getLogger(JSONUnmarshaller.class.getName()).log(Level.SEVERE, null, ex);
    }
    return instance;
  }

  /**
   * Sets the given value to the instance field, the value
   * will be cast to the field type if possible. If a class cast exception
   * appears a detailed message will be added.
   * 
   * @param f the field which gets the new value
   * @param value the value of the field
   * @param instance the instance of the class on which the field belongs
   * @throws IllegalArgumentException 
   * @throws IllegalAccessException 
   */
  private static void setValueToField(Field f, Object value, Object instance)
          throws IllegalArgumentException, IllegalAccessException {
    try {
      f.set(instance, f.getType().cast(value));
    } catch (ClassCastException cce) {
      if (f == null) {
        throw cce;
      } else {
        throw new ClassCastException(String.format(EXCEPTION_CAST_MESSAGE,
                f.getName(),
                value.getClass().getName(),
                f.getType()));
      }
    }
  }

  /**
   * Unmarshalls a JSON array and returns a corresponding collection object. The
   * JSON objects in the array are parsed recursively by the public parseJSON
   * method.
   *
   * @param array the array which contains the values
   * @param f the field which should is the corresponding collection
   * @param instance the instance of the class which will be filled with the
   * values of the JSON objects
   * @return the created collection which contains the JSON array values
   */
  private static Object unmarshallJSONArray(JSONArray array, Field f, Object instance) {
    final int len = array.length();
    Class<?> type = f.getType();
    Type genType = f.getGenericType();
    Collection c = null;
    if (JSONMarshaller.isCollection(type) && genType instanceof ParameterizedType) {
      Class<?> listType = (Class<?>) ((ParameterizedType) genType).getActualTypeArguments()[0];
      c = new ArrayList();

      for (int i = 0; i < len; i++) {
        JSONObject obj = array.optJSONObject(i);
        if (obj != null) {
          c.add(unmarshall(obj, listType));
        }
      }
      try {
        f.set(instance, c);
      } catch (IllegalAccessException ex) {
        Logger.getLogger(JSONUnmarshaller.class.getName()).log(Level.SEVERE, null, ex);
      } catch (IllegalArgumentException ex) {
        Logger.getLogger(JSONUnmarshaller.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
    return c;
  }
}
