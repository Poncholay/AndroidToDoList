package com.poncholay.todolist.controller.task;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.poncholay.todolist.DateUtils;
import com.poncholay.todolist.model.task.Task;
import com.poncholay.todolist.R;

import java.util.ArrayList;

/**
 * Created by wilmot_g on 13/01/17.
 */

public class TasksAdapter extends ArrayAdapter<Task> {
	public TasksAdapter(Context context, ArrayList<Task> tasks) {
		super(context, 0, tasks);
	}

	@NonNull
	@Override
	public View getView(int position, View convertView, @NonNull ViewGroup parent) {
		Task task = getItem(position);

		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_task, parent, false);
		}

		if (task != null && task.getTitle() != null) {
			TextView title = (TextView) convertView.findViewById(R.id.task_title);
			title.setText(task.getTitle());
			TextView content = (TextView) convertView.findViewById(R.id.task_content);
			content.setText(task.getContent() == null ? "No description" : task.getContent());
			TextView date = (TextView) convertView.findViewById(R.id.task_date);
			date.setText(task.getDate() == null ? "Anytime" : DateUtils.toLiteString(task.getDate()));
		}

		return convertView;
	}
}