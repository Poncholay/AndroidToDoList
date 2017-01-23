package com.poncholay.todolist.model.tab;

import com.poncholay.todolist.Activities.List.Fragments.TaskListFragment;

/**
 * Created by wilmot_g on 14/01/17.
 */

public class Tab {

	private String mTitle;
	private TaskListFragment mFragment;

	public Tab(String title, TaskListFragment fragment) {
		mTitle = title;
		mFragment = fragment;
	}

	public TaskListFragment getFragment() {
		return mFragment;
	}

	public String getTitle() {
		return mTitle;
	}
}
