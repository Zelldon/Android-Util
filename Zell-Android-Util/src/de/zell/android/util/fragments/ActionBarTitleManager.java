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
package de.zell.android.util.fragments;

import android.app.ActionBar;
import android.os.Bundle;

/**
 * Manages the action bar title, initialize with a bundle
 * which contains the action bar title or the direct string as title.
 * 
 * @author Christopher Zell <zelldon91@googlemail.com>
 */
public class ActionBarTitleManager {
  
  /**
   * The argument key for the action bar title.
   */
  public static final String ARG_ACTION_BAR_TITLE = "arg.action.bar.title";
  
  /**
   * The tag key for the fragment title.
   */
  public static final String TAG_ACTION_BAR_TITLE = "tag.action.bar.title";
  
  /**
   * Contains the action bar title;
   */
  private String title;

  /**
   * The ctor to create the ActionBarTitleManager instance.
   * The string represents the title which should be shown as action bar title.
   * 
   * @param title the new action bar title
   */
  public ActionBarTitleManager(String title) {
    this.title = title;
  }
  
  /**
   * The ctor to create the ActionBarTitleManager instance.
   * The given bundle contains the new action bar title.
   * The title are saved with the tag or argument key.
   * 
   * @param arg the bundle which contains the action bar title
   */
  public ActionBarTitleManager(Bundle arg) {
    if (arg != null) {
      title = arg.getString(TAG_ACTION_BAR_TITLE);
      if (title == null)
        title = arg.getString(ARG_ACTION_BAR_TITLE);
    }
  }
  
  /**
   * Sets the title of the given action bar.
   * @param bar 
   */
  public void setActionBarTitle(ActionBar bar) {
    if (title != null && bar != null)
      bar.setTitle(title);
  }
  
  /**
   * Stores the action bar title into the given bundle.
   * 
   * @param outState the out state which should contain the title
   * @return the bundle with the action bar title
   */
  public Bundle storeTitle(Bundle outState) {
    if (outState != null)
      outState.putString(TAG_ACTION_BAR_TITLE, title);
    return outState;
  }
  
}
