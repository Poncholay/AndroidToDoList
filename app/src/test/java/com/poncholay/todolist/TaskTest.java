package com.poncholay.todolist;

import android.util.Log;

import com.poncholay.todolist.model.task.Task;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Log.class})
public class TaskTest {
	@Test
	public void JsonConvertion() throws Exception {
		Task task;
		List<String> titles = new ArrayList<>();
		List<String> contents = new ArrayList<>();
		List<Date> dates = new ArrayList<>();
		List<Boolean> statusList = new ArrayList<>();
		Integer i = 0;
		
		PowerMockito.mockStatic(Log.class);

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
		i = compareTasks(task, i);

		for (String title : titles) {
			task.setTitle(title);
			i = compareTasks(task, i);
			for (String content : contents) {
				task.setContent(content);
				i = compareTasks(task, i);
				for (Date date : dates) {
					task.setDate(date);
					i = compareTasks(task, i);
					for (Boolean status : statusList) {
						task.setDone(status);
						i = compareTasks(task, i);
					}
					task.setDate(date);
					i = compareTasks(task, i);
				}
				task.setContent(content);
				i = compareTasks(task, i);
			}
			task.setTitle(title);
			i = compareTasks(task, i);
		}
	}

	private Integer compareTasks(Task task, Integer i) throws Exception {
		String json = task.toJSON();
		Task other = Task.fromJSON(json);

		System.out.println("Test n°" + ++i);
		System.out.println(json);
		System.out.println(task.getId() + " " + task.getTitle() + " " + task.getContent() + " " + task.getDate() + " " + task.getDone());
		System.out.println(other.getId() + " " + other.getTitle() + " " + other.getContent() + " " + other.getDate() + " " + other.getDone());
		System.out.println("");

		if (!task.compare(other)) {
		   throw new Exception("Tasks do not match");
		}
		return i;
	}
}