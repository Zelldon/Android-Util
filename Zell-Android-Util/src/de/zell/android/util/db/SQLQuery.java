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

import android.content.ContentValues;
import java.io.Serializable;

/**
 * Represents the SQLQuery class which contains all necessary informations for a
 * SQL query.
 *
 * @author Christopher Zell <zelldon91@googlemail.com>
 */
public class SQLQuery implements Serializable {

  /**
   * The SQL LIKE expression which will be used to search a value which should
   * be like the given value.
   */
  public static final String SQL_SEARCH_LIKE = " LIKE ? ";
  /**
   * The SQL equal operator which will be used to search a value which should be
   * equal the given value
   */
  public static final String SQL_SEARCH_EQUAL = " = ?";
  /**
   * The SQL OR operator which will be used for the where clause.
   */
  public static final String SQL_OR = " OR ";
  /**
   * The ascending order for the order by clause.
   */
  public static final String SQL_ASC_ORDER = " ASC";
  /**
   * The descending order for the order by clause.
   */
  public static final String SQL_DESC_ORDER = " DESC";
  /**
   * The SQL percentage operator which will be used with the LIKE operator.
   */
  public static final String SQL_VARIABLE_EXP = "%";
  /**
   * The requested columns from the SQL query.
   */
  private String[] requestedColumns;
  /**
   * The selection of the SQL Query (where clause).
   */
  private String selection;
  /**
   * The arguments for the selection of the SQL Query.
   */
  private String[] selectionArgs;
  /**
   * The selected entity is equal with the from clause of the SQL query.
   */
  private Entity selectedEntity;
  /**
   * The order by clause of the SQL query.
   */
  private String orderBy;
  /**
   * The group by clause of the SQL query.
   */
  private String groupBy;
  /**
   * The having clause of the SQL query.
   */
  private String having;
  /**
   * The values which will be used for an update SQL query. Key-value pairs with
   * column as key and value as value.
   */
  private ContentValues values;

  /**
   * The ctor to construct a SQLQuery object.
   *
   * @param selection the selection of the SQLQuery
   * @param selectedEntity the selected entity/table of the query
   * @param requestedColumns the requested columns of the query
   */
  public SQLQuery(String selection, Entity selectedEntity, String[] requestedColumns) {
    this.selection = selection;
    this.selectedEntity = selectedEntity;
    this.requestedColumns = requestedColumns;
  }

  public String getSelection() {
    return selection;
  }

  public void setSelection(String selection) {
    this.selection = selection;
  }

  public String[] getSelectionArgs() {
    return selectionArgs;
  }

  public void setSelectionArgs(String... selectionArgs) {
    this.selectionArgs = selectionArgs;
  }

  public Entity getSelectedEntity() {
    return selectedEntity;
  }

  public void setSelectedEntity(Entity selectedEntity) {
    this.selectedEntity = selectedEntity;
  }

  public String getOrderBy() {
    return orderBy;
  }

  public void setOrderBy(String orderBy) {
    this.orderBy = orderBy;
  }

  public String getGroupBy() {
    return groupBy;
  }

  public void setGroupBy(String groupBy) {
    this.groupBy = groupBy;
  }

  public String getHaving() {
    return having;
  }

  public void setHaving(String having) {
    this.having = having;
  }

  public ContentValues getValues() {
    return values;
  }

  public void setValues(ContentValues values) {
    this.values = values;
  }

  public String[] getRequestedColumns() {
    return requestedColumns;
  }

  public void setRequestedColumns(String[] requestedColumns) {
    this.requestedColumns = requestedColumns;
  }

  /**
   * Added values for the update query to the SQLQuery.
   * 
   * @param columnName      the key - the column name
   * @param value           the value - the new value
   */
  public void addValues(String columnName, String value) {
    if (this.values == null) {
      values = new ContentValues();
    }

    values.put(columnName, value);
  }
}
