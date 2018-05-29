package xyz.yhsj.music

import android.animation.Animator
import android.app.Application
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator

import top.wefor.circularanim.CircularAnim

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        // optional. change the default duration and fullActivity resource.
        // the original value is (618, 618,android.R.color.white).
        CircularAnim.init(100, 300, R.color.colorPrimary)
        // optional. set the default duration OnAnimatorDeployListener.
        CircularAnim.initDefaultDeployAnimators(
                CircularAnim.OnAnimatorDeployListener { animator ->
                    animator.interpolator = AccelerateInterpolator()
                },
                CircularAnim.OnAnimatorDeployListener { animator ->
                    animator.interpolator = DecelerateInterpolator()
                },
                null, null)
    }
}