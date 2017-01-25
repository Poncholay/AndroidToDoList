package com.poncholay.todolist.Activities.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.ybq.parallaxviewpager.Mode;
import com.github.ybq.parallaxviewpager.ParallaxViewPager;
import com.poncholay.todolist.Activities.CreateTask.CreateTaskActivity;
import com.poncholay.todolist.Activities.List.Fragments.TaskListFragment;
import com.poncholay.todolist.Activities.List.Fragments.TaskListFragmentAll;
import com.poncholay.todolist.Activities.List.Fragments.TaskListFragmentDone;
import com.poncholay.todolist.Constants;
import com.poncholay.todolist.R;
import com.poncholay.todolist.controller.db.DatabaseActions;
import com.poncholay.todolist.controller.db.DatabaseHelper;
import com.poncholay.todolist.controller.tab.TabsAdapter;
import com.poncholay.todolist.model.tab.Tab;
import com.poncholay.todolist.model.task.Task;

import java.util.ArrayList;
import java.util.List;

//TODO: Sort, Categorie, Calendar view, Vocal recognition, Push notification, app preferences

public class ListActivity extends AppCompatActivity {

	private DatabaseActions mDatabase;
	private List<Task> mTasks;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_list);

		mDatabase = new DatabaseActions(new DatabaseHelper(this));
		mTasks = retrieveTasks(savedInstanceState);

		super.onCreate(savedInstanceState);

		List<Tab> mTabs = createFragments(savedInstanceState);
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

	@Override
	public void onSaveInstanceState(Bundle outState) {
		ArrayList<String> list = new ArrayList<>();
		for (Task task : mTasks) {
			list.add(task.toJSON());
		}
		outState.putStringArrayList("tasks", list);
		super.onSaveInstanceState(outState);
	}

	private List<Task> retrieveTasks(Bundle bundle) {
		if (bundle != null) {
			List<String> JSONtasks = bundle.getStringArrayList("tasks");
			List<Task> tasks = new ArrayList<>();
			if (JSONtasks != null) {
				for (String json : JSONtasks) {
					Task task = Task.fromJSON(json);
					tasks.add(task);
				}
			}
			return tasks;
		} else {
			return mDatabase.getTasks();
		}
	}

	private List<Tab> createFragments(Bundle bundle) {
		List<Tab> fList = new ArrayList<>();
		if (bundle == null) {
			TaskListFragment listAllFragment = TaskListFragmentAll.newInstance();
			TaskListFragment listDoneFragment = TaskListFragmentDone.newInstance();
			TaskListFragment tmpFragment = TaskListFragmentAll.newInstance();

			fList.add(new Tab("All", listAllFragment));
			fList.add(new Tab("Done", listDoneFragment));
			fList.add(new Tab("By Month", tmpFragment));

			Bundle args = new Bundle();
			args.putBoolean("done", true);
			listDoneFragment.setArguments(args);
		} else {
			List<Fragment> fragments = getSupportFragmentManager().getFragments();
			fList.add(new Tab("All", fragments.size() > 0 ? (TaskListFragment) fragments.get(0) : TaskListFragmentAll.newInstance()));
			fList.add(new Tab("Done", fragments.size() > 1 ? (TaskListFragment) fragments.get(1) : TaskListFragmentDone.newInstance()));
			fList.add(new Tab("Calendar", fragments.size() > 2 ? (TaskListFragment) fragments.get(2) : TaskListFragmentAll.newInstance()));
		}
		return fList;
	}

	private void createTask(Task task) {
		task = mDatabase.addTask(task);
		mTasks.add(task);
		for (Fragment fragment : getSupportFragmentManager().getFragments()) {
			TaskListFragment taskListFragment = (TaskListFragment) fragment;
			taskListFragment.createTask(task);
		}
	}

	private void flagTaskDone(Task task) {
		task.setDone(!task.getDone());
		editTask(task);
	}

	private void editTask(Task task) {
		mDatabase.editTask(task);
		mTasks.remove(task);
		mTasks.add(task);
		for (Fragment fragment : getSupportFragmentManager().getFragments()) {
			TaskListFragment taskListFragment = (TaskListFragment) fragment;
			taskListFragment.editTask(task);
		}

	}

	private void deleteTask(Task task) {
		mDatabase.deleteTask(task);
		mTasks.remove(task);
		for (Fragment fragment : getSupportFragmentManager().getFragments()) {
			TaskListFragment taskListFragment = (TaskListFragment) fragment;
			taskListFragment.deleteTask(task);
		}
	}

	public void showTaskMenu(final Task task) {
		if (task != null) {
			MaterialDialog.Builder materialDialog = new MaterialDialog.Builder(this);

			String title = task.getTitle();
			if (title.length() >= 25 + 3) {
				title = title.substring(0, 25) + "...";
			}
			materialDialog.title(title);

			String content = task.getContent();
			content = content == null ? "No description" : content;
			if (content.length() >= 30 + 3) {
				content = content.substring(0, 40) + "...";
			}
			materialDialog.content(content);

			materialDialog.neutralText("Cancel");
			materialDialog.positiveText(task.getDone() ? "Uncomplete" : "Complete").onPositive(new MaterialDialog.SingleButtonCallback() {
				@Override
				public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
					flagTaskDone(task);
				}
			});
			materialDialog.negativeText("Delete").onNegative(new MaterialDialog.SingleButtonCallback() {
				@Override
				public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
					deleteTask(task);
				}
			});
			materialDialog.build().show();
		}
	}

	private void callCreateTaskActivity() {
		Intent createTask = new Intent(this, CreateTaskActivity.class);
		startActivityForResult(createTask, Constants.ADD_TASK);
	}

	public void callEditTaskActivity(Task task) {
		if (task != null) {
			Intent callCreateTaskActivity = new Intent(this, CreateTaskActivity.class);
			callCreateTaskActivity.putExtra("task", task.toJSON());
			startActivityForResult(callCreateTaskActivity, Constants.EDIT_TASK);
		}
	}

	public List<Task> getTasks() {
		return mTasks;
	}
}