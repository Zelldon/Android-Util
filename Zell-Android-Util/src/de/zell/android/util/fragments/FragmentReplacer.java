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
package de.zell.android.util.fragments;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import de.zell.android.util.R;

/**
 * Represents a fragment replacer which places a fragment 
 * on a view or layout which is identified via the given id.
 * 
 * @author Christopher Zell <zelldon91@googlemail.com>
 */
public class FragmentReplacer {

  /**
   * The id of the main content view.
   */
  public static final int MAIN_CONTENT = R.id.content_frame;
  
  /**
   * Replaces the existing fragment on the given view with the new one.
   * If no fragment exists on the view the fragment is only placed.
   * The view is identified via the layout id.
   * 
   * @param fgrMgr the fragment manager which is used to replace the frag
   * @param frg the new fragment which should be placed
   * @param id the view which gets the new fragment
   */
  public static void replace(FragmentManager fgrMgr, Fragment frg, int id) {
    if (fgrMgr == null || frg == null || id <= 0)
      throw new IllegalArgumentException();
    
    Fragment old = fgrMgr.findFragmentById(id);
    FragmentTransaction trx = fgrMgr.beginTransaction();
    if (old != null) {
      trx.remove(old);
    }
    trx.replace(id, frg).addToBackStack(null).commit();
  }
}
