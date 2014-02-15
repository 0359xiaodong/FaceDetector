package teamthree.facedetector.ui;

import teamthree.facedetector.R;
import android.os.Bundle;
import android.view.Window;

public class StartActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);

		StartFragment.newInstance(this);
	}
}