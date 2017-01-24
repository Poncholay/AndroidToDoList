package com.poncholay.todolist.Activities.CreateTask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.poncholay.todolist.R;
import com.poncholay.todolist.controller.task.BlankValidator;
import com.poncholay.todolist.model.task.Task;
import com.quemb.qmbform.FormManager;
import com.quemb.qmbform.OnFormRowClickListener;
import com.quemb.qmbform.descriptor.CellDescriptor;
import com.quemb.qmbform.descriptor.FormDescriptor;
import com.quemb.qmbform.descriptor.FormItemDescriptor;
import com.quemb.qmbform.descriptor.OnFormRowValueChangedListener;
import com.quemb.qmbform.descriptor.RowDescriptor;
import com.quemb.qmbform.descriptor.SectionDescriptor;
import com.quemb.qmbform.descriptor.Value;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

/**
 * Created by wilmot_g on 14/01/17.
 */

public class CreateTaskActivity extends AppCompatActivity implements OnFormRowValueChangedListener, OnFormRowClickListener {

	private HashMap<String, Value<?>> mChangesMap = new HashMap<>();
	private MenuItem mSaveMenuItem;
	private FormManager formManager;
	private Task task;
	private RowDescriptor titleRow;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_task);
		task = retrieveTask(savedInstanceState);
		createForm();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.form_menu, menu);
		mSaveMenuItem = menu.findItem(R.id.action_save);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		updateSaveItemVisibility();
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_save:
				save();
				break;
			case R.id.action_back:
				onBackPressed();
				break;
			default:
				break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		Intent returnIntent = new Intent();
		setResult(Activity.RESULT_CANCELED, returnIntent);
		finish();
	}

	@Override
	public void onFormRowClick(FormItemDescriptor itemDescriptor) {

	}

	@Override
	public void onValueChanged(RowDescriptor rowDescriptor, Value<?> oldValue, Value<?> newValue) {
		if (Objects.equals(rowDescriptor.getTag(), "date") || Objects.equals(rowDescriptor.getTag(), "time")) {
			if (formManager != null) {
				formManager.updateRows();
			}
		}
		mChangesMap.put(rowDescriptor.getTag(), newValue);
		updateSaveItemVisibility();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		task = recreateTask(task);
		try {
			outState.putString("task", task.toJSON());
		} catch (Exception ignored) {}
		super.onSaveInstanceState(outState);
	}

	public void save() {
		if (titleRow.isValid()) {
			Intent returnIntent = new Intent();
			if (mChangesMap != null && mChangesMap.size() != 0) {
				task = recreateTask(task);
				returnIntent.putExtra("task", task.toJSON());
				setResult(Activity.RESULT_OK, returnIntent);
			} else {
				setResult(Activity.RESULT_CANCELED, returnIntent);
			}
			finish();
		} else {
			Toast error = Toast.makeText(this, "Title cannot be empty", Toast.LENGTH_SHORT);
			error.show();
		}
	}

	private void createForm() {
		ListView mListView = (ListView) findViewById(R.id.form);

		HashMap<String, Object> cellConfig = new HashMap<>(2);
		cellConfig.put(CellDescriptor.COLOR_LABEL, 0x80000000);
		cellConfig.put(CellDescriptor.COLOR_VALUE, 0xFF000000);

		FormDescriptor descriptor = FormDescriptor.newInstance();
		descriptor.setCellConfig(cellConfig);

		SectionDescriptor sectionDescriptor = SectionDescriptor.newInstance("general", "General");
		descriptor.addSection(sectionDescriptor);

		titleRow = RowDescriptor.newInstance("title", RowDescriptor.FormRowDescriptorTypeText, "Title", new Value<>(task.getTitle()));
		titleRow.addValidator(new BlankValidator());
		sectionDescriptor.addRow(titleRow);

		final RowDescriptor contentRow = RowDescriptor.newInstance("content", RowDescriptor.FormRowDescriptorTypeTextView, "Content", new Value<>(task.getContent()));
		sectionDescriptor.addRow(contentRow);

		sectionDescriptor = SectionDescriptor.newInstance("date", "Date");
		descriptor.addSection(sectionDescriptor);

		final RowDescriptor dateRow = RowDescriptor.newInstance("date", RowDescriptor.FormRowDescriptorTypeDate, "Date", new Value<>(task.getDate()));
		sectionDescriptor.addRow(dateRow);
		final RowDescriptor timeRow = RowDescriptor.newInstance("time", RowDescriptor.FormRowDescriptorTypeTime, "Time", new Value<>(task.getDate()));
		sectionDescriptor.addRow(timeRow);

		//Only for design
		sectionDescriptor = SectionDescriptor.newInstance("", "");
		descriptor.addSection(sectionDescriptor);

		formManager = new FormManager();
		formManager.setup(descriptor, mListView, this);
		formManager.setOnFormRowClickListener(this);
		formManager.setOnFormRowValueChangedListener(this);
	}

	private Task retrieveTask(Bundle savedInstanceState) {
		Task task = null;
		Bundle extras = getIntent().getExtras();
		if (extras == null) {
			extras = savedInstanceState;
		}
		if (extras != null) {
			String value = extras.getString("task");
			if (value != null) {
				task = Task.fromJSON(value);
			}
		}
		return task == null ? new Task() : task;
	}

	private Task recreateTask(Task task) {
		Date time = null;
		for (HashMap.Entry<String, Value<?>> entry : mChangesMap.entrySet()) {
			switch (entry.getKey()) {
				case "title":
					task.setTitle((String) entry.getValue().getValue());
					break;
				case "content":
					task.setContent((String) entry.getValue().getValue());
					break;
				case "date":
					task.setDate((Date) entry.getValue().getValue());
					break;
				case "time":
					time = (Date) entry.getValue().getValue();
					break;
			}
		}
		task.setDate(mergeDates(task.getDate(), time));
		return task;
	}

	private Date mergeDates(Date date, Date time) {
		if (time == null && date == null) {
			return null;
		}
		if (time == null) {
			Calendar dateCalendar = Calendar.getInstance();
			dateCalendar.setTime(date);
			dateCalendar.set(Calendar.HOUR_OF_DAY, 0);
			dateCalendar.set(Calendar.MINUTE, 0);
			dateCalendar.set(Calendar.SECOND, 0);
			return dateCalendar.getTime();
		}
		if (date == null)
			date = new Date();
		Calendar dateCalendar = Calendar.getInstance();
		Calendar timeCalendar = Calendar.getInstance();
		dateCalendar.setTime(date);
		timeCalendar.setTime(time);
		dateCalendar.set(Calendar.HOUR_OF_DAY, timeCalendar.get(Calendar.HOUR_OF_DAY));
		dateCalendar.set(Calendar.MINUTE, timeCalendar.get(Calendar.MINUTE));
		dateCalendar.set(Calendar.SECOND, 0);
		return dateCalendar.getTime();
	}

	private void updateSaveItemVisibility() {
	 	if (mSaveMenuItem != null) {
			mSaveMenuItem.setVisible(mChangesMap.size() > 0);
		}
	}
}
