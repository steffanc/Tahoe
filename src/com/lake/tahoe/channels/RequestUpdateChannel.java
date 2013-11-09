package com.lake.tahoe.channels;

import android.content.Context;
import com.lake.tahoe.activities.TahoeActivity;
import com.lake.tahoe.callbacks.ModelCallback;
import com.lake.tahoe.callbacks.ModelGetCallback;
import com.lake.tahoe.models.Request;
import com.lake.tahoe.receivers.JsonDataReceiver;
import com.lake.tahoe.utils.PushUtil;
import org.json.JSONException;
import org.json.JSONObject;

public class RequestUpdateChannel implements
		ModelCallback<Request>,
		JsonDataReceiver.JsonDataHandler {

	private static final String CHANNEL_NAME  = "com.lake.tahoe.filters.REQUEST_UPDATED";
	private static final String OBJECT_ID_KEY = "object_id";

	public static interface HandlesRequestUpdates {
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
			String requestObjectId = data.getString(OBJECT_ID_KEY);
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

	public static JsonDataReceiver subscribe(Context context, HandlesRequestUpdates handler) {
		return PushUtil.subscribe(context, CHANNEL_NAME, new RequestUpdateChannel(handler));
	}

	public static void publish(Request request, TahoeActivity activity) {
		try {
			JSONObject payload = new JSONObject();
			payload.put(OBJECT_ID_KEY, request.getObjectId());
			PushUtil.publish(activity, CHANNEL_NAME, payload);
		} catch (JSONException e) {
			activity.onError(e);
		}
	}

}
