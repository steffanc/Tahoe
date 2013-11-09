package com.lake.tahoe.activities;

import android.os.Bundle;
import com.lake.tahoe.R;
import com.lake.tahoe.callbacks.ModelCallback;
import com.lake.tahoe.models.Request;
import com.lake.tahoe.models.User;
import com.lake.tahoe.utils.ActivityUtil;
import com.parse.ParseFacebookUtils;

public class DelegateActivity extends TahoeActivity implements ModelCallback<Request> {

	User currentUser;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActivityUtil.transitionFade(this);
	}

	@Override
	protected void onResume() {

		super.onResume();
		toggleBlocker(true);

		// check if user is logged in
		currentUser = User.getCurrentUser();

		// if not logged in, then redirect to the login activity
		if (currentUser == null || !ParseFacebookUtils.isLinked(currentUser)) {
			ActivityUtil.startLoginActivity(this);
			ActivityUtil.transitionFade(this);
			return;
		}

		// otherwise, see if the user has an unfinished request
		currentUser.getUnfinishedRequest(this);

	}

	@Override
	public void onModelFound(Request request) {
		if (request.getState().equals(Request.State.OPEN) && currentUser.getType().equals(User.Type.CLIENT))
			// Only clients can see this activity
			ActivityUtil.startRequestOpenActivity(this);
		else if (request.getState().equals(Request.State.ACTIVE))
			ActivityUtil.startRequestActiveActivity(this, currentUser);
		else if (request.getState().equals(Request.State.PENDING))
			ActivityUtil.startRequestPendingActivity(this, currentUser);
		else
			showMessage(getString(R.string.invalid_state));
		ActivityUtil.transitionFade(this);
	}

	@Override
	public void onModelError(Throwable t) {
		// no request found, drop into first view
		ActivityUtil.startFirstActivity(this, currentUser);
		ActivityUtil.transitionFade(this);
	}

}
