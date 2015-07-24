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
import de.zell.android.util.activities.ViewFragmentBroadcaster;


/**
 * Represents an auto closable dialog fragment which closes himself if the onPause
 * method is called.
 * 
 * @author Christopher Zell <zelldon91@googlemail.com>
 */
public abstract class AutoCloseDialogFragment extends DialogFragment implements Nameable {

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState); 
  }

  @Override
  public void onResume() {
    super.onResume(); 
    ViewFragmentBroadcaster.broadcstViewFragment(getActivity(), getName());
  }
  
  @Override
  public void onPause() {
    super.onPause();
    ViewFragmentBroadcaster.boradcastHideFragment(getActivity());
    dismiss();
  }
}
