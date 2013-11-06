package com.lake.tahoe.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.facebook.FacebookException;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.model.GraphUser;
import com.lake.tahoe.R;
import com.lake.tahoe.models.User;
import com.lake.tahoe.utils.ErrorUtil;
import com.lake.tahoe.utils.HandlesErrors;
import com.lake.tahoe.utils.ManifestReader;
import com.parse.*;

import java.util.Arrays;

public class LoginActivity extends Activity implements HandlesErrors, View.OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		findViewById(R.id.btnLogin).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO: Show a spinner, or at least disable the button. Right now it hangs out for a bit when the user returns.
		String fbPermissions = (String) ManifestReader.getPackageMetaData(getApplicationContext(), "com.facebook.sdk.PERMISSIONS");
		ParseFacebookUtils.logIn(Arrays.asList(fbPermissions.split(",")), this, new OnLogIn());
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		ParseFacebookUtils.finishAuthentication(requestCode, resultCode, data);
		super.onActivityResult(requestCode, resultCode, data);
	}

	private class OnLogIn extends LogInCallback {
		@Override
		public void done(ParseUser parseUser, ParseException e) {
			if (e == null) getGraphData();
			else onError(e);
		}
	}

	private void getGraphData() {
		Request.newMeRequest(ParseFacebookUtils.getSession(), new OnGraphData()).executeAsync();
	}

	private class OnGraphData implements Request.GraphUserCallback {
		@Override public void onCompleted(GraphUser graphUser, Response response) {
			if (response.getError() != null)
				onError(new FacebookException(response.getError().toString()));
			if (graphUser != null) {
				User currentUser = User.getCurrentUser();
				currentUser.setFacebookId(graphUser.getId());
				currentUser.setName(graphUser.getFirstName());
				currentUser.setEmail((String) graphUser.getProperty("email"));
				currentUser.setType(User.Type.CLIENT);
				currentUser.saveEventually(new OnUserSaved());
			}
		}
	}

	class OnUserSaved extends SaveCallback {
		@Override public void done(ParseException e) {
			if (e == null)  finish();
			else onError(e);
		}
	}

	/**
	 * TODO: Make this better. Show something on the login page
	 */
	@Override
	public void onError(Throwable t) {
		ErrorUtil.log(this, t);
	}

}
