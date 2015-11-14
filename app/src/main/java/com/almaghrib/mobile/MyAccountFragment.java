package com.almaghrib.mobile;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.almaghrib.mobile.almaghribApi.jsonModels.AlMaghribMyAccountsOrderHistoryModel;

import java.util.ArrayList;

public class MyAccountFragment extends Fragment {

    private static final int VIEW_MORE_ITEMS_NUM = 2;

    private int mNumOrderHistoryItemToShow = 2;

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
        addOrderHistoryItems();

        attachOrderHistoryViewMoreListener();
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

        setListViewHeightBasedOnItems(methodListView, -1);
    }

    private void addOrderHistoryItems() {
//        final RecyclerView methodListView = (RecyclerView) getView().findViewById(R.id.orderHistoryListView);
//
//        methodListView.setHasFixedSize(true);
//
//        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
//        methodListView.setLayoutManager(mLayoutManager);
//
//        List<AlMaghribMyAccountsOrderHistoryModel> mDataset = new ArrayList<AlMaghribMyAccountsOrderHistoryModel>();
//        mDataset.add(new AlMaghribMyAccountsOrderHistoryModel("Fiqh of chilling", "Jan 29-31 Toronto", "$99"));
//        final MyAccountOrderHistoryAdapter mAdapter = new MyAccountOrderHistoryAdapter(mDataset);
//        methodListView.setAdapter(mAdapter);

        final ListView methodListView = (ListView) getView().findViewById(R.id.orderHistoryListView);

        ArrayList<AlMaghribMyAccountsOrderHistoryModel> mDataset = new ArrayList<AlMaghribMyAccountsOrderHistoryModel>();
        mDataset.add(new AlMaghribMyAccountsOrderHistoryModel("Fiqh of chilling", "Jan 29-31 Toronto", "$99"));
        mDataset.add(new AlMaghribMyAccountsOrderHistoryModel("Fiqh of more things", "July 29-31 London", "$199"));
        mDataset.add(new AlMaghribMyAccountsOrderHistoryModel("Aqeedah 101", "July 29-31 London", "$199"));
        mDataset.add(new AlMaghribMyAccountsOrderHistoryModel("Aqeedah 102", "July 29-31 London", "$199"));
        mDataset.add(new AlMaghribMyAccountsOrderHistoryModel("Aqeedah 201", "July 29-31 London", "$199"));
        mDataset.add(new AlMaghribMyAccountsOrderHistoryModel("Aqeedah 301", "July 29-31 London", "$199"));
        mDataset.add(new AlMaghribMyAccountsOrderHistoryModel("Aqeedah 402", "July 29-31 London", "$199"));

        methodListView.setAdapter(new MyAccountOrderHistoryArrayAdapter(getActivity(), mDataset));

        setListViewHeightBasedOnItems(methodListView, mNumOrderHistoryItemToShow);


    }

    private void attachOrderHistoryViewMoreListener() {
        final ImageView moreOrdersImageView = (ImageView) getView().findViewById(R.id.orderHistoryImageView3);
        final TextView moreOrdersTextView = (TextView) getView().findViewById(R.id.orderHistoryTextView6);

        final View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ListView methodListView = (ListView) getView().findViewById(R.id.orderHistoryListView);
                mNumOrderHistoryItemToShow =
                        (mNumOrderHistoryItemToShow+VIEW_MORE_ITEMS_NUM > methodListView.getCount())
                        ? methodListView.getCount()
                        : mNumOrderHistoryItemToShow + VIEW_MORE_ITEMS_NUM;
                setListViewHeightBasedOnItems(methodListView, mNumOrderHistoryItemToShow);
                // hide the view more layout if all items are showing
                if (mNumOrderHistoryItemToShow >= methodListView.getCount()) {
                    final View viewMoreLayout = getView().findViewById(R.id.orderHistoryViewMoreLayout);
                    viewMoreLayout.setVisibility(View.GONE);
                }
            }
        };
        moreOrdersImageView.setOnClickListener(listener);
        moreOrdersTextView.setOnClickListener(listener);
    }

    /**
     * Sets ListView height dynamically based on the height of the items or
     * a specified number of items.
     *
     * @param listView to be resized
     * @param numberOfItems the number of items high the listview should be, or
     *                      -1 to use height based on number of items
     * @return true if the listView is successfully resized, false otherwise
     */
    public static boolean setListViewHeightBasedOnItems(ListView listView, int numberOfItems) {
        final ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter != null) {

            numberOfItems = (numberOfItems > listAdapter.getCount() || numberOfItems == -1)
                    ? listAdapter.getCount() : numberOfItems;
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
