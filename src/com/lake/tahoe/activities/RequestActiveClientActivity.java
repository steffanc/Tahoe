package com.lake.tahoe.activities;

import android.location.Location;
import android.os.Bundle;
import com.lake.tahoe.R;
import com.lake.tahoe.callbacks.ModelCallback;
import com.lake.tahoe.handlers.RequestUpdateChannel;
import com.lake.tahoe.handlers.UserUpdateChannel;
import com.lake.tahoe.models.Request;
import com.lake.tahoe.models.User;
import com.lake.tahoe.utils.ActivityUtil;

public class RequestActiveClientActivity extends RequestActiveActivity implements
		ModelCallback<Request>,
		RequestUpdateChannel.HandlesRequestUpdates {

	@Override
	public void onRequestUpdated(Request _request) {
		if (_request == null)
			return;

		if (!_request.getObjectId().equals(request.getObjectId()))
			return;

		if (_request.getState().equals(Request.State.ACTIVE))
			return;

		if (_request.getState().equals(Request.State.PENDING))
			startPendingActivity();
		else
			onError(new IllegalStateException(_request.getState().name()));
	}

	protected void startPendingActivity() {
		ActivityUtil.startRequestPendingActivity(this, User.getCurrentUser());
		ActivityUtil.transitionFade(this);
	}

	@Override
	public void onRequestUpdateError(Throwable t) {
		onError(t);
	}

	@Override
	protected void onStart() {
		super.onStart();
		RequestUpdateChannel.subscribe(this);
	}

	@Override
	protected void onStop() {
		super.onStop();
		RequestUpdateChannel.unsubscribe(this);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		User.getCurrentUser().getUnfinishedRequest(this);
	}

	@Override
	public void onModelFound(final Request _request) {
		request = _request;
		if (request == null || !request.getState().equals(Request.State.ACTIVE)) {
			ActivityUtil.startDelegateActivity(this);
			ActivityUtil.transitionFade(this);
			return;
		}

		if (request.getVendor() == null) {
			onError(new IllegalStateException("Bad request with no vendor"));
		} else {
			createViews(request.getVendor());
			createMapViews(request.getVendor());
			String title = String.format(getString(R.string.to_the_rescue, request.getVendor().getName()));
			getDynamicActionBar().setTitle(title);
		}
	}

	@Override
	public void onModelError(Throwable t) {
		onError(t);
	}

	@Override
	public void onLocationChanged(Location location) {
		super.onLocationChanged(location);
		if (request != null) {
			updateUserDistance(User.getCurrentUser(), request.getVendor());
		}
	}

	@Override
	public void onUserUpdated(User user) {
		if (user == null || request == null)
			return;
		if (user.getObjectId().equals(request.getVendor().getObjectId())) {
			request.setVendor(user);
			updateUserDistance(User.getCurrentUser(), user);
			updateRemoteUserMarker(user);
		}
	}

	@Override
	public void onUserUpdateError(Throwable t) {
		onError(t);
	}

	@Override
	protected void onGooglePlayServicesError(Throwable t) {
		onError(t);
	}

	@Override
	protected void onLocationTrackingFailed(Throwable t) {
		onError(t);
	}

}
