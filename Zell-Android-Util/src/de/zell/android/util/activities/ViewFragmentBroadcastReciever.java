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

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

/**
 * Represents the view fragment broadcast receiver.
 * Receives intents for the view and hide fragment.
 * 
 * @author Christopher Zell <zelldon91@googlemail.com>
 */
public class ViewFragmentBroadcastReciever extends BroadcastReceiver {

  /**
   * The information which will be logged if the view intent was received.
   */
  private static final String VIEW_FRAGMENT = "View Fragment %1$s";
  
  /**
   * The information which will be logged if the hide intent was received.
   */
  private static final String HIDE_FRAGMENT = "Hide Fragment";
  
  @Override
  public void onReceive(Context arg0, Intent arg1) {
    String action = arg1.getAction();
    if (action.equals(IntentKeys.BROADCAST_VIEW_FRAGMENT)) {
      onViewFragment(arg1.getBundleExtra(IntentKeys.BROUDCAST_VIEW_FRAGMENT_ARGS));
    }
    else {
      onHideFragment();
    }
  }
  
  /**
   * Will be called if the view fragment intent was received.
   * 
   * @param args the arguments from the intent
   */
  protected void onViewFragment(Bundle args) {
    if (args != null) {
      String name = args.getString(IntentKeys.BROUDCAST_VIEW_FRAGMENT_ARGS_FRAG_NAME);
      Log.d(ViewFragmentBroadcastReciever.class.getName(),
              String.format(VIEW_FRAGMENT, name));
    }
  }
  
  /**
   * Will be called if the hide fragment intent was received.
   */
  protected void onHideFragment() {
    Log.d(ViewFragmentBroadcastReciever.class.getName(), HIDE_FRAGMENT);
  }
}
