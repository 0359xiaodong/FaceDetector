package teamthree.facedetector.util;

import android.content.Context;
import android.preference.PreferenceManager;

public class UtilHelper {

	private static final String KEY_IMAGE_DOWNLOADED = "image_downloaded";

	private static boolean mIsImageDownloaded = false;
	private static UtilHelper mUtilHelper = null;

	public static void setImageDownloaded(Context context, boolean flag) {

		mIsImageDownloaded = flag;
		PreferenceManager.getDefaultSharedPreferences(context).edit()
				.putBoolean(KEY_IMAGE_DOWNLOADED, flag).commit();
	}

	public static boolean isImageDownloaded() {

		return mIsImageDownloaded;
	}

	public static void init(Context context) {

		if (mUtilHelper == null)
			mUtilHelper = new UtilHelper();
		mIsImageDownloaded = PreferenceManager.getDefaultSharedPreferences(
				context).getBoolean(KEY_IMAGE_DOWNLOADED, false);
	}
}
