package com.magic.circlepercentdemo;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Path.Direction;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class RingPercentView extends View {
	private int startAngle;// ��ʼ�Ƕ�
	private int sweepAngle;// ɨ���Ƕ�
	private int radius = 0;// Բ���뾶
	private int ringFrontColor = Color.RED;// Բ����ǰ��ɫ
	private int ringBackColor = Color.BLUE;// Բ���ı���ɫ
	private int strokeWidth = 30;// Բ���Ŀ��
	private int drawInterTime;// ���Ƶ��ٶ�
	private int currentSweepAngle;// ��ǰɨ��
	private int bgStartAngle;//������ʼ�Ƕ�
	private int bgSweepAngle;//����ɨ���Ƕ�
	private boolean isDrawCircleBg = true;// �Ƿ�����Բ������
	private boolean isCircleFillView = false;// ���뾶Ϊ0ʱ�Ƿ�����Ϊ�ؼ��Ŀ��.������Ϊ�ؼ���ȣ��������򲻻���Բ��
	Paint ringPaint;// Բ���Ļ���
	Paint ringBgPaint;// Բ�������Ļ���
	Timer timer;

	public RingPercentView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
		ringPaint = new Paint();
		ringPaint.setColor(ringFrontColor);
		ringPaint.setStyle(Style.STROKE);
		ringPaint.setStrokeWidth(strokeWidth);
		ringPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
		ringPaint.setStrokeCap(Paint.Cap.ROUND);

		ringBgPaint = new Paint(0);
		ringBgPaint.setColor(ringBackColor);
		ringBgPaint.setStyle(Style.STROKE);
		ringBgPaint.setStrokeWidth(strokeWidth);
		ringBgPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
		ringBgPaint.setStrokeCap(Paint.Cap.ROUND);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);

		if (widthMode == MeasureSpec.AT_MOST) {
			widthSize = radius * 2 > widthSize ? widthSize : radius * 2;
		}

		if (heightMode == MeasureSpec.AT_MOST) {
			heightSize = radius * 2 > heightSize ? heightSize : radius * 2;
		}
		
		//radius=getRealRadius(radius);
		setMeasuredDimension(MeasureSpec.makeMeasureSpec(widthSize, widthMode),
				MeasureSpec.makeMeasureSpec(heightSize, heightMode));
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if (radius == 0) {
			if (!isCircleFillView)
				return;
		}
		
		canvas.translate(getMeasuredWidth() / 2, getMeasuredHeight() / 2);
		drawRing(canvas);
	}

	private void drawRing(Canvas canvas) {
		int radius=getRealRadius(this.radius);
		if (isDrawCircleBg) {
			Path bgpath = new Path();
			RectF ovalBG = new RectF(-radius, -radius, radius, radius);
			bgpath.addArc(ovalBG, bgStartAngle, bgSweepAngle);
			canvas.drawPath(bgpath, ringBgPaint);
		}

		Path frontPath = new Path();
		ringPaint.setColor(ringFrontColor);
		RectF ovalFront = new RectF(-radius, -radius, radius, radius);
		frontPath.addArc(ovalFront, startAngle, currentSweepAngle);
		canvas.drawPath(frontPath, ringPaint);
	}

	public void setBg(int bgStartAngle,int bgSweepAngle){
		this.bgStartAngle=bgStartAngle;
		this.bgSweepAngle=bgSweepAngle;
	}
	
	public void drawRing(int startAngle, int sweepAngle, int radius, boolean isDrawCircle, int totalDrawTime) {
		this.startAngle = startAngle;
		this.sweepAngle = sweepAngle;
		
		this.radius = radius ;
		this.isDrawCircleBg = isDrawCircle;
		this.drawInterTime = totalDrawTime / sweepAngle;
		startDrawRing(drawInterTime);
	}

	public int getRealRadius(int radius) {
		// ��Բ��ֱ��������ͼ�����ֱ��Ϊ��ͼ���
		int circleTempSize = (getMeasuredHeight() > getMeasuredWidth() ? getMeasuredWidth() : getMeasuredHeight()) / 2;
		radius = radius > circleTempSize ? circleTempSize : radius;
		return radius-strokeWidth;
	}

	private void startDrawRing(int peroidTime) {
		currentSweepAngle=0;
		if (timer != null) {
			timer.cancel();
		}
		timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				currentSweepAngle++;
				if (currentSweepAngle > sweepAngle) {
					timer.cancel();
				} else {
					postInvalidate();
				}
			}
		}, 0, peroidTime);

	}

}
