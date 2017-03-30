package com.android.zzw.delicacy;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.AttributeSet;
import android.view.View;

import com.github.jorgecastilloprz.FABProgressCircle;

/**
 * Created by zzw on 17/2/25.
 */

public class FloatingActionButtonBehaviour extends CoordinatorLayout.Behavior<FABProgressCircle> {

    public FloatingActionButtonBehaviour(Context context, AttributeSet attrs) {
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, FABProgressCircle child, View dependency) {
        float translationY = Math.min(0, dependency.getTranslationY() - dependency.getHeight());
        child.setTranslationY(translationY);
        return true;
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, FABProgressCircle child, View dependency) {
        return dependency instanceof Snackbar.SnackbarLayout;
    }

}
