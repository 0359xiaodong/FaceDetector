package teamthree.facedetector.ui.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PointF;
import android.media.FaceDetector;
import android.util.AttributeSet;
import android.widget.ImageView;

public class FaceDetectorImageView extends ImageView {

	private int count;
	private FaceDetector.Face[] faces;

	private PointF tmpPoint = new PointF();
	private Paint tmpPaint = new Paint();

	public FaceDetectorImageView(Context context, AttributeSet attrs) {
		super(context, attrs);

	}

	public FaceDetectorImageView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);

	}

	public FaceDetectorImageView(Context context) {
		super(context);

	}

	public void setFaces(FaceDetector.Face[] faces) {

		this.faces = faces;
	}

	public void setCount(int count) {

		this.count = count;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		if (count > 0 && faces != null) {

			for (int i = 0; i < count; i++) {
				FaceDetector.Face face = faces[i];
				tmpPaint.setColor(Color.RED);
				tmpPaint.setAlpha(100);
				tmpPaint.setStyle(Style.STROKE);
				tmpPaint.setStrokeWidth(10);
				face.getMidPoint(tmpPoint);
				canvas.drawCircle(tmpPoint.x,
						tmpPoint.y + ((float) (face.eyesDistance() * 0.5)),
						((float) (face.eyesDistance() * 1.5)), tmpPaint);
			}
		}
	}
}