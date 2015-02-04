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
public abstract class EntityViewPagerFragment extends Fragment {

  /**
   * The tag for the for the entity value.
   */
  private static final String TAG_ENTITY_PAGER_FRAGMENT = "vierpager";
  /**
   * The argument key for the entity value.
   */
  public static final String ARG_ENITY = "entity";
  
  /**
   * The argument key for the entity url, to load the entity.
   */
  public static final String ARG_ENTITY_URL = "entity.url";
  
  /**
   * The entity which contains the content informations.
   */
  protected Entity entity;
  
  /**
   * The url to load the entity.
   */
  private String url;
  
  /**
   * Returns the url which identifies the content of the entities. The url is
   * used to download the content.
   *
   * @return the url
   */
  public String getURL() {
    return url;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    if (savedInstanceState == null) {
      savedInstanceState = getArguments();
    }

    if (savedInstanceState != null) {
      entity = (Entity) savedInstanceState.getSerializable(TAG_ENTITY_PAGER_FRAGMENT);
      if (entity == null) {
        entity = (Entity) savedInstanceState.getSerializable(ARG_ENITY);
        if (entity == null)
          loadEntity();
      }
      extractEntityInformation();
    }
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
    return root;
  }
  
  
  @Override
  public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putSerializable(TAG_ENTITY_PAGER_FRAGMENT, entity);
  }
  
  protected abstract void loadEntity();
  protected abstract FragmentPagerAdapter getPageAdapter(FragmentManager mgr);
  protected abstract void extractEntityInformation();
}

