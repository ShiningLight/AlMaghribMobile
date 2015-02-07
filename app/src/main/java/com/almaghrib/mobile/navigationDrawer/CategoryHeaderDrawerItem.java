package com.almaghrib.mobile.navigationDrawer;

public class CategoryHeaderDrawerItem implements DrawerItem {
    private String mItemTitle;

    public CategoryHeaderDrawerItem(String itemTitle) {
        mItemTitle = itemTitle;
    }

    public String getItemName() {
        return mItemTitle;
    }

    public void setItemName(String itemName) {
        mItemTitle = itemName;
    }

    @Override
    public int getItemType() {
        return TYPE_CATEGORY;
    }
}
