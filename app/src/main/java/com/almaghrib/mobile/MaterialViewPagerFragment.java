package com.almaghrib.mobile;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.github.florent37.materialviewpager.MaterialViewPager;

public abstract class MaterialViewPagerFragment extends Fragment {

    private MaterialViewPager mViewPager;
    private Toolbar mToolbar;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawer;

    public MaterialViewPagerFragment() {
        super();
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

        mViewPager.getViewPager().setAdapter(getFragmentStatePagerAdaper(getChildFragmentManager()));

        mViewPager.setMaterialViewPagerListener(getMaterialViewPagerListener(getResources()));

        mViewPager.getViewPager().setOffscreenPageLimit(mViewPager.getViewPager().getAdapter().getCount());
        mViewPager.getPagerTitleStrip().setViewPager(mViewPager.getViewPager());

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

    protected abstract PagerAdapter getFragmentStatePagerAdaper(FragmentManager childFragmentManager);

    protected abstract MaterialViewPager.Listener getMaterialViewPagerListener(Resources resources);
}
