package com.almaghrib.mobile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.almaghrib.mobile.almaghribApi.jsonModels.AlMaghribMyAccountsOrderHistoryModel;
import com.twitter.sdk.android.core.models.User;

import java.util.ArrayList;
import java.util.List;

public class MyAccountOrderHistoryArrayAdapter extends ArrayAdapter<AlMaghribMyAccountsOrderHistoryModel> {
    private static class OrderHistoryViewHolder {
        protected TextView seminarNameTextView;
        protected TextView infoTextView;
        protected TextView priceTextView;
    }

    public MyAccountOrderHistoryArrayAdapter(Context context, ArrayList<AlMaghribMyAccountsOrderHistoryModel> dataset) {
        super(context, R.layout.my_accounts_order_history_list_item, dataset);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final AlMaghribMyAccountsOrderHistoryModel model = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        OrderHistoryViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            viewHolder = new OrderHistoryViewHolder();
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.my_accounts_order_history_list_item, parent, false);

            viewHolder.seminarNameTextView = (TextView) convertView.findViewById(R.id.orderHistoryItemname);
            viewHolder.infoTextView = (TextView) convertView.findViewById(R.id.orderHistoryInfoTextView);
            viewHolder.priceTextView = (TextView) convertView.findViewById(R.id.priceTextView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (OrderHistoryViewHolder) convertView.getTag();
        }

        viewHolder.seminarNameTextView.setText(model.getSeminarName());
        viewHolder.infoTextView.setText(model.getInfo());
        viewHolder.priceTextView.setText(model.getPrice());

        return convertView;
    }

}