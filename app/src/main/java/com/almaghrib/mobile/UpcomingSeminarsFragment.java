package com.almaghrib.mobile;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class UpcomingSeminarsFragment extends Fragment {

    private static final String TAG = SocialUpdatesFragment.class.getSimpleName();

    private static final int TAB_ITEMS = 2;

    public UpcomingSeminarsFragment() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View layoutView = inflater.inflate(R.layout.home_page_main_content, container, false);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the app.
        final UpcomingSeminarsAdapter adapter =
                new UpcomingSeminarsAdapter(getActivity().getSupportFragmentManager());

        // The {@link ViewPager} that will host the section contents.
        final ViewPager viewPager = (ViewPager) layoutView.findViewById(R.id.pager);
        viewPager.setAdapter(adapter);

        return layoutView;
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

    /**
     * A {@link android.support.v4.app.FragmentStatePagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    protected static class UpcomingSeminarsAdapter extends FragmentStatePagerAdapter {
        protected static final int POSITION_SEMINARS  = 0;
        protected static final int POSITION_ONLINE = 1;

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
                    fragment = SeminarsFragment.init();
                    break;
                case POSITION_ONLINE: // 1
                    fragment = OnlineSeminarsFragment.init();
                    break;
                default:
                    fragment = SeminarsFragment.init();
            }
            mPageReferenceArray.put(position, fragment);
            return fragment;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case POSITION_SEMINARS:  // 0
                    return SeminarsFragment.getFragmentName();
                case POSITION_ONLINE: // 1
                    return OnlineSeminarsFragment.getFragmentName();
                default:
                    return SeminarsFragment.getFragmentName();
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
