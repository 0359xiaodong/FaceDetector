package teamthree.facedetector.ui;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.facebook.android.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

public class BaseActivity extends FragmentActivity {

	protected ImageLoader imageLoader = ImageLoader.getInstance();
	DisplayImageOptions options;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(
						R.drawable.com_facebook_profile_default_icon)
				.showImageForEmptyUri(
						R.drawable.com_facebook_profile_default_icon)
				.showImageOnFail(R.drawable.com_facebook_profile_default_icon)
				.cacheInMemory(true).cacheOnDisc(true).considerExifParams(true)
				.imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
	}

	public DisplayImageOptions getOptions() {

		return options;
	}

	public ImageLoader getImageLoader() {

		return imageLoader;
	}
}
