package com.almaghrib.mobile.navigationDrawer;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.almaghrib.mobile.R;

public class NavigationDrawerAdapter extends ArrayAdapter<DrawerItem> {
    /**
     * ViewHolder object for drawer item to prevent needless calls to findViewById
     */
    private static class DrawerItemViewHolder {
        ImageView icon;
        TextView  title;
    }

    private static class CategoryHeaderDrawerItemViewHolder {
        TextView  title;
    }

    private final LayoutInflater mInflater;

    public NavigationDrawerAdapter(Context context, List<DrawerItem> items) {
        super(context, 0, items);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final DrawerItem item = getItem(position);
        final int itemType = getItemViewType(position);

        switch (itemType) {
            //TODO: add case for list header
            case DrawerItem.TYPE_LIST_HEADER:
                convertView = mInflater.inflate(R.layout.drawer_item_list_header, parent, false);
                final TextView userName = (TextView) convertView.findViewById(R.id.user_name);
                final TextView userEmail = (TextView) convertView.findViewById(R.id.user_email);

                final ListHeaderDrawerItem listHeaderItem = (ListHeaderDrawerItem) item;
                userName.setText(listHeaderItem.getUserName());
                userEmail.setText(listHeaderItem.getUserEmail());

                break;

            case DrawerItem.TYPE_CATEGORY:
                CategoryHeaderDrawerItemViewHolder holder;
                if ((convertView == null) ||
                        !(convertView.getTag() instanceof CategoryHeaderDrawerItemViewHolder)) {
                    holder = new CategoryHeaderDrawerItemViewHolder();
                    convertView = mInflater.inflate(R.layout.drawer_item_category_header, parent, false);
                    holder.title = (TextView) convertView.findViewById(R.id.drawer_title);
                    convertView.setTag(holder);

                } else {
                    holder = (CategoryHeaderDrawerItemViewHolder) convertView.getTag();
                }
                final CategoryHeaderDrawerItem categoryHeaderItem = (CategoryHeaderDrawerItem) item;
                holder.title.setText(categoryHeaderItem.getItemName());

                break;
    
            case DrawerItem.TYPE_NORMAL:
                DrawerItemViewHolder normalHolder;
                if ((convertView == null) ||
                        !(convertView.getTag() instanceof DrawerItemViewHolder)) {
                    normalHolder = new DrawerItemViewHolder();
                    convertView = mInflater.inflate(R.layout.drawer_item_normal_item, parent, false);
                    normalHolder.icon = (ImageView) convertView.findViewById(R.id.drawer_icon);
                    normalHolder.title = (TextView) convertView.findViewById(R.id.drawer_item_name);
                    convertView.setTag(normalHolder);

                } else {
                    normalHolder = (DrawerItemViewHolder) convertView.getTag();
                }
                final NormalDrawerItem normalItem = (NormalDrawerItem) item;
                final int imageId = normalItem.getImageResId();
                if (imageId != -1) {
                    normalHolder.icon.setVisibility(View.VISIBLE);
                    normalHolder.icon.setImageResource(imageId);
                } else {
                    normalHolder.icon.setVisibility(View.GONE);
                }
                normalHolder.title.setText(normalItem.getItemName());

                break;
        }

        return convertView;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false; // Category dividers and list header not enabled
    }

    @Override
    public boolean isEnabled(int position) {
        // Category dividers and list header should not be
        // selectable/clickable hence not enabled
        return getItemViewType(position) == DrawerItem.TYPE_NORMAL;
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).getItemType();
    }
    
    @Override
    public boolean hasStableIds() { 
        return true;
    }

    @Override
    public int getViewTypeCount() {
        return DrawerItem.HEADER_TYPE_COUNT;
    }
    
}