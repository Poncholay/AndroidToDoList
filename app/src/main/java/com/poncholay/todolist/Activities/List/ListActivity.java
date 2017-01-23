package com.poncholay.todolist.Activities.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.github.ybq.parallaxviewpager.Mode;
import com.github.ybq.parallaxviewpager.ParallaxViewPager;
import com.poncholay.todolist.Activities.CreateTask.CreateTaskActivity;
import com.poncholay.todolist.Activities.List.Views.ListAllFragment;
import com.poncholay.todolist.Constants;
import com.poncholay.todolist.R;
import com.poncholay.todolist.controller.db.TaskActions;
import com.poncholay.todolist.controller.db.TaskDbHelper;
import com.poncholay.todolist.controller.tab.TabsAdapter;
import com.poncholay.todolist.model.tab.Tab;
import com.poncholay.todolist.model.task.Task;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wilmot_g on 10/01/17.
 */

public class ListActivity extends AppCompatActivity {

	TaskActions database;
	List<Task> tasks;
	private static final String TAG = "ListActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list);

		database = new TaskActions(new TaskDbHelper(this));
		tasks = database.getTasks();

		final List<Tab> fragments = createFragments();
		ParallaxViewPager mParallaxViewPager = (ParallaxViewPager) findViewById(R.id.viewpager);
		TabsAdapter tabsAdapter = new TabsAdapter(getSupportFragmentManager(), fragments);
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
				return createTask();
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == AppCompatActivity.RESULT_OK) {
			if (requestCode == Constants.ADD_TASK) {
				String result = data.getStringExtra("task");
				if (result != null) {
					Task task = Task.fromJSON(result);
					task = database.addTask(task);
					tasks.add(task);
//   					mAdapter.add(task);
				}
			} else if (requestCode == Constants.ADD_TASK) {
				//TODO : Modify old task
//				String result = data.getStringExtra("task");
//				if (result != null) {
//					Task task = Task.fromJSON(result);
//					task = database.addTask(task);
//					taskList.addTask(task);
//				}
			}
		}
	}

	private List<Tab> createFragments() {
		List<Tab> fList = new ArrayList<>();
		Fragment listAllFragment = ListAllFragment.newInstance();
		Fragment tmpFragment = ListAllFragment.newInstance();


		ArrayList<String> list = new ArrayList<>();
		for (Task task : tasks) {
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

	public boolean editTask(View view) {
//		int pos = taskList.getList().getPositionForView(view);
//		Task task = mAdapter.getItem(pos);
//		if (task != null) {
//			Intent createTask = new Intent(this, CreateTaskActivity.class);
//			createTask.putExtra("task", task.toJSON());
//			startActivityForResult(createTask, Constants.EDIT_TASK);
//		}
		return true;
	}

	public boolean createTask() {
		Intent createTask = new Intent(this, CreateTaskActivity.class);
		startActivityForResult(createTask, Constants.ADD_TASK);
		return true;
	}

	public void deleteTask(View view) {
		Log.d("Lol", "Mdr");
//		int pos = taskList.getList().getPositionForView(view);
//		Task task = mAdapter.getItem(pos);
//		if (task != null) {
//			database.deleteTask(task);
//			mAdapter.remove(task);
//			mAdapter.notifyDataSetChanged();
//		}
	}
}