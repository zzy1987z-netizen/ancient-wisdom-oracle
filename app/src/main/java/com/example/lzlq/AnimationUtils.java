package com.example.lzlq;

import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

public class AnimationUtils {

    /**
     * 对一个视图执行“古卷展开”的动画效果。
     * 动画从Y轴的顶部开始，将视图从0高度展开到其完整高度。
     *
     * @param view 需要执行动画的视图。
     * @param duration 动画持续时间（毫秒）。
     */
    public static void unfoldScroll(View view, long duration) {
        if (view == null) return;
        
        view.setVisibility(View.VISIBLE);
        view.setPivotY(0); // 设置动画的轴心点为视图顶部
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", 0f, 1f);
        scaleY.setDuration(duration);
        scaleY.setInterpolator(new AccelerateDecelerateInterpolator());
        scaleY.start();
    }
}
