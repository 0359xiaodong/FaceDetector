package teamthree.facedetector.ui;

import java.util.List;

import org.json.JSONObject;

import teamthree.facedetector.R;
import teamthree.facedetector.db.DBHelper;
import teamthree.facedetector.db.provider.DBProvider;
import teamthree.facedetector.ui.views.DisableTouchFrameLayout;
import teamthree.facedetector.util.UtilHelper;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter.ViewBinder;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.facebook.Request;
import com.facebook.Request.GraphUserListCallback;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

public class StartFragment extends ListFragment implements
		LoaderCallbacks<Cursor> {

	public static final String TAG = StartFragment.class.getSimpleName();

	private static final int FRIENDS_LOADER_ID = 101;

	private DisableTouchFrameLayout mRoot = null;

	private LoginButton mLoginBtn = null;
	private UiLifecycleHelper mUiHelper = null;

	private String[] from = { DBHelper.KEY_COLUMN_NAME,
			DBHelper.KEY_COLUMN_PICTURE };
	private int[] to = { R.id.item_friend_name_tv, R.id.item_friend_image };
	private SimpleCursorAdapter mAdapter = null;

	private ProgressBar mProgressBar = null;

	private DisplayImageOptions mOptions = null;
	private ImageLoader mImageLoader = null;

	private Menu mMenu = null;

	private int activeDownloaders = 0;

	private boolean mCheckDisableTouch = false;

	public static Fragment newInstance(FragmentActivity activity) {

		Fragment startFragment = new StartFragment();
		activity.getSupportFragmentManager().beginTransaction()
				.replace(R.id.frame_content, startFragment, TAG).commit();

		return startFragment;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		mImageLoader = ((BaseActivity) getActivity()).getImageLoader();
		mOptions = ((BaseActivity) getActivity()).getOptions();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mUiHelper = new UiLifecycleHelper(getActivity(), callback);
		mUiHelper.onCreate(savedInstanceState);

		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		mRoot = (DisableTouchFrameLayout) inflater.inflate(
				R.layout.start_fragment, null);

		mProgressBar = (ProgressBar) mRoot.findViewById(R.id.start_fragment_pb);

		mLoginBtn = (LoginButton) mRoot.findViewById(android.R.id.empty);
		mLoginBtn.setFragment(this);

		mAdapter = new SimpleCursorAdapter(getActivity(),
				R.layout.layout_item_friend, null, from, to, 0);
		mAdapter.setViewBinder(new ViewBinder() {

			@Override
			public boolean setViewValue(View view, Cursor cursor, int index) {

				int viewId = view.getId();

				if (viewId == R.id.item_friend_image) {

					ImageView avatar = (ImageView) view;

					String photoUrl = cursor.getString(cursor
							.getColumnIndex(DBHelper.KEY_COLUMN_PICTURE));
					if (mImageLoader != null && mOptions != null
							&& photoUrl != null && !TextUtils.isEmpty(photoUrl))
						mImageLoader.displayImage(photoUrl, avatar, mOptions,
								new ImageLoadingListener() {

									@Override
									public void onLoadingStarted(String arg0,
											View arg1) {

										activeDownloaders++;
										if (!UtilHelper.isImageDownloaded()) {

										}
									}

									@Override
									public void onLoadingFailed(String arg0,
											View arg1, FailReason arg2) {

										activeDownloaders--;
										if (!UtilHelper.isImageDownloaded()
												&& !mCheckDisableTouch) {

											checkDisableTouch();
										}
									}

									@Override
									public void onLoadingComplete(String arg0,
											View arg1, Bitmap arg2) {

										activeDownloaders--;
										if (!UtilHelper.isImageDownloaded()
												&& !mCheckDisableTouch) {

											checkDisableTouch();
										}
									}

									@Override
									public void onLoadingCancelled(String arg0,
											View arg1) {

										activeDownloaders--;
										if (!UtilHelper.isImageDownloaded()
												&& !mCheckDisableTouch) {

											checkDisableTouch();
										}
									}
								});

					return true;
				}
				return false;
			}
		});
		setListAdapter(mAdapter);
		getLoaderManager().restartLoader(FRIENDS_LOADER_ID, null, this);

		return mRoot;
	}

	private void checkDisableTouch() {

		mCheckDisableTouch = true;
		mRoot.postDelayed(new Runnable() {

			@Override
			public void run() {

				Log.d("TEST", "activeDownloaders = " + activeDownloaders);
				if (mRoot != null)
					mRoot.setDisableTouch(activeDownloaders > 0);
				if (activeDownloaders == 0) {

					if (!(getListView() != null && getListView()
							.getLastVisiblePosition() == mAdapter.getCount() - 1)) {

						Log.d("TEST", "smoothScrollBy");
						if (getListView() != null)
							getListView().smoothScrollBy(
									getListView().getHeight(), 700);
					} else {

						Log.d("TEST", "setImageDownloaded true");
						UtilHelper.setImageDownloaded(getActivity(), true);
					}
				}

				mCheckDisableTouch = false;
			}
		}, 250);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		mUiHelper.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onPause() {
		super.onPause();

		mUiHelper.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
		mUiHelper.onResume();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mUiHelper.onDestroy();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mUiHelper.onSaveInstanceState(outState);
	}

	private long getCount() {

		if (mAdapter != null && mAdapter.getCursor() != null)
			return mAdapter.getCursor().getCount();

		return -1;
	}

	private Session.StatusCallback callback = new Session.StatusCallback() {
		@Override
		public void call(Session session, SessionState state,
				Exception exception) {

			if (state.isOpened()) {

				if (getCount() > 0)
					return;

				Request friendRequest = Request.newMyFriendsRequest(
						Session.getActiveSession(),
						new GraphUserListCallback() {
							@Override
							public void onCompleted(List<GraphUser> users,
									Response response) {

								if (getCount() > 0)
									return;

								if (users != null) {

									for (GraphUser graphUser : users) {

										ContentValues values = new ContentValues();
										JSONObject userJson = graphUser
												.getInnerJSONObject();
										if (userJson != null) {

											JSONObject pictureJson = userJson
													.optJSONObject("picture");
											if (pictureJson != null) {

												JSONObject dataPictureJson = pictureJson
														.optJSONObject("data");
												if (dataPictureJson != null)
													values.put(
															DBHelper.KEY_COLUMN_PICTURE,
															dataPictureJson
																	.optString("url"));
											}
										}
										values.put(DBHelper.KEY_COLUMN_ID,
												graphUser.getId());
										values.put(
												DBHelper.KEY_COLUMN_NAME,
												graphUser.getFirstName()
														+ " "
														+ graphUser
																.getLastName());
										getActivity()
												.getContentResolver()
												.insert(DBProvider.CONTENT_DATA_URI,
														values);
									}
								}
							}
						});
				Bundle params = new Bundle();
				params.putString("fields",
						"id, first_name, last_name, picture.type(large)");
				friendRequest.setParameters(params);
				friendRequest.executeAsync();
			} else if (state.isClosed()) {

			}
		}
	};

	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.start, menu);

		mMenu = menu;
	};

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {

		case R.id.action_from_camera:

			break;

		case R.id.action_from_photo:

			Intent intent = new Intent(getActivity(), CameraActivity.class);
			startActivity(intent);
			break;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {

		if (id == FRIENDS_LOADER_ID)
			return new CursorLoader(getActivity(), DBProvider.CONTENT_DATA_URI,
					null, null, null, null);
		setListShown(true);

		return null;
	}

	@Override
	public void setListShown(boolean shown) {

		mProgressBar.setVisibility(shown ? View.VISIBLE : View.GONE);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

		if (loader.getId() == FRIENDS_LOADER_ID) {

			mAdapter.swapCursor(cursor);
			setListShown(false);
			setCreatePhotoVisible(cursor != null && cursor.getCount() > 0);

		}
	}

	private void setCreatePhotoVisible(boolean visible) {

		if (mMenu != null && mMenu.findItem(R.id.action_from_photo) != null)
			mMenu.findItem(R.id.action_from_photo).setVisible(visible);

		if (mMenu != null && mMenu.findItem(R.id.action_from_camera) != null)
			mMenu.findItem(R.id.action_from_camera).setVisible(visible);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {

		setListAdapter(null);
		setListShown(false);
	}
}
