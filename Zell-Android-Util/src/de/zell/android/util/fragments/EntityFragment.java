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
import android.view.View;
import android.widget.TextView;
import de.zell.android.util.activities.ViewFragmentBroadcaster;
import de.zell.android.util.db.Entity;

/**
 *
 * @author Christopher Zell <zelldon91@googlemail.com>
 */
public abstract class EntityFragment extends ActionBarManagerFragment implements Nameable {

  /**
   * The argument key for the entity url.
   */
  public static final String ARG_ENTITY_URL = "entity.url";
  /**
   * The argument key for the entity.
   */
  public static final String ARG_ENTITY = "entity";

  /**
   * The tag key for the entities content.
   */
  private static final String TAG_ENTITY_FRAGMENT = "entity.content";

  /**
   * The url which identifies the content of the entity.
   */
  private String url;
  
  /**
   * The fragment title which can be set via arguments.
   */
  protected String title;
  
  /**
   * The entity which contains the content informations.
   */
  protected Entity entity;
  
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
      entity = (Entity) savedInstanceState.getSerializable(TAG_ENTITY_FRAGMENT);
      restoreInstance(savedInstanceState);
      if (entity == null) {
        entity = (Entity) savedInstanceState.getSerializable(ARG_ENTITY);
        if (entity == null) {
          url = savedInstanceState.getString(ARG_ENTITY_URL);
          loadEntity();
        }
      }
      postRestore();
    }

  }

  @Override
  public void onResume() {
    super.onResume(); 
    showEntity(entity);
    ViewFragmentBroadcaster.broadcstViewFragment(getActivity(), getName());
  }

  @Override
  public void onPause() {
    super.onPause(); 
    ViewFragmentBroadcaster.boradcastHideFragment(getActivity());
  }
  
  

  @Override
  public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putSerializable(TAG_ENTITY_FRAGMENT, entity);
  }

  /**
   * Shows the entity details and information in the corresponding
   * fragment views.
   * 
   * @param entity the entity which contains the information.
   */
  protected abstract void showEntity(Entity entity);
  
  /**
   * Restore the instance with the given bundle which contains either saved
   * instance or arguments to create the fragment.
   *
   * @param values the values for the instance as bundle
   */
  protected abstract void restoreInstance(Bundle values);

  /**
   * Contains the functionality to load the entities into the current fragment.
   *
   * Could contain an async task to load the content from a web resource or load
   * content from a database.
   */
  protected abstract void loadEntity();

  /**
   * Post restore is called after the entity or url is restored or read from the
   * arguments. The post restore can contain some functionality which are
   * executed after the values are set.
   */
  protected abstract void postRestore();
  
  /**
   * Returns the text view for the given id from the root view.
   * 
   * @param root the root view
   * @param id the id which identifies the view
   * @return the corresponding view
   */
  protected TextView getTextView(View root, int id) {
    return ((TextView) root.findViewById(id));
  } 
}
