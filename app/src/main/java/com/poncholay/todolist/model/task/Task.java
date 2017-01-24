package com.poncholay.todolist.model.task;

import android.util.Log;

import com.poncholay.todolist.DateUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Date;
import java.util.Objects;

/**
 * Created by wilmot_g on 13/01/17.
 */

public class Task implements Serializable {
	private Long id;
	private String title;
	private String content;
	private Date date;
	private Boolean done;

	public Task() {
		this(null, null, null);
	}

	public Task(String title) {
		this(title, null);
	}

	public Task(String title, String content) {
		this(title, content, null);
	}

	public Task(String title, String content, Date date) {
		this.title = title;
		this.content = content;
		this.date = date;
		this.done = false;
	}

	@Override
	public boolean equals(Object task) {
		if (task == null || !Task.class.isAssignableFrom(task.getClass())) {
			return false;
		}
		final Task other = (Task) task;
		Log.d("Test", "comparing " + other.getId() + " and " + this.id);
		return Objects.equals(other.getId(), this.id);
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		if (title != null) {
			this.title = title.substring(0, 1).toUpperCase() + title.substring(1);
		}
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		if (content != null) {
			this.content = content.substring(0, 1).toUpperCase() + content.substring(1);
		}
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Boolean getDone() {
		return done;
	}

	public void setDone(Boolean done) {
		this.done = done;
	}

	public String toJSON() {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("Id", id == null ? null : this.id.toString());
			jsonObject.put("Done", done);
			jsonObject.put("Title", this.title);
			jsonObject.put("Content", this.content);
			jsonObject.put("Date", DateUtils.toString(this.date));
		} catch (JSONException e) {
			Log.d("Task", "Error while serializing to JSON");
			jsonObject = new JSONObject();
		}
		return jsonObject.toString();
	}

	public static Task fromJSON(String json) {
		Task task = new Task();
		try {
			JSONObject jsonObject = new JSONObject(json);
			if (jsonObject.has("Id")) {
				task.setId(Long.parseLong(jsonObject.getString("Id")));
			}
			task.setDone(jsonObject.getBoolean("Done"));
			task.setTitle(jsonObject.getString("Title"));
			if (jsonObject.has("Content")) {
				task.setContent(jsonObject.getString("Content"));
			}
			try {
				if (jsonObject.has("Date")) {
					task.setDate(DateUtils.toDate(jsonObject.getString("Date")));
				}
			} catch (ParseException ignored) {
				Log.d("Task", "Error while parsing Date");
			}
		} catch (JSONException e) {
			Log.d("Task", "Error while unserializing from JSON");
		}
		return task;
	}
}
