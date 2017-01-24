package com.poncholay.todolist.controller.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.poncholay.todolist.DateUtils;
import com.poncholay.todolist.model.task.Task;

import java.text.ParseException;
import java.util.ArrayList;

/**
 * Created by wilmot_g on 11/01/17.
 */

public class DatabaseActions {

	private DatabaseHelper databaseHelper;

	public DatabaseActions(DatabaseHelper databaseHelper) {
		this.databaseHelper = databaseHelper;
	}

	public Task addTask(Task task) {
		SQLiteDatabase db = databaseHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(TaskContract.TaskEntry.COL_TASK_TITLE, task.getTitle());
		values.put(TaskContract.TaskEntry.COL_TASK_DONE, task.getDone());
		if (task.getContent() != null) {
			values.put(TaskContract.TaskEntry.COL_TASK_CONTENT, task.getContent());
		}
		if (task.getDate() != null) {
			values.put(TaskContract.TaskEntry.COL_TASK_DATE, DateUtils.toString(task.getDate()));
		}
		long id = db.insertWithOnConflict(TaskContract.TaskEntry.TABLE,
				null,
				values,
				SQLiteDatabase.CONFLICT_REPLACE);
		db.close();
		task.setId(id);
		return id == -1 ? null : task;
	}

	public void editTask(Task task) {
		SQLiteDatabase db = databaseHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(TaskContract.TaskEntry.COL_TASK_TITLE, task.getTitle());
		values.put(TaskContract.TaskEntry.COL_TASK_DONE, task.getDone());
		if (task.getContent() != null) {
			values.put(TaskContract.TaskEntry.COL_TASK_CONTENT, task.getContent());
		}
		if (task.getDate() != null) {
			values.put(TaskContract.TaskEntry.COL_TASK_DATE, DateUtils.toString(task.getDate()));
		}
		db.updateWithOnConflict(
				TaskContract.TaskEntry.TABLE,
				values,
				TaskContract.TaskEntry._ID + " = ?",
				new String[]{task.getId().toString()},
				SQLiteDatabase.CONFLICT_REPLACE
		);
		db.close();
	}

	public void deleteTask(Task task) {
		SQLiteDatabase db = databaseHelper.getWritableDatabase();
		db.delete(
				TaskContract.TaskEntry.TABLE,
				TaskContract.TaskEntry._ID + " = ?",
				new String[]{task.getId().toString()}
		);
		db.close();
	}

	public ArrayList<Task> getTasks() {
		ArrayList<Task> taskList = new ArrayList<>();
		SQLiteDatabase db = databaseHelper.getReadableDatabase();
		Cursor cursor = db.query(TaskContract.TaskEntry.TABLE,
				new String[] {
						TaskContract.TaskEntry._ID,
						TaskContract.TaskEntry.COL_TASK_DONE,
						TaskContract.TaskEntry.COL_TASK_TITLE,
						TaskContract.TaskEntry.COL_TASK_CONTENT,
						TaskContract.TaskEntry.COL_TASK_DATE
				}, null, null, null, null, null);
		while (cursor.moveToNext()) {
			Task task = new Task();
			int idx = cursor.getColumnIndex(TaskContract.TaskEntry._ID);
			if (idx != - 1) {
				task.setId(cursor.getLong(idx));
			} else {
				continue;
			}
			idx = cursor.getColumnIndex(TaskContract.TaskEntry.COL_TASK_DONE);
			if (idx != - 1) {
				task.setDone(cursor.getInt(idx) > 0);
			} else {
				task.setDone(false);
			}
			idx = cursor.getColumnIndex(TaskContract.TaskEntry.COL_TASK_TITLE);
			if (idx != - 1) {
				task.setTitle(cursor.getString(idx));
			} else {
				continue;
			}
			idx = cursor.getColumnIndex(TaskContract.TaskEntry.COL_TASK_CONTENT);
			if (idx != - 1) {
				task.setContent(cursor.getString(idx));
			}
			idx = cursor.getColumnIndex(TaskContract.TaskEntry.COL_TASK_DATE);
			if (idx != - 1) {
				try {
					task.setDate(DateUtils.toDate(cursor.getString(idx)));
				} catch (ParseException ignored) {}
			}
			taskList.add(task);
		}
		cursor.close();
		db.close();
		return taskList;
	}
}
