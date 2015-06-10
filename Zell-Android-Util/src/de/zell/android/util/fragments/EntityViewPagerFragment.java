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
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import de.zell.android.util.R;
import de.zell.android.util.db.Entity;

/**
 * Represents an entity view pager to show the entity informations
 * in a view pager fragment.
 * 
 * @author Christopher Zell <zelldon91@googlemail.com>
 */
public abstract class EntityViewPagerFragment extends EntityFragment {

  @Override
  protected void postRestore() {
    extractEntityInformation();
  }

  @Override
  protected void showEntity(Entity entity) {
    //do nothing
  }
  
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View root = inflater.inflate(R.layout.fragment_viewpager, container, false);
    ViewPager viewPager = (ViewPager) root.findViewById(R.id.tab_viewPager);
    /**
     * Important: Must use the child FragmentManager or you will see side
     * effects.
     */
    viewPager.setAdapter(getPageAdapter((getChildFragmentManager())));
    viewPager.setCurrentItem(getFirstPosition());
    return root;
  }
  
  /**
   * Returns the FragmentPagerAdapter implementation which should be used for the
   * view pager fragment.
   *
   * @param mgr the fragment mgr
   * @return the fragment pager adapter which should be used
   */
  protected abstract FragmentPagerAdapter getPageAdapter(FragmentManager mgr);
  
  /**
   * If the entity was loaded or already set, the extract method is called
   * to extract the information which should be shown by the view pager.
   */
  protected abstract void extractEntityInformation();
  
  /**
   * Returns the position which item should be showed at first by the pager.
   * It defines the default page which is shown after start.
   * 
   * <p>Per default it returns 0, so the first item is showed as first.</p>
   * 
   * @return the first position of the pager
   */
  protected int getFirstPosition() {
    return 0;
  }
    
}

