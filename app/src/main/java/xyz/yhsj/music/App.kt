package xyz.yhsj.music

import android.animation.Animator
import android.app.Application
import android.content.Context
import android.support.multidex.MultiDex
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import com.lzx.musiclibrary.cache.CacheConfig
import com.lzx.musiclibrary.cache.CacheUtils
import com.lzx.musiclibrary.manager.MusicLibrary
import com.lzx.musiclibrary.notification.NotificationCreater
import com.lzx.musiclibrary.utils.BaseUtil

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


        //配置播放器
        if (!BaseUtil.getCurProcessName(this).contains(":musicLibrary")) {
            //通知栏配置
            val creater = NotificationCreater.Builder()
                    .setTargetClass("xyz.yhsj.music.MainActivity")
                    .setCreateSystemNotification(true)
                    .build()

            //边播边存配置
            val cacheConfig = CacheConfig.Builder()
                    .setOpenCacheWhenPlaying(true)
                    .setCachePath(CacheUtils.getStorageDirectoryPath() + "/YHMusic/Cache/")
                    .build()

            val musicLibrary = MusicLibrary.Builder(this)
                    .setNotificationCreater(creater)
                    .setCacheConfig(cacheConfig)
                    .build()
            musicLibrary.init()
        }
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
}