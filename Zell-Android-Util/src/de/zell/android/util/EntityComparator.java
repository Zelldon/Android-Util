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
package de.zell.android.util;

import de.zell.android.util.db.Entity;
import java.util.Comparator;

/**
 * EntityComparator will be used to compare two entities
 * for example to sort a collection of entities.
 * 
 * @author Christopher Zell <zelldon91@googlemail.com>
 */
public class EntityComparator implements Comparator<Entity> {

  /**
   * The compare method which will be used to compare the entities
   * and sort the entity list. 
   * 
   * @param arg0 the first entity which will be compared with the second one
   * @param arg1 the second entity which will be compared with the first one
   * @return a negative integer, zero, or a positive integer as the first
   *          entity is less than, equal to, or greater than the second.
   */
  public int compare(Entity arg0, Entity arg1) {
    return arg0.getID().toString().compareTo(arg1.getID().toString());
  }
  
}
