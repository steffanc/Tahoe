package com.lake.tahoe.activities;

import android.os.Bundle;
import android.view.View;
import com.lake.tahoe.callbacks.ModelCallback;
import com.lake.tahoe.callbacks.PublishedCallback;
import com.lake.tahoe.models.Request;
import com.lake.tahoe.models.User;
import com.lake.tahoe.utils.ActivityUtil;

public class RequestActiveVendorActivity extends RequestActiveActivity implements ModelCallback<Request> {
	Request request;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		User.getCurrentUser().getUnfinishedRequest(this);
	}

	@Override
	public void onModelFound(Request _request) {
		request = _request;
		User client = request.getClient();
		createViews(client);
		createMapViews(client);

		String title = String.format("%s | %s", request.getDisplayDollars(), request.getTitle());
		getDynamicActionBar().setTitle(title);

		getDynamicActionBar().setCancelAction(new View.OnClickListener() {
			@Override public void onClick(View v) {
				finish();
			}
		});

		getDynamicActionBar().setAcceptAction(new View.OnClickListener() {
			@Override public void onClick(View v) {
				completeRequest();
			}
		});
	}

	@Override
	public void finish() {
		super.finish();
		ActivityUtil.transitionRight(this);
	}

	public void completeRequest() {
		request.setState(Request.State.PENDING);
		request.saveAndPublish(this, new PublishedCallback() {
			@Override public void onPublished() {
				ActivityUtil.startRequestPendingActivity(RequestActiveVendorActivity.this, User.getCurrentUser());
				ActivityUtil.transitionFade(RequestActiveVendorActivity.this);
			}
		});
	}

	@Override
	public void onModelError(Throwable t) {
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

