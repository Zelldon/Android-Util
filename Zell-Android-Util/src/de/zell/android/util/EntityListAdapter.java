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
package de.zell.android.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;
import de.zell.android.util.db.Entity;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * The EntityListAdapter to display each single entity in the ListView.
 *
 * @author Christopher Zell <zelldon91@googlemail.com>
 */
public class EntityListAdapter extends BaseAdapter implements SectionIndexer {

  /**
   * The entities.
   */
  private List<Entity> entities;
  /**
   * Contains the sections with the corresponding index of the first element in
   * the section in the entities list.
   */
  private HashMap<String, Integer> sectionIndexes;
  /**
   * Contains the sections.
   */
  private String[] sections;
  
  /**
   * The application context.
   */
  private Context context;

  /**
   * The ctor of the EntityListAdapter.
   *
   * @param r the entities
   * @param c the application context
   */
  public EntityListAdapter(List<Entity> entities, Context c) {
    this.entities = entities;
    this.context = c;
    createSections();
  }

  /**
   * Creates the sections for the given entity list.
   */
  private void createSections() {
    sectionIndexes = new HashMap<String, Integer>();
    int size = entities.size();
    for (int i = 0; i < size; i++) {
      String str = getSectionForEntity(i);
      if (str != null) {
        if (!sectionIndexes.containsKey(str)) {
          sectionIndexes.put(str, i);
        }
      }
      ArrayList<String> sectionList = new ArrayList<String>(sectionIndexes.keySet());
      Collections.sort(sectionList);
      sections = new String[sectionList.size()];
      sectionList.toArray(sections);
    }
  }

  /**
   * Returns for the given i (position in the entity list) the corresponding
   * section.
   *
   * @param i the position in the entity list
   * @return the corresponding section of the entity
   */
  protected String getSectionForEntity(int i) {
    String str = null;
    str = entities.get(i).getID().toString();
    return str != null ? str.substring(0, 1).toUpperCase() : null;
  }

  @Override
  public int getCount() {
    return entities.size();
  }

  @Override
  public Object getItem(int arg0) {
    return entities.get(arg0);
  }

  @Override
  public long getItemId(int arg0) {
    return arg0;
  }

  /**
   * Returns the entities of the adapter.
   *
   * @return the entities
   */
  public List getEntities() {
    return entities;
  }

  /**
   * Replaces the results of the search.
   *
   * @param results the results
   */
  public void setResults(List results) {
    this.entities = results;
    createSections();
  }

  @Override
  public View getView(int position, View convertview, ViewGroup parent) {
    LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    View row = inflater.inflate(R.layout.fragment_entity_row, parent, false);
    TextView title = (TextView) row.findViewById(R.id.entity_row_title);
    TextView description = (TextView) row.findViewById(R.id.entity_row_description);
    setTextViewContent(title, description, entities.get(position));
    return row;
  }
  
  /**
   * Sets the text of the given title and description text views.
   * 
   * @param title the title text view
   * @param description the description view
   * @param e the entity which contains the content
   */
  protected void setTextViewContent(TextView title, TextView description, Entity e) {
    title.setText(e.getTableName());
    description.setText(e.getID().toString());
  }
  
  /**
   * Returns the exiting sections.
   *
   * @return the sections
   */
  public Object[] getSections() {
    return sections;
  }

  /**
   * Returns for the given section id the corresponding position of the first
   * element for that section.
   *
   * @param arg0 the section id
   * @return the position
   */
  public int getPositionForSection(int arg0) {
    return sectionIndexes.get(sections[arg0]);
  }

  /**
   * Returns the section for the given position in the entities list.
   *
   * @param arg0 the position
   * @return the section
   */
  public int getSectionForPosition(int arg0) {
    String s = getSectionForEntity(arg0);
    boolean found = false;
    int pos = 0;
    for (int i = 0; i < sections.length && !found; i++) {
      if (sections[i].equalsIgnoreCase(s)) {
        found = true;
        pos = i;
      }
    }
    return pos;
  }
}
