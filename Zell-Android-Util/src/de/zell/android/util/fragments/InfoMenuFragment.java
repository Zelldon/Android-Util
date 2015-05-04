/*
 * Copyright 2015 Quality and Usability Lab, Telekom Innvation Laboratories, TU Berlin..
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.zell.android.util.fragments;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
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
public abstract class InfoMenuFragment extends Fragment {

  
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
   * 
   * @return 
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
