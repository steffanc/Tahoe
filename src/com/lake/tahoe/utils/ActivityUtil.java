package com.lake.tahoe.utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import com.lake.tahoe.R;
import com.lake.tahoe.activities.*;
import com.lake.tahoe.models.Request;
import com.lake.tahoe.models.User;

public class ActivityUtil {

	public static Intent newIntent() {
		Intent intent = new Intent();
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		return intent;
	}

	public static Intent newIntent(Context from, Class<? extends Activity> to) {
		return newIntent().setComponent(new ComponentName(from, to));
	}

	public static void startRequestDetailActivity(Context ctx, Request request) {
		Intent i = ActivityUtil.newIntent(ctx, RequestDetailActivity.class);
		i.putExtra(RequestDetailActivity.REQUEST_ID, request.getObjectId());
		ctx.startActivity(i);
		ActivityUtil.transitionRight((Activity)ctx);
	}

	public static void startRequestPendingActivity(Context ctx, User user) {
		if (user.getType().equals(User.Type.VENDOR))
			ctx.startActivity(ActivityUtil.newIntent(ctx, RequestPendingVendorActivity.class));
		else
			ctx.startActivity(ActivityUtil.newIntent(ctx, RequestPendingClientActivity.class));
	}

	public static void startRequestOpenActivity(Context ctx) {
		ctx.startActivity(ActivityUtil.newIntent(ctx, RequestOpenActivity.class));
	}

	public static void startRequestActiveActivity(Context ctx, User user) {
		if (user.getType().equals(User.Type.VENDOR)) {
			ctx.startActivity(ActivityUtil.newIntent(ctx, RequestActiveVendorActivity.class));
		} else {
			ctx.startActivity(ActivityUtil.newIntent(ctx, RequestActiveClientActivity.class));
		}
	}

	public static void startDelegateActivity(Context ctx) {
		ctx.startActivity(ActivityUtil.newIntent(ctx, DelegateActivity.class));
	}

	public static void startFirstActivity(Context ctx, User user) {
		if (user.getType().equals(User.Type.VENDOR))
			ActivityUtil.startRequestMapActivity(ctx);
		else
			ActivityUtil.startRequestCreateActivity(ctx);
	}

	public static void startRequestCreateActivity(Context ctx) {
		ctx.startActivity(ActivityUtil.newIntent(ctx, RequestCreateActivity.class));
	}

	public static void startRequestMapActivity(Context ctx) {
		ctx.startActivity(ActivityUtil.newIntent(ctx, RequestMapActivity.class));
	}

	public static void startLoginActivity(Context ctx) {
		ctx.startActivity(ActivityUtil.newIntent(ctx, LoginActivity.class));
	}

	public static void transitionRight(Activity activity) {
		activity.overridePendingTransition(R.anim.slide_left_enter, R.anim.slide_left_leave);
	}

	public static void transitionLeft(Activity activity) {
		activity.overridePendingTransition(R.anim.slide_right_enter, R.anim.slide_right_leave);
	}

	public static void transitionFade(Activity activity) {
		activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
	}

}
