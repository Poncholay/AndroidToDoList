package com.poncholay.todolist;

import android.content.Context;

import com.poncholay.todolist.controller.db.DatabaseActions;
import com.poncholay.todolist.controller.db.DatabaseHelper;
import com.poncholay.todolist.model.task.Task;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, packageName = "com.poncholay.todolist", sdk = 19)
public class DatabaseTest {

	@Test
	public void dataBaseTest() throws Exception {
		Context context = RuntimeEnvironment.application;
		DatabaseActions db = new DatabaseActions(new DatabaseHelper(context));

		List<Task> list = new ArrayList<>();
		list.add(null);
		list.add(new Task(""));
		list.add(new Task("Salut"));
		list.add(new Task("Salut", "Test"));
		list.add(new Task("Salut", "Test", new Date()));

		List<Task> listOther = new ArrayList<>();
		listOther.add(db.addTask(null));
		listOther.add(db.addTask(new Task("")));
		listOther.add(db.addTask(new Task("Salut")));
		listOther.add(db.addTask(new Task("Salut", "Test")));
		listOther.add(db.addTask(new Task("Salut", "Test", new Date())));


		compareTasks(list, listOther);

		for (int i = 0; i < list.size(); i++) {
			Task task = list.get(i);
			Task other = listOther.get(i);

			if (task != null) {
				task.setTitle("MyZboub");
			}
			if (other != null) {
				other.setTitle("MyZboub");
			}
			db.editTask(other);
		}

		listOther = db.getTasks();
		listOther.add(0, null);

		compareTasks(list, listOther);

		for (Task task : listOther) {
			db.deleteTask(task);
		}

		listOther = db.getTasks();
		if (listOther.size() > 0) {
			throw new Exception("List should be empty");
		}
	}

	private void compareTasks(List<Task> list, List<Task> listOther) throws Exception {
		for (int i = 0; i < list.size(); i++) {
			Task task = list.get(i);
			Task other = listOther.get(i);

			if (task != null && other != null) {
				task.setId(other.getId());
			}

			if (BuildConfig.DEBUG) {
				if (task != null) {
					System.out.println(task.getId() + " " + task.getTitle() + " " + task.getContent() + " " + task.getDate() + " " + task.getDone());
				} else {
					System.out.println("NULL");
				}
				if (other != null) {
					System.out.println(other.getId() + " " + other.getTitle() + " " + other.getContent() + " " + other.getDate() + " " + other.getDone());
				} else {
					System.out.println("NULL");
				}
			}

			if ((task == null && other != null) || (task != null && !task.compare(other))) {
				throw new Exception("Tasks do not match");
			}
		}
	}
}