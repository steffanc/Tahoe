package com.lake.tahoe.activities;

import android.app.Activity;
import android.content.Intent;
import com.lake.tahoe.models.User;

public class DelegateActivity extends Activity {

	@Override
	protected void onResume() {
		super.onResume();

		// check if user is logged in
		User currentUser = User.getCurrentUser();
		if (currentUser == null) {
			startLoginActivity();
			return;
		}

		/* TODO: Check if the user has a Request with state = OPEN|ACTIVE|PENDING
		    If so, both vendors and clients should be delegated to the correct Activity.
		    This will prevent clients from exiting the app to avoid payment
		*/

		// if we get here, user does not have a Request

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
