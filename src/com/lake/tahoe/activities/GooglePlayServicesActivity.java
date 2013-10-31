package com.lake.tahoe.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.lake.tahoe.R;
import com.lake.tahoe.fragments.SimpleDialogFragment;

public abstract class GooglePlayServicesActivity extends FragmentActivity {

	/**
	 * The request code for a device requesting Google Play services
	 */
	private final static int GOOGLE_PLAY_SERVICES_REQUEST_CODE = 666;

	protected abstract void onGooglePlayServicesReady();

	protected abstract void onGooglePlayServicesError(Throwable t);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
		if (ConnectionResult.SUCCESS == resultCode) onGooglePlayServicesReady();
		else spawnGooglePlayDialog(resultCode);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == GOOGLE_PLAY_SERVICES_REQUEST_CODE) {
			if (resultCode == Activity.RESULT_OK) onGooglePlayServicesReady();
			else onGooglePlayServicesError(new GooglePlayException("Google Play Services Required"));
		}
	}

	private void spawnGooglePlayDialog(int resultCode) {

		Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(
			resultCode, this, GOOGLE_PLAY_SERVICES_REQUEST_CODE
		);

		if (errorDialog == null)
			onGooglePlayServicesError(new GooglePlayException("Unable to install Google Play Services"));

		SimpleDialogFragment errorFragment = new SimpleDialogFragment();
		errorFragment.setDialog(errorDialog);
		errorFragment.show(getSupportFragmentManager(), getString(R.string.gps_dialog_title));

	}

	private static class GooglePlayException extends Exception {
		private GooglePlayException(String detailMessage) {
			super(detailMessage);
		}
	}

}
