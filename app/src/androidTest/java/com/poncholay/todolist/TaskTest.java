package com.poncholay.todolist;

import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.poncholay.todolist.model.task.Task;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class TaskTest {

	@Test
	public void JsonConversion() throws Exception {
		Task task;
		List<String> titles = new ArrayList<>();
		List<String> contents = new ArrayList<>();
		List<Date> dates = new ArrayList<>();
		List<Boolean> statusList = new ArrayList<>();
		int i = 0;

		titles.add("");
		titles.add("]¶̡đ€ĸßðł");
		titles.add("Test");
		titles.add("           ");
		titles.add(null);

		contents.add("");
		contents.add("æ«đ«€@ð€đ«{ŧŧŧŋ€¶đ");
		contents.add("Test");
		contents.add("          ");
		contents.add(null);

		dates.add(new Date());
		dates.add(new GregorianCalendar(2014, Calendar.FEBRUARY, 11).getTime());
		dates.add(new GregorianCalendar(1234353321, Calendar.JULY, 35).getTime());
		dates.add(new GregorianCalendar(10, Calendar.DECEMBER, 0).getTime());
		dates.add(null);

		statusList.add(Boolean.TRUE);
		statusList.add(Boolean.FALSE);
		statusList.add(null);

		task = new Task();
		i = compare(task, i);

		for (String title : titles) {
			task.setTitle(title);
			i = compare(task, i);
			for (String content : contents) {
				task.setContent(content);
				i = compare(task, i);
				for (Date date : dates) {
					task.setDate(date);
					i = compare(task, i);
					for (Boolean status : statusList) {
						task.setDone(status);
						i = compare(task, i);
					}
					task.setDate(date);
					i = compare(task, i);
				}
				task.setContent(content);
				i = compare(task, i);
			}
			task.setTitle(title);
			i = compare(task, i);
		}
	}

	private Integer compare(Task task, Integer i) throws Exception {
		String json = task.toJSON();
		Task other = Task.fromJSON(json);

		if (BuildConfig.DEBUG) {
			Log.d("Test", "Test n°" + ++i);
			Log.d("Test", json);
			Log.d("Test", task.getId() + " " + task.getTitle() + " " + task.getContent() + " " + task.getDate() + " " + task.getDone());
			Log.d("Test", other.getId() + " " + other.getTitle() + " " + other.getContent() + " " + other.getDate() + " " + other.getDone());
			Log.d("Test", "");
		}

		if (!task.compare(other)) {
			throw new Exception("Tasks do not match");
		}
		return i;
	}
}