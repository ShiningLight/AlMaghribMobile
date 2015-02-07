package com.almaghrib.mobile.navigationDrawer;

public interface DrawerItem {
    static final int TYPE_LIST_HEADER = 0;
    static final int TYPE_CATEGORY = 1;
    static final int TYPE_NORMAL = 2;

    /* Number of views types */
    static final int HEADER_TYPE_COUNT = 3;

    int getItemType();
}
