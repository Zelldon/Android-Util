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

/**
 *
 * @author Christopher Zell <zelldon91@googlemail.com>
 */
public abstract class DBContract {
  
  
  /**
   * The SQLiteDatabase text datatype.
   */
  protected static final String TEXT_TYPE = " TEXT";
  
  /**
   * The SQLiteDatabase integer datatype.
   */
  protected static final String INT_TYPE = " INTEGER";
  
  /**
   * The comma separator which will be used in the SQL statements.
   */
  protected static final String COMMA_SEP = ",";
  
  /**
   * The SQLiteDatabase create table formula.
   */
  protected static final String CREATE_STATEMENT = "CREATE TABLE %s ( %s %s PRIMARY KEY, %s ); ";
  
  /**
   * The SQLiteDatabase create table formula, for composite key.
   */
  protected static final String CREATE_STATEMENT_COMPOSITE_PK = "CREATE TABLE %s ( %s, PRIMARY KEY ( %s, %s ));";
  
  /**
   * The SQLiteDatabase drop table formula.
   */
  protected static final String DROP_STATEMENT = "DROP TABLE IF EXISTS %s;";
  
}
