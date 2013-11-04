package com.lake.tahoe.apps;

import android.app.Application;
import android.os.Bundle;
import com.lake.tahoe.activities.DelegateActivity;
import com.lake.tahoe.models.Request;
import com.lake.tahoe.models.User;
import com.lake.tahoe.utils.ManifestReader;
import com.parse.*;

/**
 * Created on 10/21/13.
 */
public class TahoeApp extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		Bundle config = ManifestReader.getPackageMetaData(TahoeApp.this);
		setupParseSdk(config);
		setupFacebookSdk(config);
	}

	private void setupFacebookSdk(Bundle config) {
		ParseFacebookUtils.initialize(config.getString("com.facebook.sdk.ApplicationId"));
	}

	public void setupParseSdk(Bundle config) {
		String parseClientId = config.getString("com.parse.CLIENT_ID");
		String parseApiKey = config.getString("com.parse.API_KEY");
		ParseUser.registerSubclass(User.class);
		ParseObject.registerSubclass(Request.class);
		Parse.initialize(this, parseClientId, parseApiKey);
		PushService.setDefaultPushCallback(this, DelegateActivity.class);
		ParseInstallation.getCurrentInstallation().saveInBackground();
		ParseAnalytics.trackAppOpened(null);
	}

}
