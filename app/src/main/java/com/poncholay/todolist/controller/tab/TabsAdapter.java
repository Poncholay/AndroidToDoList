package com.poncholay.todolist.controller.tab;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.poncholay.todolist.model.tab.Tab;

import java.util.List;

/**
 * Created by wilmot_g on 14/01/17.
 */

public class TabsAdapter extends FragmentPagerAdapter {
	private List<Tab> mTabs;

	public TabsAdapter(FragmentManager fm, List<Tab> tabs) {
		super(fm);
		mTabs = tabs;
	}

	@Override
	public Fragment getItem(int position) {
		return mTabs.get(position).getFragment();
	}

	@Override
	public int getCount() {
		return mTabs.size();
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return mTabs.get(position).getTitle();
	}
}