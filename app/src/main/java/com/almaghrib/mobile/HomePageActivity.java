package com.almaghrib.mobile;

import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;

import com.almaghrib.mobile.instructors.OurInstructorsFragment;
import com.almaghrib.mobile.navigationDrawer.CategoryHeaderDrawerItem;
import com.almaghrib.mobile.navigationDrawer.DrawerItem;
import com.almaghrib.mobile.navigationDrawer.NavigationDrawerAdapter;
import com.almaghrib.mobile.navigationDrawer.NormalDrawerItem;

import java.util.ArrayList;

public class HomePageActivity extends FragmentActivity {

    private static final String CURRENT_SELECTED_ITEM = "current_selected_item";
    private static final String CURRENT_TITLE = "current_title";

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private int mCurrentlySelectedItem = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_home_page);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // setting the nav drawer list adapter
        final ArrayList<DrawerItem> navDrawerItems = getNavDrawerItems();
        final NavigationDrawerAdapter adapter =
                new NavigationDrawerAdapter(getApplicationContext(), navDrawerItems);
        mDrawerList.setAdapter(adapter);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        if (savedInstanceState != null) {
            mCurrentlySelectedItem = savedInstanceState.getInt(CURRENT_SELECTED_ITEM, 0);
            final String title = savedInstanceState.getString(
                    CURRENT_TITLE, getString(R.string.app_name));
            getActionBar().setTitle(title);
        }
        // set and open first item on launch
        if (savedInstanceState == null && mCurrentlySelectedItem == 0) {
            mDrawerList.setItemChecked(0, true);
            startFragment(this, new HomeFragment());
            getActionBar().setTitle(R.string.app_name);
        }

        mDrawerToggle = getActionBarDrawerToggle();
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(CURRENT_SELECTED_ITEM, mCurrentlySelectedItem);
        outState.putString(CURRENT_TITLE, getActionBar().getTitle().toString());
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
          return true;
        }
        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }
    
	/**
	 * Called when invalidateOptionsMenu() is triggered
	 */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// if nav drawer is opened, hide the action items
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
		menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_page, menu);
        return true;
    }

    private ArrayList<DrawerItem> getNavDrawerItems() {
        // load slide menu items
        String[] navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);
        TypedArray navMenuIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons);

        final ArrayList<DrawerItem> navDrawerItems = new ArrayList<DrawerItem>();
        // adding nav drawer items to array
        for (int i = 0; i < navMenuTitles.length; i++) {
            navDrawerItems.add(new NormalDrawerItem(navMenuTitles[i], navMenuIcons
                    .getResourceId(i, -1)));
        }
        navDrawerItems.add(new CategoryHeaderDrawerItem(getString(R.string.upcoming_events)));
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_upcoming_category_items);
        navMenuIcons = getResources().obtainTypedArray(R.array.nav_drawer_upcoming_category_icons);
        for (int i = 0; i < navMenuTitles.length; i++) {
            navDrawerItems.add(new NormalDrawerItem(navMenuTitles[i], navMenuIcons
                    .getResourceId(i, -1)));
        }
        // Recycle the typed array
        navMenuIcons.recycle();
        return navDrawerItems;
    }

    private ActionBarDrawerToggle getActionBarDrawerToggle() {
        return new ActionBarDrawerToggle(
                this, mDrawerLayout,
                R.drawable.ic_drawer,  /* nav drawer icon to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description */
                R.string.drawer_close  /* "close drawer" description */
        ) {
            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                // calling onPrepareOptionsMenu() to show action bar icons
                invalidateOptionsMenu();
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                // calling onPrepareOptionsMenu() to hide action bar icons
                invalidateOptionsMenu();
            }
        };
    }

    /**
	 * Slide menu item click listener
	 */
	private class DrawerItemClickListener implements ListView.OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view,
				int position, long id) {
			// display view for selected nav drawer item
            if (mCurrentlySelectedItem != position) {
                mCurrentlySelectedItem = position;
                handleDrawerItemClick(position);
                mDrawerLayout.closeDrawers();
            }
		}
	}
    
	private void handleDrawerItemClick(int position) {
        switch (position) {
            case 0: // Home
                startFragment(HomePageActivity.this, new HomeFragment());
                getActionBar().setTitle(R.string.app_name);
                break;
            case 1: // Social
                startFragment(HomePageActivity.this, new SocialUpdatesFragment());
                getActionBar().setTitle(R.string.social);
                break;
            case 2: // Instructors
                startFragment(HomePageActivity.this, new OurInstructorsFragment());
                getActionBar().setTitle(R.string.our_instructors);
                break;
            case 3: // Seminars
                break;
            case 4: // Register
                break;
            case 5:
                break;
            case 6:
                break;
        }
    }

    public static void startFragment(FragmentActivity fragmentActivity, Fragment fragment) {
        final FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        final String tag = fragment.getClass().getSimpleName();
        // only add fragment if it isn't already showing
        if (fragmentManager.findFragmentByTag(tag) == null) {
            fragmentTransaction.replace(R.id.content_frame, fragment, tag);
            fragmentTransaction.commit();
        }
    }

}
