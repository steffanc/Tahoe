package com.lake.tahoe.models;

import android.content.Context;

import com.orm.SugarRecord;

/**
 * Created on 10/21/13.
 */

public class User extends SugarRecord<User> {
	String name;
	String email;

	Long latitude;
	Long longitude;

	int connectionId;

	public static enum Mode {
		MODE_CLIENT, MODE_VENDOR
	}

	Mode mode;

	public User(Context ctx) {
		super(ctx);
	}

	public User(Context ctx, String name,
	            String email,
	            Mode mode) {
		super(ctx);

		this.name = name;
		this.email = email;
		this.mode = mode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Mode getMode() {
		return mode;
	}

	public void setMode(Mode mode) {
		this.mode = mode;
	}

	public Long getLatitude() {
		return latitude;
	}

	public void setLatitude(Long latitude) {
		this.latitude = latitude;
	}

	public Long getLongitude() {
		return longitude;
	}

	public void setLongitude(Long longitude) {
		this.longitude = longitude;
	}

	public int getConnectionId() {
		return connectionId;
	}

	public void setConnectionId(int connectionId) {
		this.connectionId = connectionId;
	}
}
