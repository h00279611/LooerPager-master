package net.codepig.trackpoint;

import android.util.Log;
import android.view.View;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class TrackPointAspect {

    @Pointcut("execution(* onClick(..))")
    public void onClickPointcut() {
    }

    @Pointcut("execution(* android.app.Activity+.onCreate(..))")
    public void activityOnCreatePointcut() {
    }

    @Pointcut("execution(* android.app.Activity+.onDestroy(..))")
    public void activityOnDestroyPointcut() {
    }

    @Pointcut("execution(* android.app.Fragment+.onCreate(..))")
    public void fragmentOnCreatePointcut() {
    }

    @Pointcut("execution(* android.support.v4.app.Fragment+.onCreate(..))")
    public void fragmentV4OnCreatePointcut() {
    }

    @Pointcut("execution(* android.app.Fragment+.onDestroy(..))")
    public void fragmentOnDestroyPointcut() {
    }

    @Pointcut("execution(* android.support.v4.app.Fragment+.onDestroy(..))")
    public void fragmentV4OnDestroyPointcut() {
    }

    @Around("onClickPointcut()")
    public void aroundJoinClickPoint(final ProceedingJoinPoint joinPoint) throws Throwable {
        Object target = joinPoint.getTarget();
        String className = "";
        if (target != null) {
            className = target.getClass().getName();
        }
        //获取点击事件view对象及名称，可以对不同按钮的点击事件进行统计
        Object[] args = joinPoint.getArgs();
        if (args.length >= 1 && args[0] instanceof View) {
            View view = (View) args[0];
            int id = view.getId();
            String entryName = view.getResources().getResourceEntryName(id);
            TrackPoint.onClick(className, entryName);
        }
        joinPoint.proceed();//执行原来的代码
    }

    @Around("activityOnCreatePointcut() || fragmentOnCreatePointcut() || fragmentV4OnCreatePointcut()")
    public void aroundJoinPageOpenPoint(final ProceedingJoinPoint joinPoint) throws Throwable {
        Object target = joinPoint.getTarget();
        String className = target.getClass().getName();
        TrackPoint.onPageOpen(className);
        joinPoint.proceed();
    }

    @Around("activityOnDestroyPointcut() || fragmentOnDestroyPointcut() || fragmentV4OnDestroyPointcut()")
    public void aroundJoinPageClosePoint(final ProceedingJoinPoint joinPoint) throws Throwable {
        Object target = joinPoint.getTarget();
        String className = target.getClass().getName();
        TrackPoint.onPageClose(className);
        joinPoint.proceed();
    }
}
