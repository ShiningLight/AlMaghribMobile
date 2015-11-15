package com.almaghrib.mobile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.almaghrib.mobile.almaghribApi.jsonModels.AlMaghribMyAccountsExamHistoryModel;

import java.util.ArrayList;

public class MyAccountExamHistoryArrayAdapter extends ArrayAdapter<AlMaghribMyAccountsExamHistoryModel> {
    private static class OrderHistoryViewHolder {
        protected TextView seminarNameTextView;
        protected TextView infoTextView;
        protected TextView markTextView;
    }

    public MyAccountExamHistoryArrayAdapter(Context context, ArrayList<AlMaghribMyAccountsExamHistoryModel> dataset) {
        super(context, R.layout.my_account_exam_history_list_item, dataset);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final AlMaghribMyAccountsExamHistoryModel model = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        OrderHistoryViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            viewHolder = new OrderHistoryViewHolder();
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.my_account_exam_history_list_item, parent, false);

            viewHolder.seminarNameTextView = (TextView) convertView.findViewById(R.id.examHistoryItemname);
            viewHolder.infoTextView = (TextView) convertView.findViewById(R.id.examHistoryInfoTextView);
            viewHolder.markTextView = (TextView) convertView.findViewById(R.id.examHistoryPriceTextView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (OrderHistoryViewHolder) convertView.getTag();
        }

        viewHolder.seminarNameTextView.setText(model.getSeminarName());
        viewHolder.infoTextView.setText(model.getInfo());
        viewHolder.markTextView.setText(model.getMark());

        return convertView;
    }

}