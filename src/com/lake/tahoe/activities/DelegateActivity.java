package com.lake.tahoe.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.lake.tahoe.callbacks.ModelCallback;
import com.lake.tahoe.models.Request;
import com.lake.tahoe.models.User;
import com.parse.ParseFacebookUtils;

public class DelegateActivity extends Activity implements ModelCallback<Request> {

	User currentUser;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onResume() {

		super.onResume();

		// check if user is logged in
		currentUser = User.getCurrentUser();

		// if not logged in, then redirect to the login activity
		if (currentUser == null || !ParseFacebookUtils.isLinked(currentUser)) {
			startLoginActivity();
			return;
		}

		// otherwise, see if the user has an unfinished request
		currentUser.getUnfinishedRequest(this);

	}

	@Override
	public void onModelFound(Request request) {
		if (request.getState().equals(Request.State.OPEN))
			startRequestOpenActivity();
		else if (request.getState().equals(Request.State.ACTIVE))
			startRequestActiveActivity();
		else if (request.getState().equals(Request.State.PENDING))
			startRequestPendingActivity();
	}

	@Override
	public void onModelError(Throwable t) {
		// no request found, drop into first view
		if (currentUser.getType() == User.Type.VENDOR)
			startRequestMapActivity();
		else
			startRequestCreateActivity();
	}

	private void startRequestPendingActivity() {
		//TODO Both clients and vendors can see a subclass of this activity
		//startActivity(new Intent(this, RequestPendingClientActivity.class));
		//startActivity(new Intent(this, RequestPendingVendorActivity.class));
	}

	private void startRequestOpenActivity() {
		//TODO Only clients can see this activity
		//startActivity(new Intent(this, RequestOpenActivity.class));
	}

	private void startRequestActiveActivity() {
		//TODO Both clients and vendors can see a subclass of this activity
		//startActivity(new Intent(this, RequestActiveClientActivity.class));
		//startActivity(new Intent(this, RequestActiveVendorActivity.class));
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
