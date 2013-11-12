package com.lake.tahoe.utils;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import com.lake.tahoe.receivers.JsonDataReceiver;
import com.parse.*;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created on 11/2/13.
 */
public class PushUtil {

	public static final String KEY_INTENT_FILTER = "action";
	public static final String KEY_OBJECT_ID     = "object_id";

	public static interface HandlesPublish extends HandlesErrors {
		public void onPublished(ParsePush push);
	}

	public static interface CanRegisterReciever {
		public Intent registerReceiver(BroadcastReceiver receiver, IntentFilter filter);
		public void unregisterReceiver(BroadcastReceiver receiver);
	}

	public static String getIntentFilterAction(Class<? extends ParseObject> klass) {
		return String.format("%s.CHANNEL", klass.getCanonicalName());
	}

	public static String getIntentFilterAction(ParseObject object) {
		return getIntentFilterAction(object.getClass());
	}

	public static <T extends ParseObject> void saveAndPublish(final T object, final HandlesPublish callback) {
		if (callback == null) {
			object.saveEventually();
			return;
		}
		object.saveInBackground(new SaveCallback() {
			@Override public void done(ParseException e1) {
				if (e1 != null) callback.onError(e1);
				else try {
					JSONObject payload = new JSONObject();
					payload.put(KEY_INTENT_FILTER, getIntentFilterAction(object));
					payload.put(KEY_OBJECT_ID, object.getObjectId());
					publish(payload, callback);
				} catch (JSONException e2) {
					callback.onError(e2);
				}
			}
		});
	}

	public static void saveAndPublish(final ParseObject object) {
		saveAndPublish(object, null);
	}

	public static void publish(JSONObject payload, final HandlesPublish callback) {
		final ParsePush push = new ParsePush();
		push.setQuery(ParseInstallation.getQuery());
		push.setData(payload);
		if (callback == null) {
			push.sendInBackground();
		} else {
			push.sendInBackground(new SendCallback() {
				@Override public void done(ParseException e) {
					if (e != null) callback.onError(e);
					else callback.onPublished(push);
				}
			});
		}
	}

	public static JsonDataReceiver subscribe(CanRegisterReciever context, Class<? extends ParseObject> klass, JsonDataReceiver.JsonDataHandler handler) {
		return subscribe(context, getIntentFilterAction(klass), handler);
	}

	public static JsonDataReceiver subscribe(CanRegisterReciever context, String intentFilterAction, JsonDataReceiver.JsonDataHandler handler) {
		IntentFilter filter = new IntentFilter(intentFilterAction);
		JsonDataReceiver receiver = new JsonDataReceiver(handler);
		context.registerReceiver(receiver, filter);
		return receiver;
	}

	public static void unsubscribe(CanRegisterReciever context, JsonDataReceiver receiver) {
		context.unregisterReceiver(receiver);
	}

}
