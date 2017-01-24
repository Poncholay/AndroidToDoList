package com.poncholay.todolist.controller.db;

import android.provider.BaseColumns;

/**
 * Created by wilmot_g on 11/01/17.
 */

class TaskContract {
	static final String DB_NAME = "com.poncholay.todolist.controller.db";
	static final int DB_VERSION = 1;

	class TaskEntry implements BaseColumns {
		static final String TABLE = "tasks";

		static final String COL_TASK_TITLE = "title";
		static final String COL_TASK_CONTENT = "content";
		static final String COL_TASK_DATE = "date";
		static final String COL_TASK_DONE = "done";
	}
}
