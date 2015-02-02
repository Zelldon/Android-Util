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

/**
 * Represents an CursorExtracting interface which will be used
 * for extracting a value or a class with his fields from a database cursor.
 *
 * @author Christopher Zell <zelldon91@googlemail.com>
 */
public interface CursorExtracting {
  
    /**
     * The extract method which will be used to extract the values.
     * 
     * @param c           the cursor which contains the values
     * @return            the extracted object
     */
    public Object extract(Cursor c);
}
