package com.lake.tahoe.channels;

import android.content.Context;
import com.lake.tahoe.activities.TahoeActivity;
import com.lake.tahoe.callbacks.ModelCallback;
import com.lake.tahoe.callbacks.ModelGetCallback;
import com.lake.tahoe.models.User;
import com.lake.tahoe.receivers.JsonDataReceiver;
import com.lake.tahoe.utils.PushUtil;
import org.json.JSONException;
import org.json.JSONObject;

public class UserUpdateChannel implements
	ModelCallback<User>,
	JsonDataReceiver.JsonDataHandler {

	private static final String CHANNEL_NAME  = "com.lake.tahoe.filters.USER_UPDATED";
	private static final String OBJECT_ID_KEY = "object_id";

	public static interface HandlesUserUpdates {
		public void onUserUpdated(User user);
		public void onUserUpdateError(Throwable t);
	}

	private HandlesUserUpdates handler;

	public UserUpdateChannel(HandlesUserUpdates handler) {
		this.handler = handler;
	}

	@Override
	public void onJsonData(JSONObject data) {
		try {
			String userObjectId = data.getString(OBJECT_ID_KEY);
			User.getUserQuery().getInBackground(userObjectId, new ModelGetCallback<User>(this));
		} catch (JSONException e) {
			handler.onUserUpdateError(e);
		}
	}

	@Override
	public void onJsonError(Throwable t) {
		handler.onUserUpdateError(t);
	}

	@Override
	public void onModelFound(User model) {
		handler.onUserUpdated(model);
	}

	@Override
	public void onModelError(Throwable t) {
		handler.onUserUpdateError(t);
	}

	public static JsonDataReceiver subscribe(Context context, HandlesUserUpdates handler) {
		return PushUtil.subscribe(context, CHANNEL_NAME, new UserUpdateChannel(handler));
	}

	public static void publish(User user, TahoeActivity activity) {
		try {
			JSONObject payload = new JSONObject();
			payload.put(OBJECT_ID_KEY, user.getObjectId());
			PushUtil.publish(activity, CHANNEL_NAME, payload);
		} catch (JSONException e) {
			activity.onError(e);
		}
	}

}
