package com.sunofbeaches.looerpager;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class FloatingActionButtonBehavior extends CoordinatorLayout.Behavior<FloatingActionButton> {

    public FloatingActionButtonBehavior(Context context, AttributeSet attrs) {
    }


    // 重写 layoutDependsOn() 方法，并且，如果我们想监听改变，就让方法返回 true。在例子中，我们只想监听 Snackbar 对象的改变。
    @Override
    public boolean layoutDependsOn(@NonNull CoordinatorLayout parent, @NonNull FloatingActionButton child, @NonNull View dependency) {
        return dependency instanceof Snackbar.SnackbarLayout;
    }


    // 现在，让我们继承真正行为的实现。当 CoordinatorLayout 中的 view 每次发生变化时，onDependentViewChanged 方法都会被调用。
    // 此示例中child为: android:id="@+id/fab"
    // 在这个方法中，我们要读取当前 Snackbar 的状态。当Snackbar 显示的时候，我们想把 FAB 也移上来。
    // 要实现这样的目的，我们需要把 FAB 的 Y 坐标设置为Snackbar 的高度。要得到正确的转换值，我们需要从转化的 Y 值中减去 Snackbar 的高度。
    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, FloatingActionButton child, View dependency) {
        float translationY = Math.min(0, dependency.getTranslationY() - dependency.getHeight());
        child.setTranslationY(translationY);
        return true;
    }


}
