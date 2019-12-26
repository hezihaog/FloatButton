package com.zh.android.floatbutton.weiget;

import android.content.Context;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.customview.widget.ViewDragHelper;

import com.zh.android.floatbutton.R;

/**
 * <b>Package:</b> com.zh.android.floatbutton.weiget <br>
 * <b>Create Date:</b> 2019-12-24  15:03 <br>
 * <b>@author:</b> zihe <br>
 * <b>Description:</b> 浮动拽托按钮布局 <br>
 */
public class FloatButtonLayout extends FrameLayout {
    /**
     * 可拽托按钮
     */
    private View mFloatButton;
    /**
     * 拽托帮助类
     */
    private ViewDragHelper mViewDragHelper;
    /**
     * 回调
     */
    private Callback mCallback;

    public FloatButtonLayout(@NonNull Context context) {
        this(context, null);
    }

    public FloatButtonLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FloatButtonLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        mViewDragHelper = ViewDragHelper.create(this, 0.3f, new ViewDragHelper.Callback() {
            /**
             * 开始拽托时的X坐标
             */
            private int mDownX;
            /**
             * 开始拽托时的Y坐标
             */
            private int mDownY;
            /**
             * 开始拽托时的时间
             */
            private long mDownTime;

            @Override
            public boolean tryCaptureView(@NonNull View child, int pointerId) {
                return child == mFloatButton;
            }

            @Override
            public int clampViewPositionHorizontal(@NonNull View child, int left, int dx) {
                //限制左右移动的返回，不能超过父控件
                int leftBound = getPaddingStart();
                int rightBound = getMeasuredWidth() - getPaddingEnd() - child.getWidth();
                if (left < leftBound) {
                    return leftBound;
                }
                if (left > rightBound) {
                    return rightBound;
                }
                return left;
            }

            @Override
            public int clampViewPositionVertical(@NonNull View child, int top, int dy) {
                //限制上下移动的返回，不能超过父控件
                int topBound = getPaddingTop();
                int bottomBound = getMeasuredHeight() - getPaddingBottom() - child.getHeight();
                if (top < topBound) {
                    return topBound;
                }
                if (top > bottomBound) {
                    return bottomBound;
                }
                return top;
            }

            @Override
            public int getViewHorizontalDragRange(@NonNull View child) {
                return getMeasuredWidth() - getPaddingStart() - getPaddingEnd() - child.getWidth();
            }

            @Override
            public int getViewVerticalDragRange(@NonNull View child) {
                return getMeasuredHeight() - getPaddingTop() - getPaddingBottom() - child.getHeight();
            }

            @Override
            public void onViewCaptured(@NonNull View capturedChild, int activePointerId) {
                super.onViewCaptured(capturedChild, activePointerId);
                mDownX = capturedChild.getLeft();
                mDownY = capturedChild.getTop();
                mDownTime = System.currentTimeMillis();
            }

            @Override
            public void onViewReleased(@NonNull final View releasedChild, float xvel, float yvel) {
                super.onViewReleased(releasedChild, xvel, yvel);
                //松手回弹，判断如果松手位置，近左边还是右边，进行弹性滑动
                int fullWidth = getMeasuredWidth();
                final int halfWidth = fullWidth / 2;
                final int currentLeft = releasedChild.getLeft();
                final int currentTop = releasedChild.getTop();
                //滚动到左边
                final Runnable scrollToLeft = new Runnable() {
                    @Override
                    public void run() {
                        mViewDragHelper.settleCapturedViewAt(getPaddingStart(), currentTop);
                    }
                };
                //滚动到右边
                final Runnable scrollToRight = new Runnable() {
                    @Override
                    public void run() {
                        int endX = getMeasuredWidth() - getPaddingEnd() - releasedChild.getWidth();
                        mViewDragHelper.settleCapturedViewAt(endX, currentTop);
                    }
                };
                Runnable checkDirection = new Runnable() {
                    @Override
                    public void run() {
                        if (currentLeft < halfWidth) {
                            //在屏幕一半的左边，回弹回左边
                            scrollToLeft.run();
                        } else {
                            //在屏幕一半的右边，回弹回右边
                            scrollToRight.run();
                        }
                    }
                };
                //最小移动距离
                int minMoveDistance = fullWidth / 3;
                //计算移动距离
                int distanceX = currentLeft - mDownX;
                int distanceY = currentTop - mDownY;
                long upTime = System.currentTimeMillis();
                //间隔时间
                long intervalTime = upTime - mDownTime;
                float touched = getDistanceBetween2Points(new PointF(mDownX, mDownY), new PointF(currentLeft, currentTop));
                //处理点击事件，移动距离小于识别为移动的距离，并且时间小于400
                if (touched < mViewDragHelper.getTouchSlop() && intervalTime < 300) {
                    if (mCallback != null) {
                        mCallback.onClickFloatButton();
                    }
                    //因为判断为点击事件后，return就会让按钮不进行贴边回弹了，这里再添加处理，让可以贴边回弹
                    checkDirection.run();
                    return;
                }
                //判断上下滑还是左右滑
                if (Math.abs(distanceX) > Math.abs(distanceY)) {
                    //左右滑，滑动得少，并且速度很快，则为fling操作
                    if (Math.abs(distanceX) < minMoveDistance &&
                            Math.abs(xvel) > Math.abs(mViewDragHelper.getMinVelocity())) {
                        //距离相减为正数，则为往右滑
                        if (distanceX > 0) {
                            scrollToRight.run();
                        } else {
                            //否则为往左
                            scrollToLeft.run();
                        }
                    } else {
                        //不是fling操作，判断松手位置在屏幕左边还是右边
                        checkDirection.run();
                    }
                } else {
                    //上下滑，主要是判断在屏幕左还是屏幕右，不需要判断fling
                    checkDirection.run();
                }
                invalidate();
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mViewDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mViewDragHelper.processTouchEvent(event);
        return true;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mViewDragHelper != null && mViewDragHelper.continueSettling(true)) {
            invalidate();
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mFloatButton = findViewById(R.id.float_button);
        if (mFloatButton == null) {
            throw new NullPointerException("必须要有一个可拽托按钮");
        }
    }

    /**
     * 获得两点之间的距离
     */
    public static float getDistanceBetween2Points(PointF p0, PointF p1) {
        return (float) Math.sqrt(Math.pow(p0.y - p1.y, 2) + Math.pow(p0.x - p1.x, 2));
    }

    public interface Callback {
        /**
         * 点击时回调
         */
        void onClickFloatButton();
    }

    public void setCallback(Callback callback) {
        mCallback = callback;
    }
}