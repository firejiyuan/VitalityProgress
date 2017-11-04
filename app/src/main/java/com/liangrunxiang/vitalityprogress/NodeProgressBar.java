package com.liangrunxiang.vitalityprogress;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class NodeProgressBar extends View implements Runnable {
    /**
     * 读取空进度条图片
     */
    private BitmapDrawable db_empty;
    /**
     * View宽度
     */
    private int viewWidth;
    /**
     * View高度
     */
    private int viewHeight;
    /**
     * 白色空心圆图片集合
     */
    private List<BitmapDrawable> list_whitecircle = new ArrayList<BitmapDrawable>();
    /**
     * 蓝色实心圆图片集合
     */
    private List<BitmapDrawable> list_bluecircle = new ArrayList<BitmapDrawable>();
    /**
     * 蓝色进度条
     */
    private BitmapDrawable db_blue;

    /**
     * 进度比值       控制蓝色进度条
     */
    private double ratio = 0;


    /**
     * 节点进度和位置
     */
    private double[] nodeArr = new double[]{0, 20, 50, 80, 110};
    /**
     * 节点名称
     */
    private String[] textArr = new String[]{"0", "20", "50", "80", "110"};
    /**
     * 是否已经领取
     */
//    private boolean[] hasGetArr = new boolean[]{false, false, false, false, false};
    /**
     * 宝箱图片
     */
    private List<BitmapDrawable> list_git_box = new ArrayList<BitmapDrawable>();
    /**
     * 最大进度
     */
    private double maxProgress = nodeArr[nodeArr.length - 1];
    /**
     * 节点数
     */
    private int count = textArr.length;

    /**
     * 点击宝箱，判断x值
     */
    private int[] baoxiangIndex = new int[count];

    /**
     * 当前节点进度
     */
    private int index = 0;
    /**
     * X轴对称 偏移值
     */
    private int offX = 50;
    /**
     * Y轴 偏移值
     */
    private int offY = 100;
    /**
     * 文字与节点 偏移值
     */
    private int offTextY = 85;
    /**
     * 白色空心圆偏移值
     */
    private int offWhiteCirle_y = -3;
    /**
     * 白色空心进度条偏移值
     */
    private int offWhiteRect_y = -2;
    /**
     * 白色空心进度条偏移值
     */
    private BitmapDrawable db_blue_half_circle;
    /**
     * 创建一只新画笔
     */
    private Paint paint = new Paint();
    /**
     * 白色空心圆半径
     */
    private int r_white = 76;
    /**
     * 蓝色空心圆半径
     */
    private int r_blue = 56;
    /**
     * 白色进度条高度
     */
    private int whiteProgressHeight = 30;
    /**
     * 蓝色进度条高度
     */
    private int blueProgressHeight = 16;

    /**
     * 文本颜色j
     */
    private String textColor = "#46A3FF";
    /**
     * 文本未激活颜色
     */
    private String textColorNotEnabled = "#7E7E81";
    /**
     * View 背景颜色
     */
    private String bgColor = "#FFFFFF";
    /**
     * 文本框大小
     */
    private int textSize = 32;

    /**
     * 白色空心进度条宽度
     */
    private int maxProgressWidth;
    /**
     * 半圆蓝色进度条宽度
     */
    private int half_blueWidth = 12;
    private BitmapDrawable drawable1;
    /*private BitmapDrawable drawable2;
    private BitmapDrawable drawable3;
    private BitmapDrawable drawableGitNoOpen;
    private Drawable drawableGitNoOpened;*/
    private int offY_bao;
    private int r_bao_xiang;
    private OnGitBoxClickListener onGitBoxClickListener;

    @SuppressWarnings("deprecation")
    public NodeProgressBar(Context context) {
        super(context);
//        init(context,nodeArr,textArr);
        //初始化空心进度条
        db_empty = new BitmapDrawable(BitmapFactory.decodeResource(
                getResources(), R.drawable.progress_whtie_groove));
        //初始化
        db_blue = new BitmapDrawable(BitmapFactory.decodeResource(
                getResources(), R.drawable.progress_blue_groove));
        //根据节点个数 初始化空心圆和实心圆
        drawable1 = new BitmapDrawable(
                BitmapFactory.decodeResource(getResources(),
                        R.drawable.progress_white_circle));
    }

    public NodeProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
//        init(context,nodeArr,textArr);
        //初始化空心进度条
        db_empty = new BitmapDrawable(BitmapFactory.decodeResource(
                getResources(), R.drawable.progress_whtie_groove));
        //初始化
        db_blue = new BitmapDrawable(BitmapFactory.decodeResource(
                getResources(), R.drawable.progress_blue_groove));
        //根据节点个数 初始化空心圆和实心圆
        drawable1 = new BitmapDrawable(
                BitmapFactory.decodeResource(getResources(),
                        R.drawable.progress_white_circle));
    }

    /**
     * 控制蓝色进度条
     */
    public void setProgressOnly(int i) {
        ratio = i / maxProgress * 1.0;
        invalidate();
    }

    /**
     * 以节点数来空值进度条 至少大于1
     */
    public void setProgressByNode(final double d) {
        int progress = (int) (d * maxProgress);
        setProgressAndIndex(progress);

    }

    /**
     * 控制蓝色进度条并且对节点染色
     */
    public void setProgressAndIndex(int i) {
        if (i == 0) {
            index = 0;
            ratio = 0;
            invalidate();
            return;
        }
        for (int j = 0; j < nodeArr.length; j++) {
            if (i >= nodeArr[j]) {
                index = j + 1;
            }
        }
        if (index != count) {
            //获得节点磁力比率
            double wh = 1.0d * (r_white / 2) / (double) maxProgressWidth;
            //计算蓝色进度条和染色节点宽度
            //ratio=i%maxProgress==0?ratio=1:wh+wh*2*(index-1)+1.0d*((double)adbProgress/(double)maxProgressWidth)*(i/maxProgress);
            ratio = i % maxProgress == 0 ? ratio = 1 : (1.0d * i) / maxProgress;
        } else {
            //设置进度条为满
            ratio = 1;
        }

        /*list_bluecircle.clear();
        for (boolean hasGet : hasGetArr) {
            if (hasGet) {

                list_bluecircle.add(drawable3);
            } else {

                list_bluecircle.add(drawable2);
            }
        }*/
        invalidate();
    }

    /**
     * 初始化图片资源，和基础数值
     */
    @SuppressWarnings("deprecation")
    public void init(Context context, double[] nodeArr, String[] textArr, List<BitmapDrawable> list_bluecircle, List<BitmapDrawable> list_git_box) {
//        this.hasGetArr = hasGetArr;
        this.nodeArr = nodeArr;
        this.textArr = textArr;
        this.maxProgress = nodeArr[nodeArr.length - 1];
        this.count = textArr.length;
        this.list_bluecircle = list_bluecircle;
        this.list_git_box = list_git_box;
        /**
         * X轴对称 偏移值
         */
        offX = dip2px(context, 20);
        /**
         * Y轴 偏移值
         */
        offY = dip2px(context, 50);
        /**
         * 宝箱半径
         */
        r_bao_xiang = dip2px(context, 50);
        /**
         * Y轴 宝箱偏移值
         */
        offY_bao = offY - dip2px(context, 25);
        /**
         * 文字与节点 偏移值
         */
        offTextY = dip2px(context, 22.0f);
        /**
         * 白色空心圆偏移值
         */
        offWhiteCirle_y = dip2px(context, 0);
        /**
         * 白色空心进度条偏移值
         */
        offWhiteRect_y = dip2px(context, 0);

        /**
         * 白色空心圆半径
         */
        r_white = dip2px(context, 12f);
        /**
         * 蓝色空心圆半径
         */
        r_blue = dip2px(context, 12f);
        /**
         * 白色进度条高度
         */
        whiteProgressHeight = dip2px(context, 5.0f);
        /**
         * 蓝色进度条高度
         */
        blueProgressHeight = dip2px(context, 5.0f);

        /**
         * 文本框大小
         */
        textSize = dip2px(context, 12.8f);

        /**
         * 白色空心进度条宽度
         */
        //maxProgressWidth;
        /**
         * 半圆蓝色进度条宽度
         */
        half_blueWidth = dip2px(context, 4.8f);

        /**
         * 最大进度
         */
        maxProgress = nodeArr[nodeArr.length - 1];
        /**节点数*/
        count = textArr.length;
        list_whitecircle.clear();
        /*list_bluecircle.clear();
        list_git_box.clear();*/



        for (int i = 0; i < count; i++) {
            list_whitecircle.add(drawable1);
            /*list_bluecircle.add(drawable2);
            list_git_box.add(drawableGitNoOpen);*/
        }

        //UI线程初始化数值
        this.post(this);
    }

    @Override
    public void run() {
        //读取View宽度
        viewWidth = NodeProgressBar.this.getWidth();
        //读取View高度
        viewHeight = NodeProgressBar.this.getHeight();
        // 进度条宽度计算
        maxProgressWidth = viewWidth - r_white - offX * 2;
        //绘制
        invalidate();
    }


    @Override
    public void draw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.draw(canvas);
        //获得X轴偏转值
        int offAbs_x = (int) ((viewWidth - maxProgressWidth) / 2.0d);
        //获得X轴偏转值
        canvas.drawColor(Color.parseColor(bgColor));
        //绘制空心进度条
        drawRect(canvas, db_empty, viewWidth / 2, r_white / 2 + offY + offWhiteRect_y, maxProgressWidth, whiteProgressHeight);

        paint.setTextSize(textSize);
        paint.setFakeBoldText(true);

        //绘制白色空心园
        for (int i = 0, j = list_whitecircle.size(); i < j; i++) {
            BitmapDrawable db_whitecircle = list_whitecircle.get(i);
//			int x=maxProgressWidth / (count - 1)* i+offAbs_x;//坐标点即进度条宽度平分
            int x = (int) (maxProgressWidth / maxProgress * nodeArr[i] + offAbs_x);
            int y = r_white / 2 + offWhiteCirle_y + offY;
            drawCircle(canvas, db_whitecircle, x, y, r_white);

            String str = textArr[i];
            if (i < index) {
                paint.setColor(Color.parseColor(textColor));
            } else {
                paint.setColor(Color.parseColor(textColorNotEnabled));
            }
            float textWidht = paint.measureText(str);
            canvas.drawText(str, x - textWidht / 2, y + offTextY, paint);
        }

        //绘制蓝色进度条
        drawRect(canvas, db_blue, (int)
                        ((maxProgressWidth * ratio) / 2) + offAbs_x,
                r_white / 2 + offY,
                (int) (maxProgressWidth * ratio), blueProgressHeight);

        //绘制蓝色小半圆
        /*if (ratio > 0) {

            drawRect(canvas, db_blue_half_circle, (int) ((maxProgressWidth * ratio) / 2)
                            + (int) (maxProgressWidth * ratio) / 2 + half_blueWidth
                            / 2 + offAbs_x, r_white / 2 + offY, half_blueWidth,
                    blueProgressHeight);
        }*/
        //绘制蓝色圆
        for (int i = 0, j = index; i < j; i++) {
            BitmapDrawable db_bluecircle = list_bluecircle.get(i);
            drawCircle(canvas, db_bluecircle,
                    (int) (maxProgressWidth / maxProgress * nodeArr[i] + offAbs_x), r_white / 2 + offY, r_blue);
        }
        //绘制绘制宝箱
        for (int i = 0, j = list_git_box.size(); i < j; i++) {
            if (i != 0) {
                BitmapDrawable db_bluecircle = list_git_box.get(i);
                int x = (int) (maxProgressWidth / maxProgress * nodeArr[i] + offAbs_x);
                baoxiangIndex[i] = x;
                drawCircle(canvas, db_bluecircle,
                        x, r_white / 2 + offY_bao, r_bao_xiang);
            }
        }

    }

    /**
     * 传统矩形坐标方法
     */
    public void drawRect(Canvas canvas, Drawable d, int x, int y, int width,
                         int height) {
        d.setBounds(x - width / 2, y - height / 2, x + width / 2, y + height
                / 2);
        d.draw(canvas);
    }

    /**
     * 传统圆点坐标计算方法
     */
    public void drawCircle(Canvas canvas, Drawable d, int x, int y, int r) {
        d.setBounds(x - r / 2, y - r / 2, x + r / 2, y + r / 2);
        d.draw(canvas);
    }

    /**
     * 绘制文字 传统坐标计算方法
     */
    public void drawText(Canvas canvas, Paint paint, String str, int x, int y, int w, int h) {
        canvas.drawText(str, x - w / 2, y - h / 2, x + w, y + h, paint);
    }

    public double getRatio() {
        return ratio;
    }

    public void setRatio(double ratio) {
        this.ratio = ratio;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        for (int i = 1; i < baoxiangIndex.length; i ++) {
            if ((baoxiangIndex[i] - r_bao_xiang / 2) < event.getX() && event.getX() < (baoxiangIndex[i] + r_bao_xiang / 2)) {
                //ToastUtil.showToast("点击第"+i+"个宝箱");
                if(onGitBoxClickListener!=null) {
                    onGitBoxClickListener.onGitBoxClick(i);
                }
            }
        }
        return super.onTouchEvent(event);
    }
    public interface OnGitBoxClickListener{
        void onGitBoxClick(int index);
    }
    public void setOnGitBoxClickListener(OnGitBoxClickListener onGitBoxClickListener){
        this.onGitBoxClickListener = onGitBoxClickListener;
    }
}
