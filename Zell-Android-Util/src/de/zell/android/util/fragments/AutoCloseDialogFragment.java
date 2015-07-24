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

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import de.zell.android.util.activities.IntentKeys;
import java.util.Date;


/**
 *
 * @author Christopher Zell <zelldon91@googlemail.com>
 */
public abstract class AutoCloseDialogFragment extends DialogFragment implements Nameable {

  protected abstract String getDialogName();
  

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState); 
  }

  @Override
  public void onResume() {
    super.onResume(); 
    Intent showFrag = new Intent(IntentKeys.BROADCAST_VIEW_FRAGMENT);
    Bundle args = new Bundle();
    args.putString(IntentKeys.BROUDCAST_VIEW_FRAGMENT_ARGS_FRAG_NAME, getName());
    showFrag.putExtra(IntentKeys.BROUDCAST_VIEW_FRAGMENT_ARGS, args);
    getActivity().sendBroadcast(showFrag);
  }
  
  @Override
  public void onPause() {
    super.onPause();
    Intent hideFrag = new Intent(IntentKeys.BROADCAST_HIDE_FRAGMENT);
    getActivity().sendBroadcast(hideFrag);
    dismiss();
  }
}
