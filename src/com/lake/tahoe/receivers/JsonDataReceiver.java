package com.lake.tahoe.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created on 11/2/13.
 */
public class JsonDataReceiver extends BroadcastReceiver {

	private static final String PAYLOAD_KEY = "com.parse.Data";

	public static interface JsonDataHandler {
		public void onJsonData(JSONObject data);
		public void onJsonError(Throwable t);
	}

	private JsonDataHandler handler;

	public JsonDataReceiver(JsonDataHandler handler) {
		this.handler = handler;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle extras = intent.getExtras();
		if (extras != null) try {
			String json = extras.getString(PAYLOAD_KEY);
			handler.onJsonData(new JSONObject(json));
		} catch (JSONException ex) {
			handler.onJsonError(ex);
		}
	}

}
