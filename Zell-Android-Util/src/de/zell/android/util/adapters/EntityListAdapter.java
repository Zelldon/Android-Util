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
package de.zell.android.util.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import de.zell.android.util.EntityComparator;
import de.zell.android.util.R;
import de.zell.android.util.SparseArray;
import de.zell.android.util.db.Entity;
import java.util.Collections;
import java.util.List;

/**
 * The EntitySectionListAdapter to display each single entity inside of a
 * sectionalised ListView.
 *
 * @author Christopher Zell <zelldon91@googlemail.com>
 */
public class EntityListAdapter extends BaseAdapter {

  /**
   * The entities.
   */
  protected SparseArray<Entity> entities;
  /**
   * Contains the sections.
   */
  protected SparseArray<String> sections;
  /**
   * The application context.
   */
  private Context context;

  /**
   * The ctor of the EntitySectionListAdapter.
   *
   * @param c the application context
   */
  public EntityListAdapter(Context c) {
    this.entities = new SparseArray<Entity>();
    this.sections = new SparseArray<String>();
    this.context = c;
  }

  @Override
  public int getCount() {
    return entities.size() + sections.size();
  }

  @Override
  public Object getItem(int arg0) {
    String sec = sections.get(arg0);
    if (sec == null) {
      return entities.get(arg0);
    } else {
      return sec;
    }
  }

  @Override
  public long getItemId(int arg0) {
    return arg0;
  }

  /**
   * The given entity list will be used to create a section sparse array and an
   * array for the entities.
   *
   * @param entities the entities
   */
  public void setEntities(List<Entity> entities) {
    if (entities == null) {
      return;
    }

    Collections.sort(entities, getComparator());

    int count = 0;
    for (int i = 0; i < entities.size(); i++) {
      Entity e = entities.get(i);
      String section = getSection(e);
      if (sections.indexOfValue(section) < 0) {
        sections.put(count, section);
        this.entities.put(++count, entities.get(i));
      } else {
        this.entities.put(count, entities.get(i));
      }
      count++;
    }
  }

  /**
   * Returns the section for an entity, used in the section creation process.
   *
   * @param e the entity
   * @return the corresponding section
   */
  protected String getSection(Entity e) {
    String str = e.getTableName();
    if (str != null) {
      str = str.substring(0, 1);
    } else {
      str = "";
    }
    return str;
  }

  @Override
  public View getView(int position, View convertview, ViewGroup parent) {
    LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    View row = inflater.inflate(R.layout.fragment_section_row, parent, false);

    if (isSection(position)) {
      setSectionView(row, position);
    } else {
      setEntityView(row, position);
    }

    return row;
  }

  /**
   * Sets the view for the section.
   *
   * @param row the view which contains the section view
   * @param pos the position of the section
   */
  protected void setSectionView(View row, int pos) {
    String sec = sections.get(pos);
    row.setBackgroundColor(context.getResources().getColor(R.color.light_gray));
    if (sec != null) {
      TextView header = (TextView) row.findViewById(R.id.section_header);
      header.setText(sec);
      header.setVisibility(View.VISIBLE);
    }
  }

  /**
   * Sets the view for the entity.
   *
   * @param row the view which contains the entity views
   * @param pos the position of the entity
   */
  protected void setEntityView(View row, int pos) {
    Entity e = entities.get(pos);
    if (e != null) {
      TextView title = (TextView) row.findViewById(R.id.entity_title);
      title.setText(e.getTableName());
      title.setVisibility(View.VISIBLE);
      TextView desc = (TextView) row.findViewById(R.id.entity_description);
      desc.setText(e.getID().toString());
      desc.setVisibility(View.VISIBLE);
    }
  }

  /**
   * Checks if the given position is a section.
   *
   * @param pos the position
   * @return true if it is a section, false otherwise
   */
  private boolean isSection(int pos) {
    return sections.get(pos) != null;
  }

  /**
   * Returns the comparator which will be used to sort the entity list.
   *
   * @return the entity comparator
   */
  protected EntityComparator getComparator() {
    return new EntityComparator();
  }
}
