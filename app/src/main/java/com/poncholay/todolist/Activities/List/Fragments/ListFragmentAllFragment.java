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
import java.util.List;

/**
 * Created by wilmot_g on 18/01/17.
 */

public class ListFragmentAllFragment extends TaskListFragment {

	public static ListFragmentAllFragment newInstance() {
		return new ListFragmentAllFragment();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mTaskList = new ArrayList<>();
		if (savedInstanceState == null) {
			savedInstanceState = getArguments();
		}
		if (savedInstanceState != null) {
			List<String> JSONtasks = getArguments().getStringArrayList("tasks");
			if (JSONtasks != null) {
				for (String json : JSONtasks) {
					Task task = Task.fromJSON(json);
					mTaskList.add(task);
				}
			}
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_list, container, false);
		mListView = (ListView) v.findViewById(R.id.list_todo);
		mAdapter = new TasksAdapter(getActivity(), mTaskList);
		mListView.setAdapter(mAdapter);
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
