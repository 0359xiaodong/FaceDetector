package teamthree.facedetector.ui;

import teamthree.facedetector.R;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class CameraActivity extends Activity implements OnClickListener,
		SurfaceHolder.Callback {

	private SurfaceView mSurfaceView;
	private Camera mCamera;
	private Button mStopBtn;
	private ImageView mImageView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.camera_activity);

		mStopBtn = (Button) findViewById(R.id.stop_btn);
		mStopBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				getPicture();

			}
		});
		mSurfaceView = (SurfaceView) findViewById(R.id.camera);
		mImageView = (ImageView) findViewById(R.id.imageView);
		mImageView.setVisibility(View.GONE);
		SurfaceHolder holder = mSurfaceView.getHolder();
		holder.addCallback(this);

	}

	public void getPicture() {

		mCamera.takePicture(null, null, new PictureCallback() {

			@Override
			public void onPictureTaken(final byte[] data, Camera camera) {

				CameraActivity.this.runOnUiThread(new Runnable() {

					@Override
					public void run() {

						mImageView.setVisibility(View.VISIBLE);
						mImageView.setRotation(90);
						mImageView.setImageBitmap(BitmapFactory
								.decodeByteArray(data, 0, data.length));
						mSurfaceView.setVisibility(View.GONE);
						mStopBtn.setText("Compare");
						mStopBtn.setOnClickListener(CameraActivity.this);
						if (mCamera != null) {
							mCamera.stopPreview();
							mCamera.release();
							mCamera = null;
						}

					}
				});

			}
		});
	}

	@Override
	public void onClick(View v) {

		Bitmap bitmap = ((BitmapDrawable) mImageView.getDrawable()).getBitmap();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

		if (mCamera != null)
			mCamera.startPreview();

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {

		try {
			mCamera = Camera.open();
			mCamera.setDisplayOrientation(90);
			mCamera.setPreviewDisplay(holder);
			mCamera.startPreview();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {

		if (mCamera != null) {
			mCamera.stopPreview();
			mCamera.release();
			mCamera = null;
		}

	}

	@Override
	protected void onDestroy() {

		if (mCamera != null) {
			mCamera.stopPreview();
			mCamera.release();
			mCamera = null;
		}
		super.onDestroy();
	}

	// private void convert() {
	//
	// Matrix matrix = new Matrix();
	// CameraInfo info = CameraHolder.instance().getCameraInfo()[R.id.camera];
	// // Need mirror for front camera.
	// boolean mirror = (info.facing == CameraInfo.CAMERA_FACING_FRONT);
	// matrix.setScale(mirror ? -1 : 1, 1);
	// // This is the value for android.hardware.Camera.setDisplayOrientation.
	// matrix.postRotate(90);
	// // Camera driver coordinates range from (-1000, -1000) to (1000, 1000).
	// // UI coordinates range from (0, 0) to (width, height).
	// matrix.postScale(mSurfaceView.getWidth() / 2000f,
	// mSurfaceView.getHeight() / 2000f);
	// matrix.postTranslate(mSurfaceView.getWidth() / 2f,
	// mSurfaceView.getHeight() / 2f);
	// }
}
