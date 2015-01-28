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

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;
import de.zell.android.util.adapters.EntitySectionListAdapter;
import de.zell.android.util.db.Entity;
import java.util.Arrays;
import java.util.List;

/**
 * Represents 
 * @author Christopher Zell <zelldon91@googlemail.com>
 */
public abstract class EntityListFragment extends ListFragment {
  
  
  public static final String ARG_ENTITIES_URL = "entities.url";
  private static final String TAG_ENTITIES_CONTENT = "entities.content";
  
  private int index = -1;
  private int top = 0;
  private String url;
  protected List<Entity> entities = null;
  
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

    if (null == savedInstanceState) {
      savedInstanceState = getArguments();
    }

    if (null != savedInstanceState) {
      Entity[] e = (Entity[]) savedInstanceState.getSerializable(TAG_ENTITIES_CONTENT);
      if (e != null)
        entities = Arrays.asList(e);
    }

    EntitySectionListAdapter adapter = getEntityListAdapter(getActivity());
    
    if (entities == null) {
      url = (String) savedInstanceState.getSerializable(ARG_ENTITIES_URL);
      loadEntities();
    } else
      adapter.setEntities(entities);
    
    
    setListAdapter(adapter);
  }

  
  @Override
  public void onListItemClick(ListView l, View v, int position, long id) {
    super.onListItemClick(l, v, position, id);
    Object item = getListAdapter().getItem(position);
    
    if (item instanceof Entity) 
      onEntityClick(((Entity)item));
    else
      onSectionClick(item);
  }
  
  
  @Override
  public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putSerializable(TAG_ENTITIES_CONTENT,
                              entities.toArray(new Entity[entities.size()]));
  }
  
  
  protected abstract void onEntityClick(Entity e);
  protected abstract void onSectionClick(Object o);
  /**
   * Database or Downloading stuff.
   */
  protected abstract void loadEntities();
  protected abstract EntitySectionListAdapter getEntityListAdapter(Context ctx);
  
  
  
}
