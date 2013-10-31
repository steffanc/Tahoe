package com.lake.tahoe.apps;

import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import com.lake.tahoe.models.Request;
import com.lake.tahoe.models.User;
import com.parse.Parse;
import com.parse.ParseFacebookUtils;
import com.parse.ParseObject;
import com.parse.ParseUser;

/**
 * Created on 10/21/13.
 */
public class TahoeApp extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		Bundle config = getMetaData();
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
	}

	public Bundle getMetaData() {
		try {
			PackageManager manager = getPackageManager();
			if (manager == null) return null;
			String packageName = getPackageName();
			ApplicationInfo info = manager.getApplicationInfo(packageName, PackageManager.GET_META_DATA);
			return info == null ? null : info.metaData;
		} catch(PackageManager.NameNotFoundException ex) {
			return null;
		}
	}

}
