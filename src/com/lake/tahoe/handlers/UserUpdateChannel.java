package com.lake.tahoe.handlers;

import com.lake.tahoe.callbacks.ModelCallback;
import com.lake.tahoe.callbacks.ModelGetCallback;
import com.lake.tahoe.models.User;
import com.lake.tahoe.receivers.JsonDataReceiver;
import com.lake.tahoe.utils.PushUtil;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UserUpdateChannel implements
	ModelCallback<User>,
	JsonDataReceiver.JsonDataHandler {

	private static Map<HandlesUserUpdates, JsonDataReceiver> map =
			new HashMap<HandlesUserUpdates, JsonDataReceiver>();

	public static interface HandlesUserUpdates extends PushUtil.CanRegisterReciever {
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
			String userObjectId = data.getString(PushUtil.KEY_OBJECT_ID);
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

	public static boolean isSubscribed(HandlesUserUpdates handler) {
		return map.containsKey(handler);
	}

	public static void subscribe(HandlesUserUpdates handler) {
		if (isSubscribed(handler)) return;
		map.put(handler, PushUtil.subscribe(handler, User.class, new UserUpdateChannel(handler)));
	}

	public static void unsubscribe(HandlesUserUpdates handler) {
		if (!isSubscribed(handler)) return;
		PushUtil.unsubscribe(handler, map.get(handler));
		map.remove(handler);
	}

}
