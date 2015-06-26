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

import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Represents a action bar manager fragment, which uses the 
 * ActionBarTitleManager class to manage the action bar title.
 * 
 * @see ActionBarTitleManager
 * @author Christopher Zell <zelldon91@googlemail.com>
 */
public class ActionBarManagerFragment extends Fragment {

  /**
   * The manager manages the action bar title.
   */
  private ActionBarTitleManager barManager;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (savedInstanceState == null) {
      savedInstanceState = getArguments();
    }
    barManager = new ActionBarTitleManager(savedInstanceState);
  }

  @Override
  public void onResume() {
    super.onResume();
    barManager.setActionBarTitle(getActivity().getActionBar());
  }

  @Override
  public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    barManager.storeTitle(outState);
  }
}
