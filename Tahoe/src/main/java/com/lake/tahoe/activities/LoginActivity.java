package com.lake.tahoe.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;
import com.lake.tahoe.R;
import com.lake.tahoe.models.User;
import com.lake.tahoe.utils.HandlesErrors;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class LoginActivity extends Activity implements HandlesErrors {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		//TODO: take this out when we have an actual login screen
		loginMockUser();
	}

	private void loginMockUser() {
		String username = getString(R.string.mock_username);
		String password = getString(R.string.mock_password);
		User.logInInBackground(username, password, new OnLogIn());
	}

	private void createMockUser() {
		User mock = new User();
		mock.setUsername(getString(R.string.mock_username));
		mock.setPassword(getString(R.string.mock_password));
		mock.setEmail(getString(R.string.mock_email));
		mock.setType(User.Type.valueOf(getString(R.string.mock_type)));
		mock.signUpInBackground(new OnSignUp());
	}

	/**
	 * NOTE(steffan): this can be re-used when you wire-up FB login
	 */
	private class OnSignUp extends SignUpCallback {
		@Override
		public void done(ParseException e) {
			if (e == null) finish();
			else onError(e);
		}
	}

	/**
	 * NOTE(steffan): this can be re-used when you wire-up FB login
	 */
	private class OnLogIn extends LogInCallback {
		@Override
		public void done(ParseUser parseUser, ParseException e) {
			if (e == null) finish();
			else createMockUser(); // TODO: swap this out with a method that uses user-provided data
		}
	}

	/**
	 * TODO: Make this better. Show something on the login page
	 */
	@Override
	public void onError(Throwable t) {
		Log.e(this.getLocalClassName(), "Login Error", t);
		Toast toast = Toast.makeText(this, t.getMessage(), Toast.LENGTH_LONG);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}
}
