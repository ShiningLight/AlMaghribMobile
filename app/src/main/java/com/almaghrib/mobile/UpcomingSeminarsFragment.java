package com.almaghrib.mobile;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.florent37.materialviewpager.MaterialViewPager;
import com.github.florent37.materialviewpager.header.HeaderDesign;

public class UpcomingSeminarsFragment extends MaterialViewPagerFragment {

    private static final String TAG = SocialUpdatesFragment.class.getSimpleName();

    public UpcomingSeminarsFragment() {
        super();
    }

    @Override
    public void onStop() {
        // Remove all adapter fragments
        final View v = getView();
        if (v != null) {
            final ViewPager viewPager = (ViewPager) v.findViewById(R.id.pager);
            if (viewPager != null) {
                final UpcomingSeminarsAdapter adapter = (UpcomingSeminarsAdapter) viewPager.getAdapter();
                final FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

                final SparseArray<Fragment> fragments = adapter.getAllFragments();
                if (fragments != null) {
                    for (int i = 0; i < fragments.size(); i++) {
                        fragmentManager.beginTransaction().remove(fragments.get(i)).commitAllowingStateLoss();
                    }
                }
            }
        }
        if (getActivity().getSupportFragmentManager().getFragments().contains(this)) {
            getActivity().getSupportFragmentManager().beginTransaction().remove(this).commitAllowingStateLoss();
        }
        super.onStop();
    }

    @Override
    protected PagerAdapter getFragmentStatePagerAdaper(FragmentManager childFragmentManager) {
        return new UpcomingSeminarsAdapter(childFragmentManager);
    }

    @Override
    protected MaterialViewPager.Listener getMaterialViewPagerListener(Resources resources) {
        return new UpcomingSeminarsMaterialViewPagerListener(resources);
    }

    private static class UpcomingSeminarsMaterialViewPagerListener implements MaterialViewPager.Listener {
        private final Resources mResources;

        public UpcomingSeminarsMaterialViewPagerListener(final Resources resources) {
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
    private static class UpcomingSeminarsAdapter extends FragmentStatePagerAdapter {
        private static final int POSITION_SEMINARS  = 0;
        private static final int POSITION_ONLINE = 1;

        private static final int TAB_ITEMS = 2;

        private SparseArray<Fragment> mPageReferenceArray;

        public UpcomingSeminarsAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
            mPageReferenceArray = new SparseArray<Fragment>();
        }

        @Override
        public int getCount() {
            return TAB_ITEMS;
        }

        @Override
        public Fragment getItem(int position) {
            final Fragment fragment;
            switch (position) {
                case POSITION_SEMINARS:  // 0
                    fragment = OnlineSeminarsFragment.init();
                    break;
                case POSITION_ONLINE: // 1
                    fragment = OnlineSeminarsFragment.init();
                    break;
                default:
                    fragment = OnlineSeminarsFragment.init();
            }
            mPageReferenceArray.put(position, fragment);
            return fragment;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case POSITION_SEMINARS:  // 0
                    return OnlineSeminarsFragment.getFragmentName();
                case POSITION_ONLINE: // 1
                    return OnlineSeminarsFragment.getFragmentName();
                default:
                    return OnlineSeminarsFragment.getFragmentName();
            }
        }

        /**
         * After an orientation change, the fragments are saved in the adapter, and
         * I don't want to double save them: I will retrieve them and put them in my
         * list again here.
         */
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            final Fragment fragment = (Fragment) super.instantiateItem(container, position);
            mPageReferenceArray.put(position, fragment);
            return fragment;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
            mPageReferenceArray.remove(position);
        }

        public Fragment getFragment(int key) {
            return mPageReferenceArray.get(key);
        }

        public SparseArray<Fragment> getAllFragments() {
            return mPageReferenceArray;
        }
    }

}
