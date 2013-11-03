package com.lake.tahoe.models;

import com.google.android.gms.maps.model.LatLng;
import com.parse.ParseClassName;
import com.parse.ParseGeoPoint;
import com.parse.ParseUser;

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

	public static User getCurrentUser() {
		return (User) ParseUser.getCurrentUser();
	}

}
