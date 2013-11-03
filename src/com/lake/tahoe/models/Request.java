package com.lake.tahoe.models;

import com.google.android.gms.maps.model.LatLng;
import com.parse.ParseClassName;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;

import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

/**
 * Created by steffan on 10/21/13.
 */
@ParseClassName("Request")
public class Request extends ParseObject {

	enum State {

		/**
		 * Client creates a new Request
		 */
		OPEN,

		/**
		 * Vendor accepts Request
		 */
		ACTIVE,

		/**
		 * Vendor completes Request
		 */
		PENDING,

		/**
		 * Vendor receives Payment
		 */
		FULFILLED,

		/**
		 * Vendor cancels Payment
		 */
		CANCELLED

	}

	public Request() {
		super();
		setState(State.OPEN);
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

	public String getDisplayDollars() {
		NumberFormat numberFormat = NumberFormat.getCurrencyInstance(Locale.US);
		return numberFormat.format((double)getCents()/100);
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

	public ParseGeoPoint getLocation() {
		return getClient().getLocation();
	}

	public LatLng getGoogleMapsLocation() {
		return getClient().getGoogleMapsLocation();
	}

}
