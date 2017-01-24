package com.poncholay.todolist.controller.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by wilmot_g on 11/01/17.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

	public DatabaseHelper(Context context) {
		super(context, TaskContract.DB_NAME, null, TaskContract.DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String createTable = "CREATE TABLE " + TaskContract.TaskEntry.TABLE + " ( " +
				TaskContract.TaskEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
				TaskContract.TaskEntry.COL_TASK_DONE + " INTEGER, " +
				TaskContract.TaskEntry.COL_TASK_TITLE + " TEXT NOT NULL, " +
				TaskContract.TaskEntry.COL_TASK_CONTENT + " TEXT, " +
				TaskContract.TaskEntry.COL_TASK_DATE + " TEXT);";
		db.execSQL(createTable);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TaskContract.TaskEntry.TABLE);
		onCreate(db);
	}
}
