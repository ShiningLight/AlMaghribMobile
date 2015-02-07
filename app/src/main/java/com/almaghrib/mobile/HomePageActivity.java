package com.almaghrib.mobile;

import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.almaghrib.mobile.facebook.ui.FacebookFragment;
import com.almaghrib.mobile.navigationDrawer.CategoryHeaderDrawerItem;
import com.almaghrib.mobile.navigationDrawer.DrawerItem;
import com.almaghrib.mobile.navigationDrawer.ListHeaderDrawerItem;
import com.almaghrib.mobile.navigationDrawer.NavigationDrawerAdapter;
import com.almaghrib.mobile.navigationDrawer.NormalDrawerItem;
import com.almaghrib.mobile.youtube.ui.YouTubeFragment;

import java.util.ArrayList;

public class HomePageActivity extends FragmentActivity {

    private static final int TAB_ITEMS = 3;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private HomePageAdapter mAdapter;
    
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

	// slide menu items
	private String[] navMenuTitles;
	private TypedArray navMenuIcons;
	private ArrayList<DrawerItem> navDrawerItems;
	private NavigationDrawerAdapter adapter;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_home_page);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the app.
        mAdapter = new HomePageAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mAdapter);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
		// load slide menu items
		final String[] navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);
		navMenuIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons);
		/*mDrawerList.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, navMenuTitles));*/
                //R.layout.drawer_list_item, navMenuTitles));
		navDrawerItems = new ArrayList<DrawerItem>();
        // adding nav drawer items to array
        navDrawerItems.add(new ListHeaderDrawerItem("Alan Parker", "alan@gmail.com"));

        navDrawerItems.add(new NormalDrawerItem(navMenuTitles[0], navMenuIcons
                .getResourceId(0, -1)));
        navDrawerItems.add(new NormalDrawerItem(navMenuTitles[1], navMenuIcons
                .getResourceId(1, -1)));

        navDrawerItems.add(new CategoryHeaderDrawerItem("My Al Maghrib"));
		navDrawerItems.add(new NormalDrawerItem(navMenuTitles[2], navMenuIcons
				.getResourceId(2, -1)));

        navDrawerItems.add(new CategoryHeaderDrawerItem("About Al Maghrib"));
        navDrawerItems.add(new NormalDrawerItem(navMenuTitles[3], navMenuIcons
                .getResourceId(3, -1)));
        navDrawerItems.add(new NormalDrawerItem(navMenuTitles[4], navMenuIcons
				.getResourceId(4, -1)));
		navDrawerItems.add(new NormalDrawerItem(navMenuTitles[5], navMenuIcons
				.getResourceId(5, -1)));

        navDrawerItems.add(new NormalDrawerItem("Settings"));

		// Recycle the typed array
		navMenuIcons.recycle();
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		// setting the nav drawer list adapter
		adapter = new NavigationDrawerAdapter(getApplicationContext(), navDrawerItems);
		mDrawerList.setAdapter(adapter);
        
        mDrawerToggle = new ActionBarDrawerToggle(
                this, mDrawerLayout,
                R.drawable.ic_drawer,  /* nav drawer icon to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description */
                R.string.drawer_close  /* "close drawer" description */
                ) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getActionBar().setTitle(R.string.app_name);
                // calling onPrepareOptionsMenu() to show action bar icons
				invalidateOptionsMenu();
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActionBar().setTitle(R.string.drawer_opened_title);
                // calling onPrepareOptionsMenu() to hide action bar icons
				invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        //getActionBar().setHomeButtonEnabled(true);
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
    
	/**
	 * Slide menu item click listener
	 */
	private class DrawerItemClickListener implements ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view,
				int position, long id) {
			// display view for selected nav drawer item
			handleDrawerItemClick(position);
		}
	}
    
	private void handleDrawerItemClick(int position) {
		
	}
    
    /**
     * A {@link FragmentStatePagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public static class HomePageAdapter extends FragmentStatePagerAdapter {
        
        public HomePageAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public int getCount() {
            return TAB_ITEMS;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
            case 0: 
                return YouTubeFragment.init(position);
            case 1:
                return FacebookFragment.init(position);
            default:// Fragment # 2-9 - Will show list
                return YouTubeFragment.init(position);//ArrayListFragment.init(position);
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
            case 0:
                return YouTubeFragment.getFragmentName();
            case 1:
                return FacebookFragment.getFragmentName();
            default:
                return YouTubeFragment.getFragmentName();
            }
        }
    }

}
