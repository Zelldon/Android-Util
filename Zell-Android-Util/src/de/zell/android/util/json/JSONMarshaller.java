/*
 * Copyright (C) 2015 Christopher Zell <zelldon91@googlemail.com>
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package de.zell.android.util.json;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * The JSONMarshaller marshalls the given object to a JSONObject object.
 * The given object should contain JSONElement annotations.
 * If the Object contains such an annotation the corresponding field
 * will be added to the JSON object.
 *
 * @see JSONElement
 * @see JSONUnmarshaller
 * @author Christopher Zell <zelldon91@googlemail.com>
 */
public class JSONMarshaller {

  /**
   * Marshalls the object and creates an JSON from the fields of the object which
   * are marked with the JSONElement annotation.
   *
   * @param o the object which will be parsed
   * @return the corresponding JSONObject
   */
  public static JSONObject parseObject(Object o) {
    if (o == null) {
      return null;
    }

    JSONObject json = new JSONObject();
    Field[] fields = o.getClass().getDeclaredFields();
    for (int i = 0; i < fields.length; i++) {
      Field f = fields[i];
      boolean accessible = f.isAccessible();
      if (!accessible) {
        f.setAccessible(true);
      }
      addFieldValueToJSONObject(json, f, o);
      if (!accessible) {
        f.setAccessible(false);
      }
    }
    return json;
  }

  /**
   * Adds the value of the corresponding field with the name value of the
   * JSONElement annotation to the JSONObject.
   *
   * @param json the JSONObject
   * @param f the field
   * @param o the instance of the class to which the field corresponds
   */
  private static void addFieldValueToJSONObject(JSONObject json, Field f, Object o) {
    JSONElement jsonAno = f.getAnnotation(JSONElement.class);
    if (jsonAno != null) {
      try {
        Class fieldClass = f.getType();
        Object fieldValue = f.get(o);
        if (fieldValue != null) {
          Object jsonValue;
          if (!fieldClass.isPrimitive() && !isPrimitveWrapper(fieldClass)
                  && fieldClass != String.class && !isCollection(fieldClass)
                  && !isMap(fieldClass)) {
            jsonValue = parseObject(fieldValue);
          } else {
            if (isCollection(fieldClass)) {
              jsonValue = parseCollection((Collection) fieldValue);
            } else if (isMap(fieldClass)) {
              jsonValue = new JSONObject((Map) fieldValue);
            } else {
              jsonValue = fieldValue;
            }
          }
          try {
            json.put(jsonAno.name(), jsonValue);
          } catch (JSONException ex) {
            Logger.getLogger(JSONMarshaller.class.getName()).log(Level.SEVERE, null, ex);
          }
        }
      } catch (IllegalAccessException ex) {
        Logger.getLogger(JSONMarshaller.class.getName()).log(Level.SEVERE, null, ex);
      } catch (IllegalArgumentException ex) {
        Logger.getLogger(JSONMarshaller.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
  }

  /**
   * Marshalls a collection object and returns a corresponding JSONArray Object.
   *
   * @param c the collection
   * @return the JSONArray
   */
  public static JSONArray parseCollection(Collection c) {
    JSONArray array = new JSONArray();
    Iterator i = c.iterator();
    while (i.hasNext()) {
      array.put(parseObject(i.next()));
    }
    return array;
  }
  
  /**
   * Checks whether the given class is a wrapper of a primitive type.
   *
   * @param c the class
   * @return true if is a wrapper, false otherwise
   */
  protected static boolean isPrimitveWrapper(Class c) {
    if (c == Byte.class || c == Short.class || c == Integer.class
            || c == Long.class || c == Float.class || c == Double.class
            || c == Boolean.class || c == Character.class) {
      return true;
    }
    return false;
  }

  /**
   * Checks whether the given class is a collection or not.
   *
   * @param c the class
   * @return true if is a collection, false otherwise
   */
  protected static boolean isCollection(Class c) {
    return Collection.class.isAssignableFrom(c);
  }

  /**
   * Checks whether the given class is a map or not.
   *
   * @param c the class
   * @return true if is a map, false otherwise
   */
  protected static boolean isMap(Class c) {
    return Map.class.isAssignableFrom(c);
  }
  
}
