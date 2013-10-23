package com.lake.tahoe.models;

import android.content.Context;

import com.orm.SugarRecord;

/**
 * Created by steffan on 10/21/13.
 */

public class Request extends SugarRecord<Request> {
    User client;
    User vendor;
    String title;
    String description;
	int bid;

	enum State {
		OPEN, ACTIVE, PENDING, FULFILLED, CANCELLED
	}

	State state;

	public Request(Context ctx) {
		super(ctx);
	}

	public Request(Context ctx,
	               User client,
	               String title,
	               String description,
	               int bid) {
		super(ctx);

		this.client = client;
		this.title = title;
		this.description = description;
		this.bid = bid;
		this.state = State.OPEN;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public User getClient() {
		return client;
	}

	public void setClient(User client) {
		this.client = client;
	}

	public User getVendor() {
		return vendor;
	}

	public void setVendor(User vendor) {
		this.vendor = vendor;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getBid() {
		return bid;
	}

	public void setBid(int bid) {
		this.bid = bid;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}
}
