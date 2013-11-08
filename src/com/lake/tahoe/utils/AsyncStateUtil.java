package com.lake.tahoe.utils;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import com.lake.tahoe.channels.RequestUpdateChannel;
import com.lake.tahoe.channels.UserUpdateChannel;
import com.lake.tahoe.models.Request;
import com.lake.tahoe.models.User;
import com.parse.ParseException;
import com.parse.SaveCallback;

public class AsyncStateUtil {

	public static void saveAndStartActivity(final User user, final Activity from,
	                                        final Class<? extends Activity> to, final HandlesErrors handler) {
		user.saveInBackground(new SaveCallback() {
			@Override public void done(ParseException e) {
				if (e == null) {
					UserUpdateChannel.publish(from, user, handler);
					startActivity(from, to);
				} else handler.onError(e);
			}
		});
	}

	public static void saveAndStartActivity(final Request request, final Activity from,
	                                        final Class<? extends Activity> to, final HandlesErrors handler) {
		request.saveInBackground(new SaveCallback() {
			@Override public void done(ParseException e) {
				if (e == null) {
					RequestUpdateChannel.publish(from, request, handler);
					startActivity(from, to);
				} else handler.onError(e);
			}
		});
	}

	public static void startActivity(Activity from, Class<? extends Activity> to) {
		Intent intent = new Intent(from, to);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		from.startActivity(intent);
	}

	public static View.OnClickListener finishOnClick(final Activity activity) {
		return new View.OnClickListener() {
			@Override public void onClick(View v) {
				activity.finish();
			}
		};
	}

}
