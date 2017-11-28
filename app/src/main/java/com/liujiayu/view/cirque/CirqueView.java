package com.liujiayu.view.cirque;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.liujiayu.view.R;
import com.liujiayu.view.utils.DensityUtil;

/**
 * Created by liujiayu on 2017/8/28.
 */

public class CirqueView extends View {

    private Paint circularPaint;
    private Paint circularBackground;
    private Paint cirqueBackground;
    private Paint cirque;
    private Paint timecirque;
    private int radius;
    private Paint linePaint;
    private Paint timeLinePaint;
    private Paint textPaint;
    private Paint timeTextPaint;
    private Paint unitPaint;
    private Paint bitmapPaint;
    private float mCurrentAngle = 0;
    private int defaultValue;
    private int value;
    private RectF oval;
    private txtFinishListener txtFinishListener;

    private int minTxt;
    private int maxTxt;
    private int timeMinTxt;
    private int timeMaxTxt;

    int[] colors = {
            Color.parseColor("#3FA7F4"),
            Color.parseColor("#5BB3F4"),
            Color.parseColor("#77BFF4"),
            Color.parseColor("#92CAF3"),
            Color.parseColor("#AED6F3"),
            Color.parseColor("#D5C4AF"),
            Color.parseColor("#E0AD83"),
            Color.parseColor("#EA9558"),
            Color.parseColor("#F57E2C"),
            Color.parseColor("#FF6600"),
    };

    float[] floats = {
            0.54f,
            0.587f,
            0.634f,
            0.681f,
            0.728f,
            0.775f,
            0.822f,
            0.869f,
            0.916f,
            0.96f,
    };

    private SweepGradient mSweepGradient;

    private float mTimeCurrentAngle;

    private Context context;

    private Bitmap snowflake;
    private Bitmap sun;
    private int timeInitial;
    private String text;
    private String timeText;
    private float temperature;
    private float time;

    public CirqueView(Context context) {
        this(context, null);
    }

    public CirqueView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CirqueView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initPaint();
        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CirqueView, defStyleAttr, 0);
        minTxt = ta.getInteger(R.styleable.CirqueView_temperature_min, 10);
        maxTxt = ta.getInteger(R.styleable.CirqueView_temperature_max, 30);
        timeMinTxt = ta.getInteger(R.styleable.CirqueView_time_left, 10);
        timeMaxTxt = ta.getInteger(R.styleable.CirqueView_time_right, 30);
        ta.recycle();
        timeInitial = 165;
    }

    private void initPaint() {
        defaultValue = DensityUtil.dip2px(context, 10);
        value = DensityUtil.dip2px(context, 1);
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        circularPaint = new Paint();  //中间圆
        circularPaint.setColor(Color.WHITE);
        circularPaint.setAntiAlias(true);

        circularBackground = new Paint(Paint.ANTI_ALIAS_FLAG);  //中间圆背景
        circularBackground.setColor(Color.parseColor("#3FA7F4"));
        circularBackground.setAlpha(60);
        circularBackground.setMaskFilter(new BlurMaskFilter(30, BlurMaskFilter.Blur.NORMAL));
        circularBackground.setAntiAlias(true);

        cirqueBackground = new Paint();
        cirqueBackground.setStrokeWidth(DensityUtil.dip2px(context, 4.0f));
        cirqueBackground.setStrokeCap(Paint.Cap.ROUND);//充满
        cirqueBackground.setColor(Color.GRAY);
        cirqueBackground.setAlpha(50);
        cirqueBackground.setStyle(Paint.Style.STROKE);//设置空心
        cirqueBackground.setAntiAlias(true);//设置空心


        cirque = new Paint();
        cirque.setStrokeWidth(DensityUtil.dip2px(context, 4.0f));
        cirque.setStrokeCap(Paint.Cap.ROUND);
        cirque.setStyle(Paint.Style.STROKE);//设置空心
        cirque.setAntiAlias(true);//设置空心

        timecirque = new Paint();
        timecirque.setStrokeWidth(DensityUtil.dip2px(context, 4.0f));
        timecirque.setStrokeCap(Paint.Cap.ROUND);
        timecirque.setColor(Color.parseColor("#747F9D"));
        timecirque.setStyle(Paint.Style.STROKE);//设置空心
        timecirque.setAntiAlias(true);//设置空心


        linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setStrokeWidth(DensityUtil.dip2px(context, 2.0f));

        timeLinePaint = new Paint();
        timeLinePaint.setAntiAlias(true);
        timeLinePaint.setColor(Color.parseColor("#747F9D"));
        timeLinePaint.setStrokeWidth(DensityUtil.dip2px(context, 2.0f));

        textPaint = new Paint();
        textPaint.setColor(getResources().getColor(R.color.greenview));
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(DensityUtil.dip2px(context, 30));

        timeTextPaint = new Paint();
        timeTextPaint.setColor(Color.parseColor("#747F9D"));
        timeTextPaint.setAntiAlias(true);
        timeTextPaint.setTextSize(DensityUtil.dip2px(context, 15));

        unitPaint = new Paint();
        unitPaint.setColor(Color.parseColor("#747F9D"));
        unitPaint.setAntiAlias(true);
        unitPaint.setTextSize(DensityUtil.dip2px(context, 10));


        bitmapPaint = new Paint();
        bitmapPaint.setAntiAlias(true);


        //画图片，就是贴图
        snowflake = BitmapFactory.decodeResource(getResources(), R.drawable.snowflake);
        sun = BitmapFactory.decodeResource(getResources(), R.drawable.sun);
        snowflake = conversionBitmap(snowflake, DensityUtil.dip2px(context, 13), DensityUtil.dip2px(context, 13));
        sun = conversionBitmap(sun, DensityUtil.dip2px(context, 13), DensityUtil.dip2px(context, 13));

        oval = new RectF();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(measure(widthMeasureSpec), measure(heightMeasureSpec));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        radius = (getWidth() / 2) - DensityUtil.dip2px(context, 20);
        oval.set(getWidth() / 2 - radius - defaultValue, getHeight() / 2 - radius - defaultValue,
                getWidth() / 2 + radius + defaultValue, getHeight() / 2 + radius + defaultValue);
        mSweepGradient = new SweepGradient(getWidth() / 2, getHeight() / 2, colors, floats);
        cirque.setShader(mSweepGradient);
    }

    private int measure(int measureSpec) {
        int result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            //这样，当时用wrap_content时，View就获得一个默认值200px，而不是填充整个父布局。
            result = DensityUtil.dip2px(context, 200);
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }

        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //背景圆以及背景虚化
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, radius + DensityUtil.dip2px(context, 5), circularBackground);
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, radius, circularPaint);
        //弧形背景
        canvas.drawArc(oval, 195, 150, false, cirqueBackground);//小弧形
        canvas.drawArc(oval, 15, 150, false, cirqueBackground);//小弧形
        //温度绘制弧形
        if (mCurrentAngle != 0) {
            canvas.drawArc(oval, 195, mCurrentAngle, false, cirque);//小弧形
        }
        //时间绘制弧形
        if (mTimeCurrentAngle != 0) {
            canvas.drawArc(oval, 165, -mTimeCurrentAngle, false, timecirque);//小弧形
        }
        //文字以及图标绘制
        int v = maxTxt - minTxt;
        int timeV = timeMaxTxt - timeMinTxt;
        text = Math.round(mCurrentAngle / (150f / v)) + minTxt + "℃";
        timeText = Math.round(mTimeCurrentAngle / (150f / timeV)) + timeMinTxt + "min";
        textPaint.setColor(colors[(int) (mCurrentAngle / (15 + 0.5f))]);
        canvas.drawText(text, getWidth() / 2 - textPaint.measureText(text) / 2, getHeight() / 2 - DensityUtil.dip2px(context, 5), textPaint);
        canvas.drawText(timeText, getWidth() / 2 - timeTextPaint.measureText(timeText) / 2, getHeight() / 2 + DensityUtil.dip2px(context, 20), timeTextPaint);
        canvas.drawText(timeMinTxt + "", getWidth() / 2 - radius - defaultValue - timeTextPaint.measureText(timeMinTxt + "") * 1 / 3 + value, getHeight() / 2 + DensityUtil.dip2px(context, 17), unitPaint);
        canvas.drawText(timeMaxTxt + "", getWidth() / 2 + radius + defaultValue - timeTextPaint.measureText(timeMaxTxt + "") * 1 / 3 - value, getHeight() / 2 + DensityUtil.dip2px(context, 17), unitPaint);
        canvas.drawBitmap(snowflake, getWidth() / 2 - radius - defaultValue - snowflake.getWidth() / 2 + value, getHeight() / 2 - DensityUtil.dip2px(context, 17), bitmapPaint);
        canvas.drawBitmap(sun, getWidth() / 2 + radius + defaultValue - sun.getWidth() / 2 - value, getHeight() / 2 - DensityUtil.dip2px(context, 17), bitmapPaint);

        //刻度绘制竖线
        linePaint.setColor(colors[(int) (mCurrentAngle / (15 + 0.5f))]);
        canvas.rotate(mCurrentAngle + 15f, getWidth() / 2, getHeight() / 2);
        canvas.drawLine(getWidth() / 2 - radius - defaultValue - DensityUtil.dip2px(context, 1), getHeight() / 2 - DensityUtil.dip2px(context, 1), getWidth() / 2 - radius * 3 / 4, getHeight() / 2, linePaint);
        canvas.rotate(-mTimeCurrentAngle - 30f - mCurrentAngle, getWidth() / 2, getHeight() / 2);
        canvas.drawLine(getWidth() / 2 - radius - defaultValue - DensityUtil.dip2px(context, 1), getHeight() / 2 + DensityUtil.dip2px(context, 1), getWidth() / 2 - radius * 3 / 4, getHeight() / 2, timeLinePaint);

        if (txtFinishListener != null) {  //监听
            txtFinishListener.onFinish(text, timeText);
        }
    }

    public void setDefault(int temperature, int time) {
        if (temperature < minTxt || temperature > maxTxt) {
            throw new RuntimeException("Temperature out of range");
        }
        if (time < timeMinTxt || time > timeMaxTxt) {
            throw new RuntimeException("Time out of range");
        }
        this.temperature = (float) (temperature - minTxt) / (maxTxt - minTxt) * 150f;
        this.time = (float) (time - timeMinTxt) / (timeMaxTxt - timeMinTxt) * 150f;
        startAnim();
    }

    public void startAnim() {
        ValueAnimator mAngleAnim = ValueAnimator.ofFloat(0, temperature);
        mAngleAnim.setInterpolator(new AccelerateDecelerateInterpolator());
        mAngleAnim.setDuration(1500);
        mAngleAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mCurrentAngle = (float) valueAnimator.getAnimatedValue();
                postInvalidate();
            }
        });
        mAngleAnim.start();

        ValueAnimator mTimeAngleAnim = ValueAnimator.ofFloat(0, time);
        mTimeAngleAnim.setInterpolator(new AccelerateDecelerateInterpolator());
        mTimeAngleAnim.setDuration(1500);
        mTimeAngleAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mTimeCurrentAngle = (float) valueAnimator.getAnimatedValue();
                postInvalidate();
            }
        });
        mTimeAngleAnim.start();
    }

    boolean tag = false;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        /*获取点击位置的坐标*/
        float Action_x = event.getX();
        float Action_y = event.getY();
        /*根据坐标转换成对应的角度*/
        float get_x0 = Action_x - getWidth() / 2;
        float get_y0 = Action_y - getHeight() / 2;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (Action_y < getHeight() / 2) { //温度
                    tag = true;
                } else {//时间
                    tag = false;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (tag) { //温度
                    /*02：左上角区域*/
                    if (get_x0 <= 0 & get_y0 <= 0) {
                        float tan_x = get_x0 * (-1);
                        float tan_y = get_y0 * (-1);
                        double atan = Math.atan(tan_y / tan_x);
                        mCurrentAngle = (int) Math.toDegrees(atan);
                    }

                    /*03：右上角区域*/
                    if (get_x0 >= 0 & get_y0 <= 0) {
                        float tan_x = get_x0;
                        float tan_y = get_y0 * (-1);
                        double atan = Math.atan(tan_x / tan_y);
                        mCurrentAngle = (int) Math.toDegrees(atan) + 90f;
                    }
                    if (Math.abs(mCurrentAngle) <= 1 || (get_x0 <= 0 & get_y0 >= 0))
                        mCurrentAngle = 0;
                    if (Math.abs(mCurrentAngle) >= 149 || (get_x0 >= 0 & get_y0 >= 0))
                        mCurrentAngle = 150;
                } else if (!tag) { //时间
                    /*01：左下角区域*/
                    if (get_x0 <= 0 & get_y0 >= 0) {
                        float tan_x = get_x0 * (-1);
                        float tan_y = get_y0;
                        double atan = Math.atan(tan_x / tan_y);
                        mTimeCurrentAngle = -(int) Math.toDegrees(atan) + 90f;
                    }

                    /*04：右下角区域*/
                    if (get_x0 >= 0 & get_y0 >= 0) {
                        float tan_x = get_x0;
                        float tan_y = get_y0;
                        double atan = Math.atan(tan_y / tan_x);
                        mTimeCurrentAngle = -(int) Math.toDegrees(atan) + 180f;
                    }

                    if (Math.abs(mTimeCurrentAngle) <= 1 || (get_x0 <= 0 & get_y0 <= 0))
                        mTimeCurrentAngle = 0;
                    if (Math.abs(mTimeCurrentAngle) >= 149 || (get_x0 >= 0 & get_y0 <= 0))
                        mTimeCurrentAngle = 150;


                }
                break;
            case MotionEvent.ACTION_POINTER_UP:
                break;
        }

        /*得到点的角度后进行重绘*/
        invalidate();
        return true;
    }

    public Bitmap conversionBitmap(Bitmap bitmap, int newWidth, int newHeight) {
        Bitmap bitMap = bitmap;
        int width = bitMap.getWidth();
        int height = bitMap.getHeight();
        // 计算缩放比例
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片
        return Bitmap.createBitmap(bitMap, 0, 0, width, height, matrix, true);
    }

    public interface txtFinishListener {

        void onFinish(String temperature, String time);

    }

    public txtFinishListener getTxtFinishListener() {
        return txtFinishListener;
    }

    public void setTxtFinishListener(txtFinishListener txtFinishListener) {
        this.txtFinishListener = txtFinishListener;
    }

    /**
     * 设置温度
     *
     * @param temperaturemin 最小温度
     * @param temperaturemax 最大温度
     */
    public void setTemperaturemin(int temperaturemin, int temperaturemax) {
        this.minTxt = temperaturemin;
        this.maxTxt = temperaturemax;

    }

    /**
     * 设置时间
     *
     * @param timemin 最小时间
     * @param timemax 最大时间
     */
    public void setTime(int timemin, int timemax) {
        if (timemin < 0) {
            throw new RuntimeException("Time cannot be negative");
        }
        this.timeMinTxt = timemin;
        this.timeMaxTxt = timemax;
    }

    public int getMinTxt() {
        return minTxt;
    }

    public int getMaxTxt() {
        return maxTxt;
    }

    public int getTimeMinTxt() {
        return timeMinTxt;
    }

    public int getTimeMaxTxt() {
        return timeMaxTxt;
    }


}
