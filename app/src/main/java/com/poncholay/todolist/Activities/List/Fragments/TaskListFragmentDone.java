package com.poncholay.todolist.Activities.List.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.poncholay.todolist.R;
import com.poncholay.todolist.controller.task.TasksAdapter;
import com.poncholay.todolist.model.task.Task;

import java.util.ArrayList;

/**
 * Created by wilmot_g on 24/01/17.
 */

public class TaskListFragmentDone extends TaskListFragment {

	public static TaskListFragmentAll newInstance() {
		return new TaskListFragmentAll();
	}

	@Override
	public void onCreate(Bundle state) {
		super.onCreate(state);
//		mTaskList = new ArrayList<>();
		if (state == null) {
			state = getArguments();
		}
		mAdapter = new TasksAdapter(getActivity(), new ArrayList<Task>());
		retrieveTasks(state);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_list, container, false);
		mListView = (ListView) v.findViewById(R.id.list_todo);
//		mAdapter = new TasksAdapter(getActivity(), mTaskList);
		mListView.setAdapter(mAdapter);
		addClickListeners();
		return v;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}
}
