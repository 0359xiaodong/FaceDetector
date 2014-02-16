package teamthree.facedetector.ui;

import teamthree.facedetector.R;
import teamthree.facedetector.ui.views.FaceDetectorImageView;
import teamthree.facedetector.util.UtilHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.media.FaceDetector;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

public class CameraActivity extends FragmentActivity implements
		SurfaceHolder.Callback {

	public static final int MAX_FACES = 5;

	private SurfaceView mSurfaceView;
	private Camera mCamera;
	private FaceDetectorImageView mImageView;

	private FaceDetector.Face[] faces;

	private Menu mMenu;

	private int friendsCount = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.camera_activity);

		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		mSurfaceView = (SurfaceView) findViewById(R.id.camera);
		mImageView = (FaceDetectorImageView) findViewById(R.id.imageView);
		mImageView.setVisibility(View.GONE);
		SurfaceHolder holder = mSurfaceView.getHolder();
		holder.addCallback(this);

	}

	private void getPicture() {

		mCamera.takePicture(null, null, new PictureCallback() {

			@Override
			public void onPictureTaken(final byte[] data, Camera camera) {

				CameraActivity.this.runOnUiThread(new Runnable() {

					@Override
					public void run() {

						mImageView.setVisibility(View.VISIBLE);
						BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
						bitmapOptions.inPreferredConfig = Bitmap.Config.RGB_565;
						bitmapOptions.inScaled = false;
						bitmapOptions.inDither = false;
						Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0,
								data.length, bitmapOptions);
						Bitmap rotateBitmap = UtilHelper
								.rotateImage(bitmap, 90);
						mImageView.setImageBitmap(rotateBitmap);
						detectFace(rotateBitmap);
						mSurfaceView.setVisibility(View.GONE);
						mMenu.findItem(R.id.action_get_photo).setVisible(false);
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

	private void detectFace(Bitmap bitmap) {

		FaceDetector face_detector = new FaceDetector(bitmap.getWidth(),
				bitmap.getHeight(), MAX_FACES);
		faces = new FaceDetector.Face[MAX_FACES];
		friendsCount = face_detector.findFaces(bitmap, faces);

		if (friendsCount > 0) {

			mImageView.setCount(friendsCount);
			mImageView.setFaces(faces);
			mImageView.invalidate();
		} else {

			Toast.makeText(getApplicationContext(), R.string.toast_detection,
					Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.camera, menu);
		mMenu = menu;

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (item.getItemId() == R.id.action_get_photo)
			getPicture();
		else if (item.getItemId() == android.R.id.home)
			finish();

		return super.onOptionsItemSelected(item);
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
}