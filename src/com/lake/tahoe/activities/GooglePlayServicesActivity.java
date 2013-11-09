package com.lake.tahoe.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.lake.tahoe.R;
import com.lake.tahoe.fragments.SimpleDialogFragment;

public abstract class GooglePlayServicesActivity extends TahoeActivity {

	/**
	 * The request code for a device requesting Google Play services
	 */
	protected final static int GOOGLE_PLAY_SERVICES_REQUEST_CODE = 666;

	protected abstract void onGooglePlayServicesReady();

	protected abstract void onGooglePlayServicesError(Throwable t);

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
		if (ConnectionResult.SUCCESS == resultCode) onGooglePlayServicesReady();
		else spawnGooglePlayDialog(resultCode);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == GOOGLE_PLAY_SERVICES_REQUEST_CODE) {
			if (resultCode == Activity.RESULT_OK) {
				ActionBar bar = getActionBar();
				if (bar != null) bar.show();
				onGooglePlayServicesReady();
			} else {
				onGooglePlayServicesError(new GooglePlayServicesNotAvailableException(resultCode));
			}
		}
	}

	protected void spawnGooglePlayDialog(int resultCode) {

		ActionBar bar = getActionBar();
		if (bar != null) bar.hide();

		Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(
			resultCode, this, GOOGLE_PLAY_SERVICES_REQUEST_CODE
		);

		if (errorDialog == null)
			onGooglePlayServicesError(new GooglePlayServicesNotAvailableException(resultCode));

		SimpleDialogFragment errorFragment = new SimpleDialogFragment();
		errorFragment.setDialog(errorDialog);
		errorFragment.show(getSupportFragmentManager(), getString(R.string.gps_dialog_title));

	}

}
