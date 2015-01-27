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
package de.zell.android.util;

/**
 * Improved version of the Android SparseArray.
 * The indexOfValue Method now uses equals instead of the '==' Operator!
 * 
 * influenced by: 
 * https://code.google.com/p/android/issues/detail?id=53297
 * https://gist.github.com/caseycrites/3453143
 * 
 * @author Christopher Zell <zelldon91@googlemail.com>
 */
public class SparseArray<E> extends android.util.SparseArray<E> {

  @Override
  public int indexOfValue(E value) {
    final int count = size();
    if (value != null) {
      for (int i = 0; i < count; i++) {
        if (value.equals(valueAt(i))) {
          return i;
        }
      }
    }
    return -1;

  }
}
