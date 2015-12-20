package com.almaghrib.mobile;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.florent37.materialviewpager.MaterialViewPager;
import com.github.florent37.materialviewpager.header.HeaderDesign;

public class HomeFragment extends Fragment {
    private static final String TAG = HomeFragment.class.getSimpleName();

    private MaterialViewPager mViewPager;
    private Toolbar mToolbar;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawer;

    public HomeFragment() {
        super();
    }

    public static String getFragmentName() {
        return "Home"; //TODO: use strings
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View layoutView = inflater.inflate(R.layout.home_page, container, false);

        getActivity().setTitle("");

        mViewPager = (MaterialViewPager) layoutView.findViewById(R.id.materialViewPager);

        mToolbar = mViewPager.getToolbar();

        if (mToolbar != null) {
            final AppCompatActivity parentActivity = (AppCompatActivity) getActivity();
            parentActivity.getSupportActionBar().hide();
            parentActivity.setSupportActionBar(mToolbar);

            final ActionBar actionBar = parentActivity.getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setDisplayShowHomeEnabled(true);
                actionBar.setDisplayShowTitleEnabled(true);
                actionBar.setDisplayUseLogoEnabled(false);
                actionBar.setHomeButtonEnabled(true);
            }
        }

        mViewPager.getViewPager().setAdapter(new HomeScreenTabsAdapter(getChildFragmentManager()));

        mViewPager.setMaterialViewPagerListener(new MaterialViewPager.Listener() {
            @Override
            public HeaderDesign getHeaderDesign(int page) {
                switch (page) {
                    case 0:
//                        return HeaderDesign.fromColorResAndUrl(
//                                R.color.green,
//                                "https://fs01.androidpit.info/a/63/0e/android-l-wallpapers-630ea6-h900.jpg");
                    case 1:
//                        return HeaderDesign.fromColorResAndUrl(
//                                R.color.blue,
//                                "http://cdn1.tnwcdn.com/wp-content/blogs.dir/1/files/2014/06/wallpaper_51.jpg");
                    case 2:
//                        return HeaderDesign.fromColorResAndUrl(
//                                R.color.cyan,
//                                "http://www.droid-life.com/wp-content/uploads/2014/10/lollipop-wallpapers10.jpg");
                    case 3:
//                        return HeaderDesign.fromColorResAndUrl(
//                                R.color.red,
//                                "http://www.tothemobile.com/wp-content/uploads/2014/07/original.jpg");
                    default:
                        return HeaderDesign.fromColorAndDrawable(
                                getResources().getColor(R.color.almaghrib_purple),
                                getResources().getDrawable(R.drawable.almaghrib_purple_banner));
                }

            }
        });

        mViewPager.getViewPager().setOffscreenPageLimit(mViewPager.getViewPager().getAdapter().getCount());
        mViewPager.getPagerTitleStrip().setViewPager(mViewPager.getViewPager());

        View logo = layoutView.findViewById(R.id.logo_white);
        if (logo != null)
            logo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewPager.notifyHeaderChanged();
                    Toast.makeText(getActivity().getApplicationContext(), "Yes, the title is clickable", Toast.LENGTH_SHORT).show();
                }
            });

        return layoutView;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mDrawer = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), mDrawer, 0, 0);
        mDrawer.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return mDrawerToggle.onOptionsItemSelected(item);
    }

    /**
     * A {@link android.support.v4.app.FragmentStatePagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    private static class HomeScreenTabsAdapter extends FragmentStatePagerAdapter {
        protected static final int POSITION_SEMINARS  = 0;
        protected static final int POSITION_NEWS = 1;
        protected static final int POSITION_CHECK_IN  = 2;
        protected static final int POSITION_JOURNALS = 3;

        private static final int NUM_TAB_ITEMS = 4;

        public HomeScreenTabsAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position % 4) {
                case POSITION_SEMINARS:
                    return SeminarsFragment.init();
                case POSITION_NEWS:
                    return SeminarsFragment.init();
                case POSITION_CHECK_IN:
                    return SeminarsFragment.init();
                case POSITION_JOURNALS:
                    return SeminarsFragment.init();
                default:
                    return SeminarsFragment.init();
            }
        }

        @Override
        public int getCount() {
            return NUM_TAB_ITEMS;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position % 4) {
                case POSITION_SEMINARS:
                    return SeminarsFragment.getFragmentName();
                case POSITION_NEWS:
                    return SeminarsFragment.getFragmentName();
                case POSITION_CHECK_IN:
                    return SeminarsFragment.getFragmentName();
                case POSITION_JOURNALS:
                    return SeminarsFragment.getFragmentName();
                default:
                    return "";
            }
        }
    }

}
