package com.lake.tahoe.models;

import com.google.android.gms.maps.model.LatLng;
import com.lake.tahoe.callbacks.ModelCallback;
import com.lake.tahoe.callbacks.ModelFindCallback;
import com.lake.tahoe.callbacks.ModelGetCallback;
import com.parse.*;

/**
 * Created on 10/21/13.
 */

@ParseClassName("_User")
public class User extends ParseUser {

	public static enum Type {

		/**
		 * Buys services or things from Vendors
		 */
		CLIENT,

		/**
		 * Sells services or things to Clients
		 */
		VENDOR

	}

	public static final int MAX_DISTANCE = 20;
	public static final int MAX_ITEMS = 50;

	public User() {
		super();
	}

	public Type getType() {
		return Type.valueOf(getString("type"));
	}

	public void setType(Type type) {
		put("type", type.toString());
	}

	public ParseGeoPoint getLocation() {
		return getParseGeoPoint("location");
	}

	public LatLng getGoogleMapsLocation() {
		ParseGeoPoint pGP = getLocation();
		LatLng latlng = null;
		if (pGP != null) {
			latlng = new LatLng(pGP.getLatitude(), pGP.getLongitude());
		}
		return latlng;
	}

	public void setLocation(Double latitude, Double longitude) {
		put("location", new ParseGeoPoint(latitude, longitude));
	}

	public String getFacebookId() {
		return getString("facebookId");
	}

	public void setFacebookId(String facebookId) {
		put("facebookId", facebookId);
	}

	public String getEmail() {
		return getString("email");
	}

	public void setEmail(String email) {
		put("email", email);
	}

	public String getName() {
		return getString("name");
	}

	public void setName(String name) {
		put("name", name);
	}

	public static User getCurrentUser() {
		return (User) ParseUser.getCurrentUser();
	}

	public static ParseQuery<User> getUserQuery() {
		return ParseQuery.getQuery(User.class);
	}

	public void findNearbyUsers(User.Type userType, ModelCallback<User> callback) {
		ParseGeoPoint userLocation = this.getLocation();
		ParseQuery<User> query = getUserQuery();
		query.whereContains("type", userType.toString());
		query.whereExists("name");  // filter out blank names
		query.whereNotEqualTo("objectId", this.getObjectId());  // exclude self
		query.whereWithinMiles("location", userLocation, MAX_DISTANCE);
		query.setLimit(MAX_ITEMS);
		query.findInBackground(new ModelFindCallback<User>(callback));
	}

	public void getUnfinishedRequest(ModelCallback<Request> callback) {
		String type = getType().toString().toLowerCase();
		ParseQuery<Request> query = Request.getRequestQuery();
		query.whereNotEqualTo("state", Request.State.FULFILLED.toString());
		query.whereNotEqualTo("state", Request.State.CANCELLED.toString());
		query.whereEqualTo(type, this);
		query.getFirstInBackground(new ModelGetCallback<Request>(callback));
	}

}
