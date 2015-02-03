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

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;
import de.zell.android.util.adapters.EntityListAdapter;
import de.zell.android.util.db.Entity;
import java.util.Arrays;
import java.util.List;

/**
 * Represents an abstract EntityListFragment which provides some list fragment
 * functionality and also defines some methods to create a good looking
 * sectional list fragment.
 *
 * @author Christopher Zell <zelldon91@googlemail.com>
 */
public abstract class EntityListFragment extends ListFragment {

  /**
   * The argument key for the entity url.
   */
  public static final String ARG_ENTITIES_URL = "entities.url";
  /**
   * The argument key for the entities.
   */
  public static final String ARG_ENTITIES = "entities";
  /**
   * The tag key for the entities content.
   */
  private static final String TAG_ENTITIES_CONTENT = "entities.content";
  /**
   * The index of the current showed entity.
   */
  private int index = -1;
  /**
   * The top of the list view.
   */
  private int top = 0;
  /**
   * The url which identifies the content of the entities.
   */
  private String url;
  /**
   * The list of entities which are showed in the list fragment.
   */
  protected List<Entity> entities = null;

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
  public void onResume() {
    super.onResume();
    if (index != -1) {
      this.getListView().setSelectionFromTop(index, top);
    }
  }

  @Override
  public void onPause() {
    super.onPause();
    index = this.getListView().getFirstVisiblePosition();
    View v = this.getListView().getChildAt(0);
    top = (v == null) ? 0 : v.getTop();
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    if (savedInstanceState == null) {
      savedInstanceState = getArguments();
    }

    if (savedInstanceState != null) {
      restoreEntities(savedInstanceState, TAG_ENTITIES_CONTENT);
      EntityListAdapter adapter = getEntityListAdapter(getActivity());
      if (entities == null) {
        restoreEntities(savedInstanceState, ARG_ENTITIES);
        if (entities == null) {
          url = (String) savedInstanceState.getSerializable(ARG_ENTITIES_URL);
          loadEntities();
        } 
      }
      adapter.setEntities(entities);
      setListAdapter(adapter);
    }
  }

  /**
   * Restores the entity list from the given bundle with the given key.
   * 
   * @param savedInstanceState the bundle which should contain the entity array
   * @param key the key which identifies the entity array in the bundle
   */
  private void restoreEntities(Bundle savedInstanceState, String key) {
    Entity[] e = (Entity[]) savedInstanceState.getSerializable(key);
    if (e != null) {
      entities = Arrays.asList(e);
    }
  }

  @Override
  public void onListItemClick(ListView l, View v, int position, long id) {
    super.onListItemClick(l, v, position, id);
    Object item = getListAdapter().getItem(position);

    if (item instanceof Entity) {
      onEntityClick(((Entity) item));
    } else {
      onSectionClick(item);
    }
  }

  @Override
  public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    if (entities != null)
      outState.putSerializable(TAG_ENTITIES_CONTENT,
                entities.toArray(new Entity[entities.size()]));
  }

  /**
   * The method is called if the element, which was clicked in the list
   * fragment, was an entity.
   *
   * @param e the entity which was clicked
   */
  protected abstract void onEntityClick(Entity e);

  /**
   * The method is called if the element, which was clicked in the list
   * fragment, was a section.
   *
   * @param o the section which was clicked
   */
  protected abstract void onSectionClick(Object o);

  /**
   * Contains the functionality to load the entities into the current list
   * adapter.
   *
   * Could contain an async task to load the content from a web resource or load
   * content from a database.
   */
  protected abstract void loadEntities();

  /**
   * Returns the EntityListAdapter implementation which should be used for the
   * list fragment.
   *
   * @param ctx the application context
   * @return the list adapter which should be used
   */
  protected abstract EntityListAdapter getEntityListAdapter(Context ctx);
}
