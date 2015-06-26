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
import android.support.v4.app.DialogFragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import de.zell.android.util.R;

/**
 * Represents the info options menu which will be shown in the action bar
 * of the navigation drawer.
 * 
 * @author Christopher Zell <zelldon91@googlemail.com>
 */
public abstract class InfoMenuFragment extends ActionBarManagerFragment {

  
  private static final String TAG_INFO = "info";
  
  /**
   * The info menu item.
   */
  protected MenuItem item;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setHasOptionsMenu(true);
  }
  
  /**
   * Returns the used info dialog fragment.
   * 
   * @return the dialog fragment
   */
  protected abstract DialogFragment getInfoDialog();


  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);
    inflater.inflate(R.menu.info_menu, menu);
    item = menu.findItem(R.id.action_info);
  }

  @Override
  public void onResume() {
    super.onResume();
    getActivity().invalidateOptionsMenu();
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem favItem) {
    if (favItem.getItemId() == R.id.action_info) {
      DialogFragment dialog = getInfoDialog();
      dialog.show(getChildFragmentManager(), TAG_INFO);
      return true;
    }
    return super.onOptionsItemSelected(favItem);
  }
}
