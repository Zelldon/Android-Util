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

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * Represents a broadcaster which broadcasts that a fragment is viewed or hidden.
 * 
 * @author Christopher Zell <zelldon91@googlemail.com>
 */
public class ViewFragmentBroadcaster {
  
  
  /**
   * Broadcasts that the fragment is viewed.
   * 
   * @param ctx the context which is used to send the broadcast
   * @param fragmentName the fragment name which is shown
   */
  public static void broadcstViewFragment(Context ctx, String fragmentName) {
    Intent showFrag = new Intent(IntentKeys.BROADCAST_VIEW_FRAGMENT);
    Bundle args = new Bundle();
    args.putString(IntentKeys.BROUDCAST_VIEW_FRAGMENT_ARGS_FRAG_NAME, fragmentName);
    showFrag.putExtra(IntentKeys.BROUDCAST_VIEW_FRAGMENT_ARGS, args);
    sendBroadcast(ctx, showFrag);
  }
  
  /**
   * Broadcasts that the fragment is hidden.
   * 
   * @param ctx the context which is used to send the broadcast
   */
  public static void boradcastHideFragment(Context ctx) {
    sendBroadcast(ctx, new Intent(IntentKeys.BROADCAST_HIDE_FRAGMENT));
  }
  
  /**
   * Sends the given intent as broadcast.
   * 
   * @param ctx the context which is used to send the broadcast
   * @param intent the intent which should be send
   */
  private static void sendBroadcast(Context ctx, Intent intent) {
    ctx.sendBroadcast(intent);
  }
}
