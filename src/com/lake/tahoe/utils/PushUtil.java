package com.lake.tahoe.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import com.lake.tahoe.receivers.JsonDataReceiver;
import com.parse.ParsePush;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created on 11/2/13.
 */
public class PushUtil {

	public static void publish(String channel, JSONObject payload) throws JSONException {
		ParsePush push = new ParsePush();
		payload.put("action", channel);
		push.setData(payload);
		push.sendInBackground();
	}

	public static JsonDataReceiver subscribe(Context context, String channel, JsonDataReceiver.JsonDataHandler handler) {
		IntentFilter filter = new IntentFilter(channel);
		JsonDataReceiver receiver = new JsonDataReceiver(handler);
		context.registerReceiver(receiver, filter);
		return receiver;
	}

	public static void unsubscribe(Context context, BroadcastReceiver receiver) {
		context.unregisterReceiver(receiver);
	}

}
