package com.lake.tahoe.activities;

import android.os.Bundle;
import android.view.View;
import com.lake.tahoe.callbacks.ModelCallback;
import com.lake.tahoe.models.Request;
import com.lake.tahoe.models.User;
import com.lake.tahoe.utils.ActivityUtil;
import com.lake.tahoe.utils.PushUtil;
import com.parse.ParsePush;

public class RequestActiveVendorActivity extends RequestActiveActivity implements
		ModelCallback<Request> {
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
				abortRequest();
			}
		});

		getDynamicActionBar().setAcceptAction(new View.OnClickListener() {
			@Override public void onClick(View v) {
				completeRequest();
			}
		});
	}

	public void abortRequest() {
		request.setVendor(null);
		request.setState(Request.State.OPEN);
		request.saveAndPublish(new PushUtil.HandlesPublish() {
			@Override public void onPublished(ParsePush push) {
				finish();
			}
			@Override public void onError(Throwable t) {
				RequestActiveVendorActivity.this.onError(t);
			}
		});
		finish();
	}

	@Override
	public void finish() {
		super.finish();
		ActivityUtil.transitionLeft(this);
	}

	public void completeRequest() {
		toggleBlocker(true);
		request.setState(Request.State.PENDING);
		request.saveAndPublish(new PushUtil.HandlesPublish() {
			@Override public void onPublished(ParsePush push) {
				ActivityUtil.startRequestPendingActivity(RequestActiveVendorActivity.this, User.getCurrentUser());
				ActivityUtil.transitionFade(RequestActiveVendorActivity.this);
			}
			@Override public void onError(Throwable t) {
				RequestActiveVendorActivity.this.onError(t);
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

