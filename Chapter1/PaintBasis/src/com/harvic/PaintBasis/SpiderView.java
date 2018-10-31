package com.harvic.PaintBasis;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by qijian on 16/10/17.
 */
public class SpiderView extends View {
    //数据个数
    private int count = 6;
private float radius;//网格最大半径
private int centerX;//中心X
private int centerY;//中心Y
    private Paint radarPaint,valuePaint;
    //计算出每个夹角的度数
    private float angle = (float) (Math.PI*2/count);
//数据
private double[] data={2,5,1,6,4,5};
//最大值
private float maxValue=6;

    public SpiderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    private void init(){
        radarPaint = new Paint();
        radarPaint.setStyle(Paint.Style.STROKE);
        radarPaint.setColor(Color.GREEN);

        valuePaint = new Paint();
        valuePaint.setColor(Color.BLUE);
        valuePaint.setStyle(Paint.Style.FILL);
    }
@Override
protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    radius = Math.min(h, w)/2*0.9f;
    //中心坐标
    centerX = w/2;
    centerY = h/2;
    postInvalidate();
    super.onSizeChanged(w, h, oldw, oldh);
}

    /**
     * 绘制正多边形
     */
private void drawPolygon(Canvas canvas){
    Path path = new Path();
    float r = radius/(count);//r是蜘蛛丝之间的间距
    for(int i=1;i<=count;i++){//中心点不用绘制
        float curR = r*i;//当前半径
        path.reset();
        for(int j=0;j<count;j++){
            if(j==0){
                path.moveTo(centerX+curR,centerY);
            }else{
                //根据半径，计算出蜘蛛丝上每个点的坐标
                float x = (float) (centerX+curR*Math.cos(angle*j));
                float y = (float) (centerY+curR*Math.sin(angle*j));
                path.lineTo(x,y);
            }
        }
        path.close();//闭合路径
        canvas.drawPath(path, radarPaint);
    }
}

    /**
     * 绘制直线
     */
private void drawLines(Canvas canvas){
    Path path = new Path();
    for(int i=0;i<count;i++){
        path.reset();
        path.moveTo(centerX, centerY);
        float x = (float) (centerX+radius*Math.cos(angle*i));
        float y = (float) (centerY+radius*Math.sin(angle*i));
        path.lineTo(x, y);
        canvas.drawPath(path, radarPaint);
    }
}

    /**
     * 绘制区域
     * @param canvas
     */
private void drawRegion(Canvas canvas){
    Path path = new Path();
    valuePaint.setAlpha(127);
    for(int i=0;i<count;i++){
        double percent = data[i]/maxValue;
        float x = (float) (centerX+radius*Math.cos(angle*i)*percent);
        float y = (float) (centerY+radius*Math.sin(angle*i)*percent);
        if(i==0){
            path.moveTo(x, centerY);
        }else{
            path.lineTo(x,y);
        }
        //绘制小圆点
        canvas.drawCircle(x,y,10,valuePaint);
    }
    //绘制填充区域
    valuePaint.setStyle(Paint.Style.FILL_AND_STROKE);
    canvas.drawPath(path, valuePaint);
}

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawPolygon(canvas);
        drawLines(canvas);
        drawRegion(canvas);
    }
}
