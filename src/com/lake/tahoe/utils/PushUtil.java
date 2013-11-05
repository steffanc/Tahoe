package com.lake.tahoe.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Bundle;
import com.lake.tahoe.receivers.JsonDataReceiver;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * Created on 11/2/13.
 */
public class PushUtil {

	public static void publish(Context context, String channel, JSONObject payload) throws JSONException {

		/*
		 * Should be as easy as:
		 *
		 * ParsePush push = new ParsePush();
		 * payload.put("action", channel);
		 * push.setData(payload);
		 * push.sendInBackground();
		 *
		 * But their encoder is broken. Reverting to their REST API... FML
		 *
		 */

		Bundle metadata = ManifestReader.getPackageMetaData(context);
		AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler(); // ignore responses for now

		try {
			payload.put("action", channel);
			AsyncHttpClient client = new AsyncHttpClient();
			JSONObject postbody = new JSONObject();
			postbody.put("data", payload);
			postbody.put("where", new JSONObject());
			StringEntity entity = new StringEntity(postbody.toString());
			Header[] headers = new Header[2];
			headers[0] = new BasicHeader("X-Parse-Application-Id", metadata.getString("com.parse.CLIENT_ID"));
			headers[1] = new BasicHeader("X-Parse-REST-API-Key", metadata.getString("com.parse.REST_API_KEY"));
			client.post(context, "https://api.parse.com/1/push", headers, entity, "application/json", handler);
		} catch (UnsupportedEncodingException e) {
			throw new JSONException(e.toString());
		}

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
