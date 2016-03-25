package com.example.view;

import java.util.ArrayList;

import com.example.apitestapp.R;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
public class MyChartView extends View{
	


	private ArrayList<MyChartItem> items;
    private String unit;
    private String yFormat = "0.#";
    ArrayList<Integer> weather_ico;
    private Context context;
	private Paint paint1;
	private ArrayList<String> weathers;
	private ArrayList<Integer> icosNight;
	//private int[] weather_ico.get;
 //items.x icosNight
    public void SetTuView(ArrayList<MyChartItem> list, String unitInfo,ArrayList<Integer> weather_ico,ArrayList<Integer> icosNight,ArrayList<String> weathers) {
        this.items = list;
        this.unit = unitInfo;
        this.weather_ico=weather_ico;
        this.weathers=weathers;
        this.icosNight=icosNight;
    }
 
    public MyChartView(Context ct) {
        super(ct);
        this.context = ct;
    }
 
    public MyChartView(Context ct, AttributeSet attrs) {
        super(ct, attrs);
        this.context = ct;
    }
 
    public MyChartView(Context ct, AttributeSet attrs, int defStyle) {
        super(ct, attrs, defStyle);
        this.context = ct;
    }
 
    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
 
        if (items == null) {
            return;
        }
 
        int height = getHeight();
        int width = getWidth();
 
        int split = dip2px(context, 8);
        int marginl = width / 12;
        int margint = dip2px(context, 60);
        int margint2 = dip2px(context, 25);
        int bheight = height - margint - 2 * split;
        
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        Paint paint1 = new Paint(Paint.ANTI_ALIAS_FLAG);//低温
        paint.setColor(Color.parseColor("#7fffffff"));
        paint.setStrokeWidth(10);
        paint.setStyle(Style.FILL);
        paint1.setColor(Color.GREEN);//画笔颜色
        paint1.setStrokeWidth(10);
        paint1.setStyle(Style.FILL);
        canvas.drawLine(split, margint2, width - split, margint2, paint);
        
        canvas.drawLine(split, height - split, width - split, height - split,
                paint);
 
        Paint p = new Paint();
        p.setAlpha(0x0000ff);
        p.setTextSize(sp2px(context, 10));
        p.setColor(Color.parseColor("#28bbff"));
        canvas.drawText(unit, split, margint2 + split * 2, p);
 
        ArrayList<Integer> xlist = new ArrayList<Integer>();
        paint.setColor(Color.GRAY);
        for (int i = 0; i < items.size(); i++) {
            int span = (width - 2 * marginl) / items.size();
            int x = marginl + span / 2 + span * i;
            xlist.add(x);
            drawText(items.get(i).getX(), x, split * 2, canvas);
            drawText(weathers.get(i), x, margint2 + split * 2, canvas);
            drawText(weathers.get(i), x,  height - split*2, canvas);
            Log.i("drawText", weathers.get(i));
           //划线
        }
        
        float max = Float.MIN_VALUE;
        float min = Float.MAX_VALUE;
        for (int i = 0; i < items.size(); i++) {//加lll~~~~~~~~
            float y = items.get(i).getY();
            if (y > max) {
                max = y;
            }
            if (y < min) {
                min = y;
            }
        }
        for (int i = 0; i < items.size(); i++) {//加的lll~~~~~~~~
            float l = items.get(i).getL();
            if (l > max) {
                max = l;
            }
            if (l < min) {
                min = l;
            }
        }
 
        float span = max - min;
        if (span == 0) {
            span = 6.0f;
        }
        max = max + span / 6.0f;
        min = min - span / 6.0f;
 
        // ��ȡ�㼯��
        Point[] mPoints = getPoints(xlist, max, min, bheight, margint);
        Point[] mPointl = getPointL(xlist, max, min, bheight, margint);
        // ����
        paint.setColor(Color.parseColor("#7fffffff"));
        paint.setStyle(Style.FILL);
        // ����
        paint.setColor(Color.parseColor("#28bbff"));
        paint.setStyle(Style.FILL);
        paint.setStrokeWidth(10);
        drawLine(mPointl, canvas, paint1);//l
        drawLine(mPoints, canvas, paint);
    
        for (int i = 0; i < mPoints.length; i++) {
            canvas.drawCircle(mPoints[i].x, mPoints[i].y, 12, paint);
            canvas.drawCircle(mPointl[i].x, mPointl[i].y, 12, paint1);
            Bitmap bitmap=BitmapFactory.decodeResource(getResources(), weather_ico.get(i));
           // canvas.drawLine(mPoints[i].x-10, startY, stopX, stopY, paint1)
			canvas.drawBitmap(bitmap, mPoints[i].x-50, mPoints[i].y, paint);
			canvas.drawBitmap(bitmap, mPointl[i].x-50, mPointl[i].y, paint);
            canvas.drawCircle(mPoints[i].x, mPoints[i].y, 12, paint);
            String yText = new java.text.DecimalFormat(yFormat).format(items//加L
                    .get(i).getY());
            String lText = new java.text.DecimalFormat(yFormat).format(items//加L
                    .get(i).getL());
            drawText(yText, mPoints[i].x,
                    mPoints[i].y - dip2px(context, 12), canvas);
            drawText(lText, mPointl[i].x,
                    mPointl[i].y - dip2px(context, 12), canvas);
        }
    }
 
    private Point[] getPoints(ArrayList<Integer> xlist, float max, float min,//加l
            int h, int top) {
        Point[] points = new Point[items.size()];
        for (int i = 0; i < items.size(); i++) {
            int ph = top + h
                    - (int) (h * ((items.get(i).getY() - min) / (max - min)));
            points[i] = new Point(xlist.get(i), ph);
        }
        return points;
    }
   
    private Point[] getPointL(ArrayList<Integer> xlist, float max, float min,//加de l
            int h, int top) {
        Point[] points = new Point[items.size()];
        for (int i = 0; i < items.size(); i++) {
            int ph = top + h
                    - (int) (h * ((items.get(i).getL() - min) / (max - min)));
            points[i] = new Point(xlist.get(i), ph);
        }
        return points;
    }
    
    
    private void drawLine(Point[] ps, Canvas canvas, Paint paint) {
        Point startp = new Point();
        Point endp = new Point();
        for (int i = 0; i < ps.length - 1; i++) {
            startp = ps[i];
            endp = ps[i + 1];
            canvas.drawLine(startp.x, startp.y, endp.x, endp.y, paint);
        }
    }
 
    private void drawText(String text, int x, int y, Canvas canvas) {
        Paint p = new Paint();
        p.setAlpha(0x0000ff);
        p.setTextSize(sp2px(context, 14));
        p.setTextAlign(Paint.Align.CENTER);
        p.setColor(Color.WHITE);
        canvas.drawText(text, x, y, p);
    }
 
    public ArrayList<MyChartItem> getItems() {
        return items;
    }
 
    public void setItems(ArrayList<MyChartItem> items) {
        this.items = items;
    }
 
    public String getyFormat() {
        return yFormat;
    }
 
    public void setyFormat(String yFormat) {
        this.yFormat = yFormat;
    }
    
    public int sp2px(Context context, float spValue) {//sp到ps(像素)
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }
 
    public int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;//屏幕密度
        return (int) (dpValue * scale + 0.5f);
    }
}

	






