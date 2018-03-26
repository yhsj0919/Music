package xyz.yhsj.music.view.play

import android.media.MediaPlayer
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.support.v7.widget.Toolbar
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_play.*
import xyz.yhsj.music.R
import xyz.yhsj.music.entity.common.Song
import xyz.yhsj.music.service.MediaPlayerHelper
import xyz.yhsj.music.utils.LogUtil
import xyz.yhsj.music.utils.MusicAction
import xyz.yhsj.music.view.base.BaseActivity

class PlayActivity : BaseActivity() {

    private var mMediaController: MediaControllerCompat? = null

    override val layoutId: Int = R.layout.activity_play

    override fun getToolbar(): Toolbar? = null
    override fun init() {
        val song = intent.getSerializableExtra(MusicAction.PLAY_DATA)

        if (song is Song) {
            MediaPlayerHelper.songList.add(song)
        }

        //设置媒体播放回键听
        MediaPlayerHelper.setMediaPlayerUpdateListener(object : MediaPlayerHelper.MediaPlayerUpdateCallBack {
            override fun onError(what: Int, msg: String?) {

                LogUtil.e("错误", msg + ">>>>>>>>>")
                Toast.makeText(this@PlayActivity, "播放错误，尝试其他网站播放吧", Toast.LENGTH_LONG).show()

            }

            override fun onPlaying(currentPosition: Int, duration: Int) {
                seekBar.max = duration
                seekBar.progress = currentPosition

            }

            override fun onCompletion(mediaPlayer: MediaPlayer?) {

            }

            override fun onBufferingUpdate(mediaPlayer: MediaPlayer, percent: Int) {
                seekBar.secondaryProgress = percent
            }

            override fun onPrepared(mediaPlayer: MediaPlayer?) {
            }

        })

        mMediaController = MediaControllerCompat(this@PlayActivity, MediaPlayerHelper.mediaSession!!.sessionToken)
        //注册回调
        mMediaController?.registerCallback(object : MediaControllerCompat.Callback() {
            override fun onPlaybackStateChanged(state: PlaybackStateCompat) {
                when (state.state) {
                    PlaybackStateCompat.STATE_NONE//无任何状态
                    -> play.text = "播放"
                    PlaybackStateCompat.STATE_PLAYING -> play.text = "暂停"
                    PlaybackStateCompat.STATE_PAUSED -> play.text = "播放"
                    PlaybackStateCompat.STATE_SKIPPING_TO_NEXT//下一首
                    -> {
                    }
                    PlaybackStateCompat.STATE_SKIPPING_TO_PREVIOUS//上一首
                    -> {
                    }
                    PlaybackStateCompat.STATE_FAST_FORWARDING//快进
                    -> {
                    }
                    PlaybackStateCompat.STATE_REWINDING//快退
                    -> {
                    }
                }
            }
        })

        play.setOnClickListener {
            if (mMediaController?.playbackState?.state == PlaybackStateCompat.STATE_PLAYING) {
                mMediaController?.transportControls?.pause()
            } else if (mMediaController?.playbackState?.state == PlaybackStateCompat.STATE_PAUSED) {
                mMediaController?.transportControls?.play()
            } else {
                mMediaController?.transportControls?.playFromSearch("", null)
            }
        }
        last.setOnClickListener {
            mMediaController?.transportControls?.skipToPrevious()
        }
        next.setOnClickListener {
            mMediaController?.transportControls?.skipToNext()
        }
    }

}
