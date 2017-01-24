package com.poncholay.todolist.model.tab;

import com.poncholay.todolist.Activities.List.Fragments.TaskListFragment;

public class Tab {

	final private String mTitle;
	final private TaskListFragment mFragment;

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
