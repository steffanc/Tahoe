package com.lake.tahoe.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

/**
 * Created by rhu on 11/2/13.
 */
public class ManifestReader {
/*
	public static Bundle getActivityMetaData(Activity activity) {
		try {
			ActivityInfo ai = activity.getPackageManager().getActivityInfo(activity.getComponentName(), PackageManager.GET_META_DATA);
			Bundle metaData = ai.metaData;

			if(metaData == null) {
				Log.d("debug", "metaData is null. Unable to get meta data");
			}
			else {
				return metaData;
			}
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}*/

	public static Bundle getPackageMetaData(Context ctx) {
		try {
			PackageManager manager = ctx.getPackageManager();
			if (manager == null) return null;
			String packageName = ctx.getPackageName();
			ApplicationInfo info = manager.getApplicationInfo(packageName, PackageManager.GET_META_DATA);
			return info == null ? null : info.metaData;
		} catch(PackageManager.NameNotFoundException ex) {
			return null;
		}
	}

	public static Object getPackageMetaData(Context ctx, String name) {
		Bundle b = getPackageMetaData(ctx);
		if (b == null) {
			return null;
		}
		return b.getString(name);
	}
}
