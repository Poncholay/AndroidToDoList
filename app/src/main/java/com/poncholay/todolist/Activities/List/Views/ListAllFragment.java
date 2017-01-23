package com.poncholay.todolist.Activities.List.Views;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.poncholay.todolist.R;
import com.poncholay.todolist.controller.task.TasksAdapter;
import com.poncholay.todolist.model.task.Task;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wilmot_g on 18/01/17.
 */

public class ListAllFragment extends Fragment {

	ListView mListView;
	ArrayList<Task> mTaskList;
	private ArrayAdapter<Task> mAdapter;

	public static ListAllFragment newInstance() {
		return new ListAllFragment();
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
