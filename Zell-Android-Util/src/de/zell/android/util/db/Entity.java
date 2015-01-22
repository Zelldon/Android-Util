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
 * Represents an entity interface which should be implemented from all entity
 * classes of a Database.
 * 
 * @author Christopher Zell <zelldon91@googlemail.com>
 */
public interface Entity<ID> {
  
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
  public abstract String getTableName();
}
