/*
 * Copyright (c) 2015 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package lib.material.dialogs.internal.progress;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Animatable;
import android.os.Build;

@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
abstract class IndeterminateProgressDrawableBase extends ProgressDrawableBase
        implements Animatable {

    protected Animator[] mAnimators;

    public IndeterminateProgressDrawableBase(Context context) {
        super(context);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        if (isStarted()) {
            invalidateSelf();
        }
    }

    @Override
    public void start() {

        if (isStarted()) {
            return;
        }

        for (Animator animator : mAnimators) {
            animator.start();
        }
        invalidateSelf();
    }

    private boolean isStarted() {
        for (Animator animator : mAnimators) {
            if (animator.isStarted()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void stop() {
        for (Animator animator : mAnimators) {
            animator.end();
        }
    }

    @Override
    public boolean isRunning() {
        for (Animator animator : mAnimators) {
            if (animator.isRunning()) {
                return true;
            }
        }
        return false;
    }
}