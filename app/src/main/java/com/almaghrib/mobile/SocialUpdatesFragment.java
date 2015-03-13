package com.almaghrib.mobile;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.almaghrib.mobile.facebook.ui.FacebookFragment;
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
    public void onDestroyView() {
        if (getActivity().getSupportFragmentManager().getFragments().contains(this)) {
            getActivity().getSupportFragmentManager().beginTransaction().remove(this);
        }
        super.onDestroyView();
    }

    /**
     * A {@link android.support.v4.app.FragmentStatePagerAdapter} that returns a fragment corresponding to
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
                default:// TODO: Replace with Twitter
                    return YouTubeFragment.init(position);
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
