package teamthree.facedetector.ui;

import teamthree.facedetector.R;
import android.os.Bundle;

public class StartActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);

		getActionBar().setDisplayShowHomeEnabled(false);

		StartFragment.newInstance(this);
	}
}