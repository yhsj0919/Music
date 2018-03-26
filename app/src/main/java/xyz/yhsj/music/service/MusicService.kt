package xyz.yhsj.music.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.os.IBinder
import xyz.yhsj.music.utils.MusicAction

/**
 * Created by cuiyang on 16/8/18.
 */

class MusicService : Service() {

    private var mAudioManager: AudioManager? = null

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        //获取audio service
        mAudioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        MediaPlayerHelper.initService(applicationContext)
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent != null && intent.action != null) {
            when (intent.action) {
                MusicAction.ACTION_PLAY -> MediaPlayerHelper.mediaController?.transportControls?.play()
                MusicAction.ACTION_NEXT -> MediaPlayerHelper.mediaController?.transportControls?.skipToNext()
                MusicAction.ACTION_LAST -> MediaPlayerHelper.mediaController?.transportControls?.skipToPrevious()
                MusicAction.ACTION_PAUSE -> MediaPlayerHelper.mediaController?.transportControls?.pause()
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }


    override fun onDestroy() {
        super.onDestroy()
        MediaPlayerHelper.destoryService()
    }
}
