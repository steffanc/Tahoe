package com.lake.tahoe.apps;

import android.app.Application;
import com.lake.tahoe.R;
import com.lake.tahoe.models.User;
import com.parse.Parse;
import com.parse.ParseUser;

/**
 * Created on 10/21/13.
 */
public class TahoeApp extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		setupParseSdk();
	}

	public void setupParseSdk() {
		ParseUser.registerSubclass(User.class);
		Parse.initialize(this, getString(R.string.parse_client_id), getString(R.string.parse_key));
	}

}
