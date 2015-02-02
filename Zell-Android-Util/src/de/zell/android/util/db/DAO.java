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
package de.zell.android.util.db;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

/**
 * The abstract DAO class which defines the data access object methods
 * to communicate with the SQLite database.
 * 
 * @author Christopher Zell <zelldon91@googlemail.com>
 */
public abstract class DAO {
  
  /**
   * The application context which is used to communicate with the database.
   */
  private final Context context;
  
  /**
   * The ctor to create a DAO object.
   * 
   * @param context the application context
   */
  public DAO(Context context) {
    this.context = context;
  }

  /**
   * Returns the used context of the application.
   * 
   * @return the context
   */
  protected Context getContext() {
    return context;
  }
  
  /**
   * The method updates the hole database with new content.
   */
  public abstract void updateDB();
  
  /**
   * Returns for the given SQL-Query a selection from the database,
   * the result can be used in the given postJob object.
   * 
   * @param postJob the post job which defines the examination of the result
   * @param query 
   */
  public abstract void getSelection(AsyncDBListReader.PostExecuteJob postJob, SQLQuery query);
  
  /**
   * Updates with the given SQL-Query the entity on the SQLite database.
   * 
   * @param query the update SQL-Query
   */
  public void updateEntity(SQLQuery query) {
    new AsyncDBEntityUpdater(query).execute(getSQLiteOpenHelper());
  }
  
  /**
   * The cursor extractor which should be used for the given class
   * to extract the values for a given cursor from the database.
   * 
   * @param c the class for which the extractor is needed
   * @return the corresponding cursor extractor
   */
  protected abstract CursorExtracting getCursorExtractorForClass(Class c);
  
  /**
   * Returns the SQLiteOpenHelper object which is used to communicate with
   * the SQLite database.
   * 
   * @return the SQLiteOpenHelper
   */
  protected abstract SQLiteOpenHelper getSQLiteOpenHelper();
  
  
  
  /**
   * Returns for the given class the declared fields. The method is used to get
   * the columns of the tables from the DB contract.
   * DB contract class should contain abstract classes which contains static string
   * fields, which represents the columns of the corresponding database table.
   *
   *
   * @param c the class which represents the database table
   * @param ignore the field names which should be ignored
   * @return the corresponding columns
   */
  protected static String[] getColumns(Class c, String ... ignore) {
    Field fields[] = c.getDeclaredFields();
    String values[] = new String[fields.length];
    List ignoreValues = Arrays.asList(values);
    for (int i = 0; i < fields.length; i++) {
      try {
        
        if (!ignoreValues.contains(fields[i])) {
          values[i] = fields[i].get(null).toString();
        }
      } catch (IllegalAccessException ex) {
        Log.e(DAO.class.getName(), IllegalAccessException.class.getName(), ex);
      } catch (IllegalArgumentException ex) {
        Log.e(DAO.class.getName(), IllegalArgumentException.class.getName(), ex);
      }
    }
    return values;
  }
  
}
