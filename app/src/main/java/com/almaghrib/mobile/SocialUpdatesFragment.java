package com.almaghrib.mobile;

import android.content.Intent;
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

import com.almaghrib.mobile.facebook.ui.FacebookFragment;
import com.almaghrib.mobile.twitter.ui.TwitterFragment;
import com.almaghrib.mobile.youtube.ui.YouTubeFragment;

public class SocialUpdatesFragment extends Fragment {
    private static final String TAG = SocialUpdatesFragment.class.getSimpleName();

    private static final int TAB_ITEMS = 3;

    public SocialUpdatesFragment() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View layoutView = inflater.inflate(R.layout.home_page_main_content, container, false);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the app.
        final HomePageAdapter adapter = new HomePageAdapter(getActivity().getSupportFragmentManager());

        // The {@link ViewPager} that will host the section contents.
        final ViewPager viewPager = (ViewPager) layoutView.findViewById(R.id.pager);
        viewPager.setAdapter(adapter);

        return layoutView;
    }

    @Override
    public void onPause() {
        getActivity().setProgressBarIndeterminateVisibility(false);
        super.onPause();
    }

    @Override
    public void onStop() {
        // Remove all adapter fragments
        final View v = getView();
        if (v != null) {
            final ViewPager viewPager = (ViewPager) v.findViewById(R.id.pager);
            if (viewPager != null) {
                final HomePageAdapter adapter = (HomePageAdapter) viewPager.getAdapter();
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Pass the activity result to the Twitter fragment, which will then pass
        // the result to the login button.
        final View v = getView();
        if (v != null) {
            final ViewPager viewPager = (ViewPager) v.findViewById(R.id.pager);
            if (viewPager != null) {
                final HomePageAdapter adapter = (HomePageAdapter) viewPager.getAdapter();
                final Fragment fragment = adapter.getFragment(HomePageAdapter.POSITION_TWITTER);
                if (fragment != null) {
                    fragment.onActivityResult(requestCode, resultCode, data);
                }
            }
        }
    }

    /**
     * A {@link android.support.v4.app.FragmentStatePagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    protected static class HomePageAdapter extends FragmentStatePagerAdapter {
        protected static final int POSITION_YOUTUBE  = 0;
        protected static final int POSITION_FACEBOOK = 1;
        protected static final int POSITION_TWITTER  = 2;

        private SparseArray<Fragment> mPageReferenceArray;

        public HomePageAdapter(FragmentManager fragmentManager) {
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
                case POSITION_YOUTUBE:  // 0
                    fragment = YouTubeFragment.init(position);
                    break;
                case POSITION_FACEBOOK: // 1
                    fragment = FacebookFragment.init();
                    break;
                case POSITION_TWITTER:  // 2
                default:
                    fragment = TwitterFragment.init();
            }
            mPageReferenceArray.put(position, fragment);
            return fragment;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case POSITION_YOUTUBE:  // 0
                    return YouTubeFragment.getFragmentName();
                case POSITION_FACEBOOK: // 1
                    return FacebookFragment.getFragmentName();
                case POSITION_TWITTER:  // 2
                default:
                    return TwitterFragment.getFragmentName();
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
