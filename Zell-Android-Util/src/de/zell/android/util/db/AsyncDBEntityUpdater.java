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
package de.zell.android.util.db;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;

/**
 * Represents the asynchronous entity updater which updates the given entity
 * on the database.
 * 
 * @author Christopher Zell <zelldon91@googlemail.com>
 */
public class AsyncDBEntityUpdater extends AsyncTask<SQLiteOpenHelper, Void, Integer> {
  
  /**
   * Contains the information for the SQL update statement.
   */
  private SQLQuery query;
  

  /**
   * The ctor which creates the asynchronous entity updater.
   * 
   * @param query   the SQL query which contains the update statement.
   */
  public AsyncDBEntityUpdater(SQLQuery query) {
    this.query = query;
  }
  
  @Override
  protected Integer doInBackground(SQLiteOpenHelper... helper) {
    if (helper == null || helper.length == 0 || helper[0] == null)
      throw new IllegalArgumentException();
    
    SQLiteDatabase db = helper[0].getWritableDatabase();
    return db.update(query.getSelectedEntity().getTableName(), query.getValues(),
                    query.getSelection(), query.getSelectionArgs());
  }
}
