package com.cwq.tinkertest;

import android.animation.AnimatorSet;
import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.support.annotation.Keep;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/3/2 0002.
 */

public class AnimationHelper {

    public static void start(View view) {
        ViewGroup suitableParent = findSuitableParent(view);
        MyView child = new MyView(view.getContext());
        suitableParent.addView(child);
    }

    private static ViewGroup findSuitableParent(View view) {
        ViewGroup fallback = null;
        do {
            if (view instanceof FrameLayout) {
                if (view.getId() == android.R.id.content) {
                    return (ViewGroup) view;
                } else {
                    fallback = (ViewGroup) view;
                }
            }
            if (view != null) {
                final ViewParent parent = view.getParent();
                view = parent instanceof View ? (View) parent : null;
            }
        } while (view != null);

        return fallback;
    }


    private static class MyView extends View implements View.OnClickListener {
        private Bitmap mIcon;
        private Paint mPaint;
        private int mWidth;
        private int mHeight;
        private int showCount;
        private int shake;
        private ArrayList<AnimationInfo> mInfo = new ArrayList<>();
        Matrix mMatrix = new Matrix();

        public MyView(Context context) {
            super(context);
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            mWidth = MeasureSpec.getSize(widthMeasureSpec);
            mHeight = MeasureSpec.getSize(heightMeasureSpec);
            init();
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }

        private void init() {
            if (mWidth == 0)
                return;
            mIcon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
            mPaint = new Paint();
            mPaint.setAntiAlias(true);
            mPaint.setColor(Color.WHITE);


            mInfo.clear();

            int width = (int) (mIcon.getWidth() * 0.5);
            int height = (int) (mIcon.getHeight() * 0.5);
            int centerX = mWidth / 2 - width / 2;
            mInfo.add(new AnimationInfo(0.5F, 0, centerX + width * 4F, mHeight * 3 / 4 - height));
            mInfo.add(new AnimationInfo(0.55F, 20, centerX + width * 3.4F, mHeight * 3 / 4 - height * 2.2F));
            mInfo.add(new AnimationInfo(0.6F, 340, centerX + width * 2.6F, mHeight * 3 / 4 - height * 3.5F));

            mInfo.add(new AnimationInfo(0.65F, 20, centerX - width, mHeight / 2 - height));
            mInfo.add(new AnimationInfo(0.6F, 340, centerX - width * 2.8F, mHeight / 2 + height));

            mInfo.add(new AnimationInfo(0.5F, 20, centerX + width * 0.5F, mHeight / 4 - height * 2F));

            mInfo.add(new AnimationInfo(0.7F, 320, centerX, mHeight / 2F));

            mInfo.add(new AnimationInfo(0.5F, 40, centerX - width * 0.8F, mHeight / 2 + height * 3F));

            mInfo.add(new AnimationInfo(0.7F, 250, centerX - width * 2F, mHeight / 2 - height * 2F));

            mInfo.add(new AnimationInfo(0.6F, 320, centerX - width * 3F, mHeight / 2 - height * 1.5F));

            mInfo.add(new AnimationInfo(0.7F, 45, centerX + width * 3F, mHeight / 2 - height * 2F));

            mInfo.add(new AnimationInfo(0.75F, 20, centerX, mHeight / 2 - height * 2.5F));

            mInfo.add(new AnimationInfo(0.6F, 320, centerX + width * 1.5F, mHeight / 2 - height * 4F));

            mInfo.add(new AnimationInfo(0.6F, 45, centerX + width * 0.5F, mHeight / 2 - height * 4.5F));

            mInfo.add(new AnimationInfo(0.5F, 320, centerX - width * 1.8F, mHeight / 2 - height * 5F));

            mInfo.add(new AnimationInfo(0.6F, 100, centerX + width * 1.8F, mHeight / 2 + height * 3F));

            mInfo.add(new AnimationInfo(0.5F, 320, centerX, mHeight / 2 + height * 5F));

            mInfo.add(new AnimationInfo(0.6F, 10, centerX - width * 0.5F, mHeight / 2 + height * 1.5F));

            showCount = 0;

            setOnClickListener(this);

            ObjectAnimator animator1, animator2, animator3;


            animator1 = ObjectAnimator.ofInt(this, "showCount", mInfo.size());
            animator1.setDuration(mInfo.size() * 35);

            animator2 = ObjectAnimator.ofInt(this, "shake", 0, 20, 0, -20, 0, 20, 0, -20);
            animator2.setDuration(300);

            PropertyValuesHolder scaleXValuesHolder = PropertyValuesHolder.ofFloat("scaleX", 1, 8);
            PropertyValuesHolder scaleYValuesHolder = PropertyValuesHolder.ofFloat("scaleY", 1, 8);
            Keyframe keyframe1 = Keyframe.ofFloat(0, 1);
            Keyframe keyframe2 = Keyframe.ofFloat(0.5f, 1);
            Keyframe keyframe3 = Keyframe.ofFloat(1, 0);
            PropertyValuesHolder alphaValuesHolder = PropertyValuesHolder.ofKeyframe("alpha", keyframe1, keyframe2, keyframe3);
            animator3 = ObjectAnimator.ofPropertyValuesHolder(this, scaleXValuesHolder, scaleYValuesHolder, alphaValuesHolder);
            animator3.setDuration(200);

            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.playSequentially(animator1, animator2, animator3);
            animatorSet.start();
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            if (showCount < mInfo.size()) {
                drawStep1(canvas);
            } else {
                drawStep2(canvas);
            }
        }

        private void drawStep1(Canvas canvas) {
            for (int i = 0; i < showCount && i < mInfo.size(); i++) {
                AnimationInfo info = mInfo.get(i);
                canvas.save();
                mMatrix.reset();
                mMatrix.postScale(info.scale, info.scale, info.x, info.y);
                mMatrix.postRotate(info.rotate, info.x, info.y);
                canvas.concat(mMatrix);
                canvas.drawBitmap(mIcon, info.x, info.y, mPaint);
                canvas.restore();
            }
        }

        private void drawStep2(Canvas canvas) {
            for (int i = 0; i < showCount && i < mInfo.size(); i++) {
                AnimationInfo info = mInfo.get(i);
                canvas.save();
                mMatrix.reset();
                float x = info.calculateTranslationX(shake);
                float y = info.calculateTranslationY(shake);
                mMatrix.postScale(info.scale, info.scale, x, y);
                mMatrix.postRotate(info.rotate, x, y);
                canvas.concat(mMatrix);
                canvas.drawBitmap(mIcon, x, y, mPaint);
                canvas.restore();
            }
        }

        @Override
        public void onClick(View v) {
            ViewGroup parent = (ViewGroup) v.getParent();
            parent.removeView(v);
        }

        @Keep
        private void setShowCount(int showCount) {
            this.showCount = showCount;
            invalidate();
        }

        @Keep
        private void setShake(int shake) {
            this.shake = shake;
            invalidate();
        }
    }


    private static class AnimationInfo {
        float scale;
        float rotate;
        float x;
        float y;

        public AnimationInfo(float scale, float rotate, float x, float y) {
            this.scale = scale;
            this.rotate = rotate;
            this.x = x;
            this.y = y;
        }

        public float calculateTranslationX(float length) {
            return (float) Math.cos(rotate) * length + x;
        }

        public float calculateTranslationY(float length) {
            return (float) Math.sin(rotate) * length + y;
        }
    }
}
