package com.poncholay.todolist.model.tab;

import android.support.v4.app.Fragment;

/**
 * Created by wilmot_g on 14/01/17.
 */

public class Tab {

	private String mTitle;
	private Fragment mFragment;

	public Tab(String title, Fragment fragment) {
		mTitle = title;
		mFragment = fragment;
	}

	public Fragment getFragment() {
		return mFragment;
	}

	public String getTitle() {
		return mTitle;
	}
}
