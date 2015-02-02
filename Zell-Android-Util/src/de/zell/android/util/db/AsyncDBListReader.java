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

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the AsyncDBListReader which reads asynchronous from the database a
 * list as result of a SQL Query.
 *
 * @author Christopher Zell <zelldon91@googlemail.com>
 */
public class AsyncDBListReader extends AsyncTask<SQLiteOpenHelper, Void, List> {

  /**
   * The list which contains the elements after the SQL query was executed.
   */
  private List values;
  
  /**
   * The extractor which will be used to extract the values from the result set.
   */
  private final CursorExtracting extract;
  
  /**
   * The job which will be executed after the SQL query was executed.
   */
  private final PostExecuteJob postJob;

  /**
   * Contains all necessary informations for the SQL request.
   */
  private final SQLQuery query;
  
  /** 
   * The ctor to create the AsyncDBListReader object to read
   * asynchronous from the database a list as result of the given SQL Query. 
   * 
   * @param values              the list which will be used to save the result
   * @param query               the query which contains all necessary informations
   * @param extract             the extractor which will be used to extract the values from the result set
   * @param postJob             the job which will be executed after the SQL query was executed
   */
  public AsyncDBListReader(List values, SQLQuery query,
          CursorExtracting extract, PostExecuteJob postJob) {
    this.values = values;
    this.query = query;
    this.extract = extract;
    this.postJob = postJob;
  }

  @Override
  protected List doInBackground(SQLiteOpenHelper... helper) {
    if (extract == null || query == null
            || helper == null || helper.length == 0) {
      throw new IllegalStateException();
    }

    if (values == null) {
      values = new ArrayList();
    }

    SQLiteDatabase db = helper[0].getReadableDatabase();
    Cursor c = db.query(query.getSelectedEntity().getTableName(), 
                        query.getRequestedColumns(),
                        query.getSelection(),
                        query.getSelectionArgs(),
                        query.getGroupBy(), 
                        query.getHaving(), 
                        query.getOrderBy());
    c.moveToFirst();
    while (!c.isAfterLast()) {
      Object o = extract.extract(c);
      values.add(o);
      c.moveToNext();
    }
    c.close();
    helper[0].close();

    return values;
  }

  @Override
  protected void onPostExecute(List result) {
    super.onPostExecute(result);
    postJob.doJob(result);
  }

  /**
   * Represents the PostExecuteJob which will be used to execute a job/task after
   * the SQL query was executed.
   */
  public interface PostExecuteJob {

    /**
     * The job which will be executed.
     * 
     * @param result      the result from the executed SQL query
     */
    public void doJob(List result);
  }
}
