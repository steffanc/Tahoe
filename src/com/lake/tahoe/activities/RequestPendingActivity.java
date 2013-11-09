package com.lake.tahoe.activities;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import com.lake.tahoe.R;
import com.lake.tahoe.callbacks.ModelCallback;
import com.lake.tahoe.handlers.RequestUpdateChannel;
import com.lake.tahoe.models.Request;
import com.lake.tahoe.models.User;
import com.lake.tahoe.utils.ActivityUtil;

/**
 * Created on 11/5/13.
 */
public abstract class RequestPendingActivity extends TahoeActivity implements
		RequestUpdateChannel.HandlesRequestUpdates,
		ModelCallback<Request> {

	protected TextView tvSurText;
	protected TextView tvSubText;
	protected TextView tvDollars;
	protected TextView tvPay;

	protected ImageView ivCheck;
	protected ImageView ivCancel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_request_pending);

		User user = User.getCurrentUser();

		tvPay     = (TextView) findViewById(R.id.tvPay);
		tvSubText = (TextView) findViewById(R.id.tvSubText);
		tvSurText = (TextView) findViewById(R.id.tvSurText);
		tvDollars = (TextView) findViewById(R.id.tvDollars);

		ivCheck  = (ImageView) findViewById(R.id.ivCheck);
		ivCancel = (ImageView) findViewById(R.id.ivCancel);

		user.getUnfinishedRequest(this);

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
	public void onRequestUpdateError(Throwable t) {
		onError(t);
	}

	@Override
	public void finish() {
		super.finish();
		ActivityUtil.transitionFade(this);
	}

	@Override
	public void onModelFound(Request request) {
		if (!request.getState().equals(Request.State.PENDING)) {
			finish();
		} else {
			tvDollars.setText(request.getDisplayDollars());
			onPendingRequest(request);
		}
	}

	@Override
	public void onModelError(Throwable t) {
		onError(t);
	}

	protected abstract void onPendingRequest(Request request);

}
