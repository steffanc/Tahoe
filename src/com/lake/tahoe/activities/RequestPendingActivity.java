package com.lake.tahoe.activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import com.lake.tahoe.R;
import com.lake.tahoe.callbacks.ModelCallback;
import com.lake.tahoe.channels.RequestUpdateChannel;
import com.lake.tahoe.models.Request;
import com.lake.tahoe.models.User;
import com.lake.tahoe.utils.ErrorUtil;
import com.lake.tahoe.utils.HandlesErrors;
import com.lake.tahoe.utils.PushUtil;

/**
 * Created on 11/5/13.
 */
public abstract class RequestPendingActivity extends Activity implements
		HandlesErrors,
		RequestUpdateChannel.HandlesRequestUpdates,
		ModelCallback<Request> {

	BroadcastReceiver subscription;

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
		if (user == null) {
			finish();
			return;
		}

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
		subscription = RequestUpdateChannel.subscribe(this, this);
	}

	@Override
	protected void onStop() {
		super.onStop();
		if (subscription != null) {
			PushUtil.unsubscribe(this, subscription);
			subscription = null;
		}
	}

	@Override
	public void onRequestUpdateError(Throwable t) {
		onError(t);
	}

	@Override
	public void onModelFound(Request request) {
		if (!request.getState().equals(Request.State.PENDING)) finish();
		else {
			tvDollars.setText(request.getDisplayDollars());
			onPendingRequest(request);
		}
	}

	@Override
	public void onModelError(Throwable t) {
		finish();
	}

	@Override
	public void onError(Throwable t) {
		ErrorUtil.log(this, t);
	}

	protected abstract void onPendingRequest(Request request);

}
