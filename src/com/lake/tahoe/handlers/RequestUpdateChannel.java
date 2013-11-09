package com.lake.tahoe.handlers;

import com.lake.tahoe.callbacks.ModelCallback;
import com.lake.tahoe.callbacks.ModelGetCallback;
import com.lake.tahoe.models.Request;
import com.lake.tahoe.receivers.JsonDataReceiver;
import com.lake.tahoe.utils.PushUtil;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RequestUpdateChannel implements
		ModelCallback<Request>,
		JsonDataReceiver.JsonDataHandler {

	private static Map<HandlesRequestUpdates, JsonDataReceiver> map =
			new HashMap<HandlesRequestUpdates, JsonDataReceiver>();

	public static interface HandlesRequestUpdates extends PushUtil.CanRegisterReciever {
		public void onRequestUpdated(Request request);
		public void onRequestUpdateError(Throwable t);
	}

	private HandlesRequestUpdates handler;

	public RequestUpdateChannel(HandlesRequestUpdates handler) {
		this.handler = handler;
	}

	@Override
	public void onJsonData(JSONObject data) {
		try {
			String requestObjectId = data.getString(PushUtil.KEY_OBJECT_ID);
			Request.getRequestQuery().getInBackground(requestObjectId, new ModelGetCallback<Request>(this));
		} catch (JSONException e) {
			handler.onRequestUpdateError(e);
		}
	}

	@Override
	public void onJsonError(Throwable t) {
		handler.onRequestUpdateError(t);
	}

	@Override
	public void onModelFound(Request model) {
		handler.onRequestUpdated(model);
	}

	@Override
	public void onModelError(Throwable t) {
		handler.onRequestUpdateError(t);
	}

	public static boolean isSubscribed(HandlesRequestUpdates handler) {
		return map.containsKey(handler);
	}

	public static void subscribe(HandlesRequestUpdates handler) {
		if (isSubscribed(handler)) return;
		map.put(handler, PushUtil.subscribe(handler, Request.class, new RequestUpdateChannel(handler)));
	}

	public static void unsubscribe(HandlesRequestUpdates handler) {
		if (!isSubscribed(handler)) return;
		PushUtil.unsubscribe(handler, map.get(handler));
		map.remove(handler);
	}

}
