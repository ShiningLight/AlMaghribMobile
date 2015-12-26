package com.almaghrib.mobile;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.almaghrib.mobile.instructors.OurInstructorsFragment;
import com.almaghrib.mobile.util.view.CircleNetworkImageView;
import com.android.volley.toolbox.ImageLoader;

public class HomePageActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String CURRENT_SELECTED_ITEM = "current_selected_item";
    private static final String CURRENT_TITLE = "current_title";

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private NavigationView mNavigationView;

    private MenuItem mActiveMenuItem;

    private int mCurrentlySelectedItem = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        // Set a toolbar to replace the action bar.
        final Toolbar toolbar = (Toolbar) findViewById(R.id.app_toolbar);
        setSupportActionBar(toolbar);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = getActionBarDrawerToggle(toolbar);
        // Tie DrawerLayout events to the ActionBarToggle
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mNavigationView = (NavigationView) findViewById(R.id.navigation_view);
        mNavigationView.setNavigationItemSelectedListener(this);

        setupNavViewHeader(mNavigationView);

        if (savedInstanceState != null) {
            // get selected menu item and re-set it as selected item on drawer
            mCurrentlySelectedItem = savedInstanceState.getInt(CURRENT_SELECTED_ITEM, 0);
            if (mCurrentlySelectedItem != 0) {
                mNavigationView.getMenu().findItem(mCurrentlySelectedItem).setChecked(true);
            }
            final String title = savedInstanceState.getString(CURRENT_TITLE, "");
            if (!title.isEmpty()) {
                getSupportActionBar().setTitle(title);
            }
        }
        // set and open first item on launch
        if (savedInstanceState == null && mCurrentlySelectedItem == 0) {
            //mDrawerList.setItemChecked(0, true);
            startFragment(this, new HomeFragment());
            setDrawerItem(R.id.home, getString(R.string.title_home));
        }

        /*mNavigationView.getMenu().setGroupCheckable(R.id.main_section_group, true, true);
        mNavigationView.getMenu().setGroupCheckable(R.id.ilm_section_group, true, true);*/

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setupNavViewHeader(NavigationView navigationView) {
        final View header = navigationView.findViewById(R.id.header);

        final AlMaghribSharedPreferences prefs =
                AlMaghribSharedPreferences.getInstance(getApplicationContext());

        final CircleNetworkImageView imageView = (CircleNetworkImageView) header.findViewById(R.id.profile_image);
        imageView.setDefaultImageResId(R.drawable.waleed);
        if (!TextUtils.isEmpty(prefs.getAlMaghribUserEmailMd5())) {
            final ImageLoader imageLoader =
                    RequestQueueSingleton.getInstance(getApplicationContext()).getImageLoader();
            imageView.setImageUrl(
                    getString(R.string.gravatar_avatar_url) + prefs.getAlMaghribUserEmailMd5(),
                    imageLoader);
        }
        if (!TextUtils.isEmpty(prefs.getAlMaghribUserFullName())) {
            final TextView name = (TextView) header.findViewById(R.id.username);
            name.setText(prefs.getAlMaghribUserFullName());
        }
        final TextView email = (TextView) header.findViewById(R.id.email);
        email.setText(prefs.getAlMaghribUserEmail());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(CURRENT_SELECTED_ITEM, mCurrentlySelectedItem);
        outState.putString(CURRENT_TITLE, getSupportActionBar().getTitle().toString());
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
	/**
	 * Called when invalidateOptionsMenu() is triggered
	 */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// if nav drawer is opened, hide the action items
		//boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
		//menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Pass the activity result to the fragment, which will then pass the result
        // to the login button.
        final Fragment fragment = getSupportFragmentManager()
                .findFragmentByTag(SocialUpdatesFragment.class.getSimpleName());
        if (fragment != null) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
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
        // Pass any configuration change to the drawer toggles
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    private ActionBarDrawerToggle getActionBarDrawerToggle(Toolbar toolbar) {
        return new ActionBarDrawerToggle(
                this, mDrawerLayout,
                toolbar,  /* nav drawer icon to replace 'Up' caret */
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


    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        //if an item from extras group is clicked,refresh NAV_ITEMS_MAIN to remove previously checked item
//        if (menuItem.getGroupId() == R.id.main_section_group) {
//            mNavigationView.getMenu().setGroupCheckable(R.id.main_section_group, true, true);
//            mNavigationView.getMenu().setGroupCheckable(R.id.ilm_section_group, false, true);
//        } else {
//            mNavigationView.getMenu().setGroupCheckable(R.id.ilm_section_group, true, true);
//            mNavigationView.getMenu().setGroupCheckable(R.id.ilm_section_group, true, true);
//            mNavigationView.getMenu().setGroupCheckable(R.id.main_section_group, false, true);
//        }
//
//        // Update selected/deselected MenuItems
//        if (mActiveMenuItem != null) {
//            mActiveMenuItem.setChecked(false);
//            if (mActiveMenuItem.hasSubMenu()) {
//                mActiveMenuItem.getSubMenu().setGroupCheckable(R.id.ilm_section_group, true, true);
//            }
//        }
//        mActiveMenuItem = menuItem;

        switch (menuItem.getItemId()) {
            case R.id.home: // Home
                startFragment(HomePageActivity.this, new HomeFragment());
                break;
            case R.id.social: // Social
                startFragment(HomePageActivity.this, new SocialUpdatesFragment());
                break;
            case R.id.instructors: // Instructors
                startFragment(HomePageActivity.this, new OurInstructorsFragment());
                break;
            case R.id.my_account: // My Account
                startFragment(HomePageActivity.this, new MyAccountFragment());
                break;
            case R.id.register: // Register
                // Don't set as selected item as it will open an external app
                final Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(getApplicationContext().getString(R.string.register_url)));
                startActivity(browserIntent);
                mDrawerLayout.closeDrawers();
                return true;
//            case R.id.ilmfest:
//                break;
//            case R.id.ilmsummit:
//                break;
        }

        menuItem.setChecked(true);
        mCurrentlySelectedItem = menuItem.getItemId();
        getSupportActionBar().setTitle(menuItem.getTitle());
        mDrawerLayout.closeDrawers();

        return true;
    }

    @Override
    public void onBackPressed() {
        // Close navigation drawer if open
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawers();
        } else {
            // If not in Home, move back to Home - just checking if fragment present
            final FragmentManager fragmentManager = getSupportFragmentManager();
            final String tag = "HomeFragment";
            if (fragmentManager.findFragmentByTag(tag) == null) {
                onNavigationItemSelected(mNavigationView.getMenu().findItem(R.id.home));
            } else {
                // must be on home page with home fragment present
                super.onBackPressed();
            }
        }
    }

    public void setDrawerItem(int itemId, String title) {
        mNavigationView.getMenu().findItem(itemId).setChecked(true);
        mCurrentlySelectedItem = itemId;
        getSupportActionBar().setTitle(title);
    }


    public void upcomingButtonAction(View view) {
        startFragment(this, new UpcomingSeminarsFragment());
        getSupportActionBar().setTitle(R.string.upcoming_title);
    }

    public void almaghribTalksButtonAction(View view) {
        startFragment(HomePageActivity.this, new SocialUpdatesFragment());
        getSupportActionBar().setTitle(R.string.title_social);
    }

    public void blogAndSocialButtonAction(View view) {
        startFragment(HomePageActivity.this, new SocialUpdatesFragment());
        getSupportActionBar().setTitle(R.string.title_social);
    }

    public void checkInButtonAction(View view) {
        startFragment(this, new CheckInFragment());
        getSupportActionBar().setTitle(R.string.check_in);
    }

    public static void startFragment(FragmentActivity fragmentActivity, Fragment fragment) {
        final FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        final String tag = fragment.getClass().getSimpleName();
        // only add fragment if it isn't already showing
        if (fragmentManager.findFragmentByTag(tag) == null) {
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
            fragmentTransaction.replace(R.id.content_frame, fragment, tag);
            fragmentTransaction.commit();
        }
    }

}
