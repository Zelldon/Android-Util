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
package de.zell.android.util.activities;

/**
 * Represents the keys for the broadcast intents.
 * 
 * @author Christopher Zell <zelldon91@googlemail.com>
 */
public class IntentKeys {
  /**
   * The intent key which is used if the fragment is displayed.
   */
  public static final String BROADCAST_VIEW_FRAGMENT = "broadcast.view.fragment";
  
  /**
   * The intent key which is used if the fragment will be hidden.
   */
  public static final String BROADCAST_HIDE_FRAGMENT = "broadcast.hide.fragment";
  
  /**
   * The argument key for the view fragment intent.
   */
  public static final String BROUDCAST_VIEW_FRAGMENT_ARGS = "broadcast.view.fragment.args";
  
  /**
   * The argument key for the fragment name.
   */
  public static final String BROUDCAST_VIEW_FRAGMENT_ARGS_FRAG_NAME = "broadcast.view.fragment.args.frg.name";
}
