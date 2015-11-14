package com.almaghrib.mobile;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

public class MyAccountFragment extends Fragment {

    public static MyAccountFragment init() {
        final MyAccountFragment fragment = new MyAccountFragment();
        return fragment;
    }

    public static String getFragmentName() {
        return "My Account"; //TODO: use strings
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO: trigger off fetch

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View layoutView = inflater.inflate(R.layout.my_account_page, container, false);

        return layoutView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // todo: remove from here and call on Volley callback
        addPaymentMethodsItems();
    }

    private void addPaymentMethodsItems() {
        final ListView methodListView = (ListView) getView().findViewById(R.id.paymentMethodListView);

        String[] itemname ={
                "VISA - 6743",
                "MASTERCARD - 6743",
                "AMCARD - 7861"
        };
        methodListView.setAdapter(new ArrayAdapter<String>(
                getActivity(), R.layout.my_accounts_payment_methods_list_item,
                R.id.Itemname, itemname));

        setListViewHeightBasedOnItems(methodListView);
    }

    /**
     * Sets ListView height dynamically based on the height of the items.
     *
     * @param listView to be resized
     * @return true if the listView is successfully resized, false otherwise
     */
    public static boolean setListViewHeightBasedOnItems(ListView listView) {
        final ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter != null) {

            final int numberOfItems = listAdapter.getCount();

            // Get total height of all items.
            int totalItemsHeight = 0;
            for (int itemPos = 0; itemPos < numberOfItems; itemPos++) {
                View item = listAdapter.getView(itemPos, null, listView);
                item.measure(0, 0);
                totalItemsHeight += item.getMeasuredHeight();
            }

            // Get total height of all item dividers.
            int totalDividersHeight = listView.getDividerHeight() * (numberOfItems - 1);

            // Set list height.
            final ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalItemsHeight + totalDividersHeight;
            listView.setLayoutParams(params);
            listView.requestLayout();

            return true;

        } else {
            return false;
        }
    }

}
