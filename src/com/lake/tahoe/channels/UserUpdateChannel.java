package com.lake.tahoe.channels;

import android.content.Context;
import com.lake.tahoe.models.User;
import com.lake.tahoe.receivers.JsonDataReceiver;
import com.lake.tahoe.utils.PushUtil;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import org.json.JSONException;
import org.json.JSONObject;

public class UserUpdateChannel extends GetCallback<ParseUser> implements JsonDataReceiver.JsonDataHandler {

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
			User.getQuery().getInBackground(userObjectId, this);
		} catch (JSONException e) {
			handler.onUserUpdateError(e);
		}
	}

	@Override
	public void done(ParseUser user, ParseException e) {
		if (e != null) handler.onUserUpdateError(e);
		if (user != null) handler.onUserUpdated((User)user);
	}

	@Override
	public void onJsonError(Throwable t) {
		handler.onUserUpdateError(t);
	}

	public static JsonDataReceiver subscribe(Context context, HandlesUserUpdates handler) {
		return PushUtil.subscribe(context, CHANNEL_NAME, new UserUpdateChannel(handler));
	}

	public static void publish(User user) throws JSONException {
		JSONObject payload = new JSONObject();
		payload.put(OBJECT_ID_KEY, user.getObjectId());
		PushUtil.publish(CHANNEL_NAME, payload);
	}

}
