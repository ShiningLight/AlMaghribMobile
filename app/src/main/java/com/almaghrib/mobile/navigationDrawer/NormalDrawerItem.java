package com.almaghrib.mobile.navigationDrawer;

public class NormalDrawerItem implements DrawerItem {
	private String mItemTitle;
	private int mImageResId;

    public NormalDrawerItem(String itemTitle) {
        this(itemTitle, -1);
    }

	public NormalDrawerItem(String itemTitle, int imgResID) {
		mItemTitle = itemTitle;
		mImageResId = imgResID;
	}

	public String getItemName() {
		return mItemTitle;
	}

	public void setItemName(String itemName) {
		mItemTitle = itemName;
	}

	public int getImageResId() {
		return mImageResId;
	}

	public void setImageResId(int mImageResId) {
		mImageResId = mImageResId;
	}

    @Override
    public int getItemType() {
        return TYPE_NORMAL;
    }
}
