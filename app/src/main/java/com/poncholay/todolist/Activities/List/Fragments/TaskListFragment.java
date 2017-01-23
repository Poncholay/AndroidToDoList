package com.poncholay.todolist.Activities.List.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.poncholay.todolist.model.task.Task;

import java.util.ArrayList;

/**
 * Created by wilmot_g on 23/01/17.
 */

public class TaskListFragment extends Fragment {

	protected ListView mListView;
	protected ArrayList<Task> mTaskList;
	protected ArrayAdapter<Task> mAdapter;

	public Task getTask(View view) {
		if (mListView != null) {
			int pos = mListView.getPositionForView(view);
			if (mAdapter != null) {
				return mAdapter.getItem(pos);
			}
		}
		return null;
	}

	public void deleteTask(Task task) {
		if (mAdapter != null) {
			mAdapter.remove(task);
			mAdapter.notifyDataSetChanged();
		}
	}

	public void editTask(Task task) {
		if (mAdapter != null) {
			mAdapter.remove(task);
			mAdapter.add(task);
			mAdapter.notifyDataSetChanged();
		}
	}

	public void createTask(Task task) {
		if (mAdapter != null) {
			mAdapter.add(task);
			mAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return super.onCreateView(inflater, container, savedInstanceState);
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


//	private ListView mTaskListView;
//	private ArrayAdapter<Task> mAdapter;
//
//	public TaskListFragment(ListView view) {
//		mTaskListView = view ;
//	}
//
//	public void createAdapter(Activity main, ArrayList<Task> taskList) {
//		mAdapter = new TasksAdapter(main, taskList);
//		mTaskListView.setAdapter(mAdapter);
//	}
//
//	public void update(List<Task> taskList) {
//		mAdapter.clear();
//		mAdapter.addAll(taskList);
//		mAdapter.notifyDataSetChanged();
//	}
//
//	public void addTask(Task task) {
//		if (task != null) {
//			mAdapter.add(task);
//			mAdapter.notifyDataSetChanged();
//		}
//	}
//
//	public void deleteTask(Task task) {
//		if (task != null) {
//			mAdapter.remove(task);
//			mAdapter.notifyDataSetChanged();
//		}
//	}
//
//	public ListView getList() {
//		return mTaskListView;
//	}
//
//	public ArrayAdapter<Task> getAdapter() {
//		return mAdapter;
//	}