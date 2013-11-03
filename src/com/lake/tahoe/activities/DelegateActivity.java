package com.lake.tahoe.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.lake.tahoe.models.User;
import com.parse.ParseAnalytics;
import com.parse.ParseFacebookUtils;

public class DelegateActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ParseAnalytics.trackAppOpened(getIntent());
	}

	@Override
	protected void onResume() {
		super.onResume();

		// check if user is logged in
		User currentUser = User.getCurrentUser();

		if (currentUser == null || !ParseFacebookUtils.isLinked(currentUser)) {
			startLoginActivity();
			return;
		}

		/* TODO: Check if the user has a Request with state = OPEN|ACTIVE|PENDING
		    If so, both vendors and clients should be delegated to the correct Activity.
		    This will prevent clients from exiting the app to avoid payment
		*/

		// TODO: leave this hardcoded for now; change it in future signup activity
		currentUser.setType(User.Type.VENDOR);

		if (currentUser.getType() == User.Type.VENDOR) {
			startRequestMapActivity();
		} else {
			startRequestCreateActivity();
		}

	}

	private void startRequestCreateActivity() {
		startActivity(new Intent(this, RequestCreateActivity.class));
	}

	private void startRequestMapActivity() {
		startActivity(new Intent(this, RequestMapActivity.class));
	}

	public void startLoginActivity() {
		startActivity(new Intent(this, LoginActivity.class));
	}

}
