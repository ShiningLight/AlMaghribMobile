package com.almaghrib.mobile.navigationDrawer;

public class ListHeaderDrawerItem implements DrawerItem {
    private String mUserName;
    private String mUserEmail;

    public ListHeaderDrawerItem(String userName, String userEmail) {
        mUserName = userName;
        mUserEmail = userEmail;
    }

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String userName) {
        mUserName = userName;
    }

    public String getUserEmail() {
        return mUserEmail;
    }

    public void setUserEmail(String userEmail) {
        mUserEmail = userEmail;
    }

    @Override
    public int getItemType() {
        return TYPE_LIST_HEADER;
    }
}
