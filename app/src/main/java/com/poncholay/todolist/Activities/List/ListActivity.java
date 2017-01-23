package com.poncholay.todolist.Activities.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.github.ybq.parallaxviewpager.Mode;
import com.github.ybq.parallaxviewpager.ParallaxViewPager;
import com.poncholay.todolist.Activities.CreateTask.CreateTaskActivity;
import com.poncholay.todolist.Activities.List.Fragments.ListFragmentAllFragment;
import com.poncholay.todolist.Activities.List.Fragments.TaskListFragment;
import com.poncholay.todolist.Constants;
import com.poncholay.todolist.R;
import com.poncholay.todolist.controller.db.DatabaseActions;
import com.poncholay.todolist.controller.db.DatabaseHelper;
import com.poncholay.todolist.controller.tab.TabsAdapter;
import com.poncholay.todolist.model.tab.Tab;
import com.poncholay.todolist.model.task.Task;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wilmot_g on 10/01/17.
 */

public class ListActivity extends AppCompatActivity {

	private DatabaseActions mDatabase;
	private List<Task> mTasks;
	private List<Tab> mTabs;

	private static final String TAG = "ListActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list);

		mDatabase = new DatabaseActions(new DatabaseHelper(this));
		mTasks = mDatabase.getTasks();

		mTabs = createFragments();
		ParallaxViewPager mParallaxViewPager = (ParallaxViewPager) findViewById(R.id.viewpager);
		TabsAdapter tabsAdapter = new TabsAdapter(getSupportFragmentManager(), mTabs);
		mParallaxViewPager.setAdapter(tabsAdapter);
		mParallaxViewPager.setOffscreenPageLimit(1);
		mParallaxViewPager.setMode(Mode.LEFT_OVERLAY);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_add_task:
				callCreateTaskActivity();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == AppCompatActivity.RESULT_OK) {
			String result = data.getStringExtra("task");
			if (result != null) {
				Task task = Task.fromJSON(result);
				if (requestCode == Constants.ADD_TASK) {
					createTask(task);
				} else if (requestCode == Constants.EDIT_TASK) {
					editTask(task);
				}
			}
		}
	}

	private List<Tab> createFragments() {
		List<Tab> fList = new ArrayList<>();
		TaskListFragment listAllFragment = ListFragmentAllFragment.newInstance();
		TaskListFragment tmpFragment = ListFragmentAllFragment.newInstance();


		ArrayList<String> list = new ArrayList<>();
		for (Task task : mTasks) {
			list.add(task.toJSON());
		}

		Bundle bundle = new Bundle();
		bundle.putStringArrayList("tasks", list);

		listAllFragment.setArguments(bundle);
		tmpFragment.setArguments(bundle);
		fList.add(new Tab("All", listAllFragment));
		fList.add(new Tab("By Month", tmpFragment));
		return fList;
	}

	private void createTask(Task task) {
		task = mDatabase.addTask(task);
		mTasks.add(task);
		for (Tab tab : mTabs) {
			TaskListFragment taskListFragment = tab.getFragment();
			taskListFragment.createTask(task);
		}
	}

	private void editTask(Task task) {
		mDatabase.editTask(task);
		mTasks.remove(task);
		mTasks.add(task);
		for (Tab tab : mTabs) {
			TaskListFragment taskListFragment = tab.getFragment();
			taskListFragment.editTask(task);
		}

	}

	public void deleteTask(View view) {
		Task task = mTabs.get(0).getFragment().getTask(view);
		if (task != null) {
			mDatabase.deleteTask(task);
			mTasks.remove(task);
			for (Tab tab : mTabs) {
				TaskListFragment taskListFragment = tab.getFragment();
				taskListFragment.deleteTask(task);
			}
		}
	}

	public void callCreateTaskActivity() {
		Intent createTask = new Intent(this, CreateTaskActivity.class);
		startActivityForResult(createTask, Constants.ADD_TASK);
	}

	public void callEditTaskActivity(View view) {
		Task task = mTabs.get(0).getFragment().getTask(view);
		if (task != null) {
			Intent callCreateTaskActivity = new Intent(this, CreateTaskActivity.class);
			callCreateTaskActivity.putExtra("task", task.toJSON());
			startActivityForResult(callCreateTaskActivity, Constants.EDIT_TASK);
		}
	}
}