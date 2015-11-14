package com.almaghrib.mobile;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.almaghrib.mobile.almaghribApi.jsonModels.AlMaghribMyAccountsOrderHistoryModel;

import java.util.List;

public class MyAccountOrderHistoryAdapter extends RecyclerView.Adapter<MyAccountOrderHistoryAdapter.OrderHistoryViewHolder> {

    private List<AlMaghribMyAccountsOrderHistoryModel> mDataset;

    protected static class OrderHistoryViewHolder extends RecyclerView.ViewHolder {
        protected TextView seminarNameTextView;
        protected TextView infoTextView;
        protected TextView priceTextView;

        public OrderHistoryViewHolder(View v) {
            super(v);
            seminarNameTextView = (TextView) v.findViewById(R.id.orderHistoryItemname);
            infoTextView = (TextView) v.findViewById(R.id.orderHistoryInfoTextView);
            priceTextView = (TextView) v.findViewById(R.id.priceTextView);
        }
    }

    public MyAccountOrderHistoryAdapter(List<AlMaghribMyAccountsOrderHistoryModel> dataset) {
        mDataset = dataset;
    }

    @Override
    public OrderHistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_accounts_order_history_list_item, parent, false);
        return new OrderHistoryViewHolder(v);
    }

    @Override
    public void onBindViewHolder(OrderHistoryViewHolder holder, int position) {
        final AlMaghribMyAccountsOrderHistoryModel model = mDataset.get(position);

        holder.seminarNameTextView.setText(model.getSeminarName());
        holder.infoTextView.setText(model.getInfo());
        holder.priceTextView.setText(model.getPrice());
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void updateItems(List<AlMaghribMyAccountsOrderHistoryModel> dataset) {
        mDataset = dataset;
        notifyDataSetChanged();
    }
}
