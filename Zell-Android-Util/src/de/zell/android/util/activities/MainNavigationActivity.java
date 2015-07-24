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


import android.app.ActionBar;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import de.zell.android.util.R;
import de.zell.android.util.fragments.ActionBarTitleManager;
import de.zell.android.util.fragments.FragmentReplacer;

/**
 * Represents the main navigation activity with a navigation drawer. On the left
 * side of the drawer displays the navigation to other existing views. If some
 * of them are selected the current fragment are replaced with the selected view
 * (respectively fragment). That means it exists only one main fragment in the
 * center which will be replaced every time.
 *
 * Influenced by the Android Navigation Drawer Tutorial:
 * https://developer.android.com/training/implementing-navigation/nav-drawer.html
 *
 * @author Christopher Zell <zelldon91@googlemail.com>
 */
public abstract class MainNavigationActivity extends FragmentActivity {

  /**
   * Drawer which will be used to draw the navigation on the left side.
   */
  private DrawerLayout mDrawerLayout;
  
  /**
   * The ListView which shows the fragments for the navigation.
   */
  private ListView mDrawerList;
  
  /**
   * The ActionBar toggle which enables to toggle the navigation bar.
   */
  private ActionBarDrawerToggle mDrawerToggle;
  
  /**
   * The title of the action bar.
   */
  private CharSequence mDrawerTitle;
  
  /**
   * The title of the current view.
   */
  private CharSequence mTitle;
  
  /**
   * The name of the applications.
   */
  private String[] applications;

  /**
   * Returns the navigation fragment array.
   * This array contains the fragments which correspond to the given fragment
   * names. If a specific name is selected in the navigation drawer the 
   * corresponding fragment is showed in the center of the application. 
   * 
   * @return the navigation fragments
   */
  protected abstract Fragment[] getNavigationFragments();
  
  /**
   * Returns the list adapter for the navigation list.
   * 
   * @return the list adapter
   */
  protected ListAdapter getNavigationListAdapter() {
    return new ArrayAdapter<String>(this, R.layout.drawer_list_item, applications);
  }
  /**
   * Returns the navigations fragment names which will be shown by
   * the navigation drawer.
   * 
   * @return the names of the fragments
   */
  protected abstract String[] getNavigationFragmentNames();
  
  private ViewFragmentBroadcastReciever reciever;
  
  protected ViewFragmentBroadcastReciever getBroadcastReciever() {
    return new ViewFragmentBroadcastReciever();
  }
  
  protected IntentFilter getIntentFilter() {
    IntentFilter filter = new IntentFilter();
    filter.addAction(IntentKeys.BROADCAST_VIEW_FRAGMENT);
    filter.addAction(IntentKeys.BROADCAST_HIDE_FRAGMENT);
    return filter;
  }
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main_nav_drawer);

    mTitle = mDrawerTitle = getTitle();
    applications = getNavigationFragmentNames();
    mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
    mDrawerList = (ListView) findViewById(R.id.left_drawer);

    // set a custom shadow that overlays the main content when the drawer opens
    mDrawerLayout.setDrawerShadow(R.drawable.ic_drawer_shadow, GravityCompat.START);
    // set up the drawer's list view with items and click listener
    mDrawerList.setAdapter(getNavigationListAdapter());
    
    mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
    initActionBar();

    // ActionBarDrawerToggle ties together the the proper interactions
    // between the sliding drawer and the action bar app icon
    mDrawerToggle = createActionBarDrawerToggle();
    mDrawerLayout.setDrawerListener(mDrawerToggle);
    ((DrawerItemClickListener) mDrawerList.getOnItemClickListener()).selectItem(0);
  }
  
  private void initActionBar() {
    ActionBar bar = getActionBar();
    // enable ActionBar app icon to behave as action to toggle nav drawer
    bar.setIcon(getActionBarIcon());
    bar.setDisplayHomeAsUpEnabled(true);
    bar.setHomeButtonEnabled(true);
  }
  
  protected int getActionBarIcon() {
    return R.drawable.ic_launcher;
  }
  
  
  /**
   * Returns the progress bar which could be used to show 
   * a progress of an async task or other task.
   * 
   * @return the main progress bar
   */
  public ProgressBar getProgressBar() {
    return (ProgressBar) this.findViewById(R.id.main_progress_bar);
  }
  
  /**
   * Creates a action bar drawer toggle object, which will be used
   * for the toggling of the drawer.
   * 
   * @return the drawer toggle object
   */
  private ActionBarDrawerToggle createActionBarDrawerToggle() {
    return new ActionBarDrawerToggle(this, /* host Activity */
                                     mDrawerLayout, /* DrawerLayout object */
                                     R.drawable.ic_drawer, /* nav drawer image to replace 'Up' caret */
                                     R.string.drawer_open, /* "open drawer" description for accessibility */
                                     R.string.drawer_close /* "close drawer" description for accessibility */) {
      @Override
      public void onDrawerClosed(View view) {
        onDrawerChanged(mTitle);
      }

      @Override
      public void onDrawerOpened(View drawerView) {
        onDrawerChanged(mDrawerTitle);
      }
      
      /**
       * Will be called if the drawer was changed.
       * Sets the given title as ActionBar title and calls
       * the invalidateOptionsMenu to prepare the menu.
       */
      private void onDrawerChanged(CharSequence title) {
        getActionBar().setTitle(title);
        invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
      }
    };
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // The action bar home/up action should open or close the drawer.
    // ActionBarDrawerToggle will take care of this.
    return mDrawerToggle.onOptionsItemSelected(item);
  }

  /**
   * Starts the searching with the tipped query. The query is tipped in on the
   * navigation bar (left).
   *
   * @param query the value which is searched for
   */
  protected abstract void startSearch(String query);

  /**
   * The click listener for the ListView in the navigation drawer.
   */
  private class DrawerItemClickListener implements ListView.OnItemClickListener {

    /**
     * Contains all existing main fragments.
     */
    private final Fragment fragments[];

    public DrawerItemClickListener() {
      fragments = getNavigationFragments();
      for(int i = 0; i < fragments.length; i++) {
        Fragment frg = fragments[i];
        Bundle title = frg.getArguments();
        if (title == null)
          title = new Bundle();
        title.putString(ActionBarTitleManager.ARG_ACTION_BAR_TITLE,
                       applications[i % applications.length]);
        frg.setArguments(title);
       }
    }
    
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
      selectItem(position);
    }

    /**
     * Selects the item which the user has clicked and starts the clicked
     * fragment (application).
     *
     * @param position the position in the list
     */
    public void selectItem(int position) {
      // update the main content by replacing fragments
      FragmentReplacer.replace(getSupportFragmentManager(),
                               getFragmentForChoice(position),
                               FragmentReplacer.MAIN_CONTENT);
      // update selected item and title, then close the drawer
      mDrawerList.setItemChecked(position, true);
      setTitle(applications[position % applications.length]);
      mDrawerLayout.closeDrawer(mDrawerList);
    }

    /**
     * Returns for the given choice the corresponding fragment.
     * 
     * @param choice        the choice
     * @return              the corresponding fragment
     */
    private Fragment getFragmentForChoice(int choice) {
      return fragments[choice % fragments.length];
    }
  }
  
  @Override
  public void setTitle(CharSequence title) {
    mTitle = title;
    getActionBar().setTitle(mTitle);
  }

  /*
   * When using the ActionBarDrawerToggle, you must call it during
   * onPostCreate() and onConfigurationChanged()...
   */
  @Override
  protected void onPostCreate(Bundle savedInstanceState) {
    super.onPostCreate(savedInstanceState);
    // Sync the toggle state after onRestoreInstanceState has occurred.
    mDrawerToggle.syncState();
  }

  @Override
  public void onConfigurationChanged(Configuration newConfig) {
    super.onConfigurationChanged(newConfig);
    // Pass any configuration change to the drawer toggls
    mDrawerToggle.onConfigurationChanged(newConfig);
  }

  @Override
  protected void onStart() {
    super.onStart(); 
    reciever = getBroadcastReciever();
    registerReceiver(reciever, getIntentFilter());
  }

  @Override
  protected void onStop() {
    super.onStop(); 
    unregisterReceiver(reciever);
  }
}