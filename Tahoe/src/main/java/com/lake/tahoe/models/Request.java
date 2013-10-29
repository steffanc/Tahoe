package com.lake.tahoe.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by steffan on 10/21/13.
 */
@ParseClassName("Request")
public class Request extends ParseObject {

	enum State {
		OPEN, ACTIVE, PENDING, FULFILLED, CANCELLED
	}

	State state;

	public Request() {
		super();
	}

	public User getClient() {
		return (User) getParseObject("client");
	}

	public void setClient(User client) {
		put("client", client);
	}

	public User getVendor() {
		return (User) getParseObject("vendor");
	}

	public void setVendor(User vendor) {
		put("vendor", vendor);
	}

	public String getTitle() {
		return getString("title");
	}

	public void setTitle(String title) {
		put("title", title);
	}

	public String getDescription() {
		return getString("description");
	}

	public void setDescription(String description) {
		put("description", description);
	}

	public int getCents() {
		return getInt("cents");
	}

	public void setCents(Integer cents) {
		put("cents", cents);
	}

	public State getState() {
		return State.valueOf(getString("state"));
	}

	public void setState(State state) {
		put("state", state.toString());
	}

}
