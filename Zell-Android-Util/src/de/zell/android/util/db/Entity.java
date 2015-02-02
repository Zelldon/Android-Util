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

import java.io.Serializable;

/**
 * Represents an entity interface which should be implemented from all entity
 * classes of a Database.
 * 
 * @author Christopher Zell <zelldon91@googlemail.com>
 * @param <ID> the identity type to identify an entity
 */
public interface Entity<ID> extends Serializable {
  
  /**
   * Returns the identifier of the entity with the given ID type.
   * 
   * @return the identifier of the entity
   */
  public ID getID();
  
  /**
   * Returns the table name of an entity in the corresponding database.
   * 
   * @return the table name
   */
  public String getTableName();
}
