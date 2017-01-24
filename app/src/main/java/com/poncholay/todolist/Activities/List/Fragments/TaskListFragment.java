package com.poncholay.todolist.Activities.List.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.poncholay.todolist.Activities.List.ListActivity;
import com.poncholay.todolist.model.task.Task;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wilmot_g on 23/01/17.
 */

public class TaskListFragment extends Fragment {

	protected ListView mListView;
	protected ArrayAdapter<Task> mAdapter;
	Boolean done;

	public void deleteTask(Task task) {
		if (mAdapter != null && task != null) {
			mAdapter.remove(task);
			mAdapter.notifyDataSetChanged();
		}
	}

	public void editTask(Task task) {
		if (mAdapter != null && task != null) {
			int pos = mAdapter.getPosition(task);
			mAdapter.remove(task);
			if (task.getDone() == done) {
				mAdapter.insert(task, pos == -1 ? mAdapter.getCount() : pos);
			}
			mAdapter.notifyDataSetChanged();
		}
	}

	public void createTask(Task task) {
		if (mAdapter != null && task != null) {
			if (task.getDone() == done) {
				mAdapter.add(task);
				mAdapter.notifyDataSetChanged();
			}
		}
	}

	protected void addClickListeners() {
		mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> av, View v, int pos, long id) {
				((ListActivity) getActivity()).showTaskMenu(mAdapter.getItem(pos));
				return true;
			}
		});
		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> av, View v, int pos, long id) {
				((ListActivity) getActivity()).callEditTaskActivity(mAdapter.getItem(pos));
			}
		});
	}

	protected void retrieveTasks(Bundle state) {
		this.done = state != null && state.getBoolean("done");
		if (getActivity() != null) {
			List<Task> tasks = ((ListActivity) getActivity()).getTasks();
			for (Task task : tasks) {
				if (task.getDone() == done) {
					mAdapter.add(task);
				}
			}
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
		outState.putBoolean("done", done);
	}
}