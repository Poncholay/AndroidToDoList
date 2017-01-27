package com.poncholay.todolist.Activities.List;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.github.ybq.parallaxviewpager.Mode;
import com.github.ybq.parallaxviewpager.ParallaxViewPager;
import com.nightonke.boommenu.BoomButtons.ButtonPlaceEnum;
import com.nightonke.boommenu.BoomButtons.HamButton;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.ButtonEnum;
import com.nightonke.boommenu.Piece.PiecePlaceEnum;
import com.nightonke.boommenu.Util;
import com.poncholay.todolist.Activities.CreateTask.CreateTaskActivity;
import com.poncholay.todolist.Activities.List.Fragments.TaskListFragment;
import com.poncholay.todolist.Activities.List.Fragments.TaskListFragmentAll;
import com.poncholay.todolist.Activities.List.Fragments.TaskListFragmentDone;
import com.poncholay.todolist.ColorUtils;
import com.poncholay.todolist.Constants;
import com.poncholay.todolist.R;
import com.poncholay.todolist.controller.db.DatabaseActions;
import com.poncholay.todolist.controller.db.DatabaseHelper;
import com.poncholay.todolist.controller.tab.TabsAdapter;
import com.poncholay.todolist.model.tab.Tab;
import com.poncholay.todolist.model.task.Task;

import java.util.ArrayList;
import java.util.List;

//TODO: Calendar view, Vocal recognition, Push notification, Tags

public class ListActivity extends AppCompatActivity {

	private DatabaseActions mDatabase;
	private List<Task> mTasks;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_list);

		mDatabase = new DatabaseActions(new DatabaseHelper(this));
		mTasks = retrieveTasks(savedInstanceState);

		super.onCreate(savedInstanceState);

		List<Tab> mTabs = createFragments(savedInstanceState, getPreferences(Context.MODE_PRIVATE).getInt("sort", 0));
		ParallaxViewPager mParallaxViewPager = (ParallaxViewPager) findViewById(R.id.viewpager);
		TabsAdapter tabsAdapter = new TabsAdapter(getSupportFragmentManager(), mTabs);
		mParallaxViewPager.setAdapter(tabsAdapter);
		mParallaxViewPager.setOffscreenPageLimit(1);
		mParallaxViewPager.setMode(Mode.LEFT_OVERLAY);

		createBoomMenu();
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

	private List<Tab> createFragments(Bundle bundle, int index) {
		List<Tab> fList = new ArrayList<>();
		List<Fragment> fragments = null;
		if (bundle != null) {
			fragments = getSupportFragmentManager().getFragments();
		}
		fList.add(new Tab("All", fragments != null && fragments.size() > 0 ? (TaskListFragment) fragments.get(0) : TaskListFragmentAll.newInstance()));
		fList.add(new Tab("Done", fragments != null && fragments.size() > 1 ? (TaskListFragment) fragments.get(1) : TaskListFragmentDone.newInstance()));
		fList.add(new Tab("Calendar", fragments != null && fragments.size() > 2 ? (TaskListFragment) fragments.get(2) : TaskListFragmentAll.newInstance()));

		Bundle args = new Bundle();
		args.putInt("sort", index);
		if (fragments == null || fragments.size() <= 0) {
			fList.get(0).getFragment().setArguments(args);
		}
		if (fragments == null || fragments.size() <= 2) {
			fList.get(2).getFragment().setArguments(args);
		}
		Bundle argsDone = (Bundle) args.clone();
		argsDone.putBoolean("done", true);
		if (fragments == null || fragments.size() <= 1) {
			fList.get(1).getFragment().setArguments(argsDone);
		}

		return fList;
	}

	private HamButton.Builder createHamButton(int color, String text, String subText, String drawable) {
		return new HamButton.Builder()
				.normalImageDrawable(TextDrawable.builder()
						.beginConfig()
						.withBorder(2)
						.endConfig()
						.buildRound(drawable, ColorUtils.darken(color)))
				.imagePadding(new Rect(8, 8, 8, 8))
				.normalText(text)
				.subNormalText(subText)
				.containsSubText(true)
				.shadowEffect(true)
				.shadowRadius(Util.dp2px(2))
				.rippleEffect(false)
				.normalColor(color)
				.highlightedColor(ColorUtils.darken(ColorUtils.darken(color)))
				.listener(new OnBMClickListener() {
					@Override
					public void onBoomButtonClick(int index) {
						SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
						SharedPreferences.Editor editor = sharedPref.edit();
						editor.putInt("sort", index);
						editor.apply();
						sortLists(index);
					}
				});
	}

	private void createBoomMenu() {
		BoomMenuButton bmb = (BoomMenuButton) findViewById(R.id.bmb);
		bmb.setButtonEnum(ButtonEnum.Ham);
		bmb.setPiecePlaceEnum(PiecePlaceEnum.HAM_4);
		bmb.setButtonPlaceEnum(ButtonPlaceEnum.HAM_4);

		ColorGenerator generator = ColorGenerator.MATERIAL;
		List<Integer> colors = new ArrayList<>();
		for (int i = 0; i < 4; i ++) {
			colors.add(generator.getColor(i + i));
		}

		bmb.addBuilder(createHamButton(ColorUtils.darken(colors.get(0)), "Sort by date", "Closest tasks first", "Jan"));
		bmb.addBuilder(createHamButton(ColorUtils.darken(colors.get(1)), "Sort by date", "Farthest tasks first", "Dec"));
		bmb.addBuilder(createHamButton(ColorUtils.darken(colors.get(2)), "Sort alphabetically", "A → Z", "A"));
		bmb.addBuilder(createHamButton(ColorUtils.darken(colors.get(3)), "Sort alphabetically", "Z → A", "Z"));
	}

	private void sortLists(int index) {
		if (getSupportFragmentManager() != null && getSupportFragmentManager().getFragments() != null) {
			for (Fragment fragment : getSupportFragmentManager().getFragments()) {
				TaskListFragment taskListFragment = (TaskListFragment) fragment;
				taskListFragment.sortTasks(index);
			}
		}
	}

	private void createTask(Task task) {
		task = mDatabase.addTask(task);
		mTasks.add(task);
		if (getSupportFragmentManager() != null && getSupportFragmentManager().getFragments() != null) {
			for (Fragment fragment : getSupportFragmentManager().getFragments()) {
				TaskListFragment taskListFragment = (TaskListFragment) fragment;
				taskListFragment.createTask(task);
			}
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
		if (getSupportFragmentManager() != null && getSupportFragmentManager().getFragments() != null) {
			for (Fragment fragment : getSupportFragmentManager().getFragments()) {
				TaskListFragment taskListFragment = (TaskListFragment) fragment;
				taskListFragment.editTask(task);
			}
		}
	}

	private void deleteTask(Task task) {
		mDatabase.deleteTask(task);
		mTasks.remove(task);
		if (getSupportFragmentManager() != null && getSupportFragmentManager().getFragments() != null) {
			for (Fragment fragment : getSupportFragmentManager().getFragments()) {
				TaskListFragment taskListFragment = (TaskListFragment) fragment;
				taskListFragment.deleteTask(task);
			}
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