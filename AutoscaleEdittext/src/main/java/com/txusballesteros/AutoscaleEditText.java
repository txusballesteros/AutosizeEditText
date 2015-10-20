/*
 * Copyright Txus Ballesteros 2015 (@txusballesteros)
 *
 * This file is part of some open source application.
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
 * Contact: Txus Ballesteros <txus.ballesteros@gmail.com>
 */
package com.txusballesteros;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.widget.EditText;

public class AutoscaleEditText extends EditText {
    private final static int WITHOUT_SIZE = 0;
    private final static float DEFAULT_TEXT_SCALE = 0.70f;
    private final static int DEFAULT_ANIMATION_DURATION = 300;
    private final static float DEFAULT_LINES_LIMIT = 2.0f;
    private static final int DEF_STYLE_ATTR = 0;
    private static final int DEF_STYLE_RES = 0;
    private int originalViewWidth = WITHOUT_SIZE;
    private float originalTextSize = WITHOUT_SIZE;
    private boolean resizeInProgress = false;
    private float linesLimit = DEFAULT_LINES_LIMIT;
    private int animationDuration = DEFAULT_ANIMATION_DURATION;
    private float textScale = DEFAULT_TEXT_SCALE;
    private Paint textMeasuringPaint;

    public AutoscaleEditText(Context context) {
        super(context);
    }

    public AutoscaleEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        configureAttributes(attrs);
    }

    public AutoscaleEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        configureAttributes(attrs);
    }

    @TargetApi(21)
    public AutoscaleEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        configureAttributes(attrs);
    }

    @SuppressWarnings("unused")
    public void setLinesLimit(float linesLimit) {
        this.linesLimit = linesLimit;
    }

    @SuppressWarnings("unused")
    public void setTextScale(float textScale) {
        this.textScale = textScale;
    }

    @SuppressWarnings("unused")
    public void setAnimationDuration(int animationDuration) {
        this.animationDuration = animationDuration;
    }

    private void configureAttributes(AttributeSet attrs) {
        TypedArray attributes = getContext().getTheme()
                .obtainStyledAttributes(attrs, R.styleable.AutoscaleEditText, DEF_STYLE_ATTR, DEF_STYLE_RES);
        linesLimit = attributes
                .getFloat(R.styleable.AutoscaleEditText_linesLimit, DEFAULT_LINES_LIMIT);
        textScale = attributes
                .getFloat(R.styleable.AutoscaleEditText_textScale, DEFAULT_TEXT_SCALE);
        animationDuration = attributes
                .getInteger(R.styleable.AutoscaleEditText_animationDuration, DEFAULT_ANIMATION_DURATION);
        attributes.recycle();
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        if (!resizeInProgress) {
            float numOfLinesOnScreen = calculateNumberOfLinesNedeed();
            if (numOfLinesOnScreen > linesLimit) {
                resizeTextToSmallSize();
            } else {
                resizeTextToNormalSize();
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_ENTER) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (originalViewWidth == WITHOUT_SIZE) {
            originalViewWidth = getMeasuredWidth();
            originalViewWidth -= getPaddingRight() + getPaddingLeft();
        }
        if (originalTextSize == WITHOUT_SIZE) {
            originalTextSize = getTextSize();
            initializeTextMeasurerPaint();
        }
    }

    private void initializeTextMeasurerPaint() {
        textMeasuringPaint = new Paint();
        textMeasuringPaint.setTypeface(getTypeface());
        textMeasuringPaint.setTextSize(originalTextSize);
    }

    private void resizeTextToSmallSize() {
        float smallTextSize = (originalTextSize * textScale);
        float currentTextSize = getTextSize();
        if (currentTextSize > smallTextSize) {
            playAnimation(currentTextSize, smallTextSize);
        }
    }

    private void resizeTextToNormalSize() {
        float currentTextSize = getTextSize();
        if (currentTextSize < originalTextSize) {
            playAnimation(currentTextSize, originalTextSize);
        }
    }

    private float calculateNumberOfLinesNedeed() {
        float textSizeInPixels = measureText();
        return (textSizeInPixels / originalViewWidth);
    }

    private float measureText() {
        float result = 0f;
        if (textMeasuringPaint != null) {
            result = textMeasuringPaint.measureText(getText().toString());
        }
        return result;
    }

    @Override
    public void setTextSize(float size) {
        super.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
    }

    private void playAnimation(float origin, float destination) {
        ObjectAnimator animator
                = ObjectAnimator.ofFloat(this, "textSize", origin, destination);
        animator.setTarget(this);
        animator.setDuration(animationDuration);
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) { resizeInProgress = true; }

            @Override
            public void onAnimationEnd(Animator animation) { resizeInProgress = false; }

            @Override
            public void onAnimationCancel(Animator animation) { }

            @Override
            public void onAnimationRepeat(Animator animation) { }
        });
        animator.start();
    }
}
