package com.almaghrib.mobile;

import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;

import com.github.florent37.materialviewpager.MaterialViewPager;
import com.github.florent37.materialviewpager.header.HeaderDesign;

public class HomeFragment extends MaterialViewPagerFragment {
    private static final String TAG = HomeFragment.class.getSimpleName();

    public HomeFragment() {
        super();
    }

    public static String getFragmentName() {
        return "Home"; //TODO: use strings
    }

    @Override
    protected PagerAdapter getFragmentStatePagerAdaper(FragmentManager childFragmentManager) {
        return new HomeScreenTabsAdapter(childFragmentManager);
    }

    @Override
    protected MaterialViewPager.Listener getMaterialViewPagerListener(Resources resources) {
        return new HomeMaterialViewPagerListener(resources);
    }

    private static class HomeMaterialViewPagerListener implements MaterialViewPager.Listener {
        private final Resources mResources;

        public HomeMaterialViewPagerListener(final Resources resources) {
            mResources = resources;
        }

        @Override
        public HeaderDesign getHeaderDesign(int page) {
            switch (page) {
                case 0:
                    return HeaderDesign.fromColorAndDrawable(
                            mResources.getColor(R.color.almaghrib_purple),
                            mResources.getDrawable(R.drawable.banner_one));
                case 1:
                    return HeaderDesign.fromColorAndDrawable(
                            mResources.getColor(R.color.almaghrib_purple),
                            mResources.getDrawable(R.drawable.banner_two));
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
                            mResources.getColor(R.color.almaghrib_purple),
                            mResources.getDrawable(R.drawable.banner_one));
            }
        }
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
                    return NewsFragment.init();
                case POSITION_CHECK_IN:
                    return CheckInFragment.init();
                case POSITION_JOURNALS:
                    return LatestJournalsFragment.init();
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
                    return NewsFragment.getFragmentName();
                case POSITION_CHECK_IN:
                    return CheckInFragment.getFragmentName();
                case POSITION_JOURNALS:
                    return LatestJournalsFragment.getFragmentName();
                default:
                    return "";
            }
        }
    }

}
