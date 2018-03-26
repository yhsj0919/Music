package xyz.yhsj.music.service

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.*
import android.support.v4.app.NotificationCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.support.v7.widget.AppCompatSeekBar
import xyz.yhsj.music.R
import xyz.yhsj.music.entity.common.Song
import xyz.yhsj.music.utils.MusicAction
import java.io.IOException
import java.util.Timer
import java.util.TimerTask
import kotlin.collections.ArrayList
import kotlin.collections.List


@SuppressLint("StaticFieldLeak")
/**
 * Created by cuiyang on 16/8/21.
 */

object MediaPlayerHelper {
    private lateinit var mContext: Context
    var mediaPlayer: MediaPlayer? = null
    var playbackState: PlaybackStateCompat? = null
    var mediaSession: MediaSessionCompat? = null
    var mediaController: MediaControllerCompat? = null
    private var playerUpdateListener: MediaPlayerUpdateCallBack? = null
    private val mTimer = Timer() // 计时器
    val songList: ArrayList<Song> = ArrayList()
    private var songIndex: Int = 0

    // 通知渠道组的id.
    private val channelId = "yhmusic_group_01"
    // 用户可见的通知渠道组名称.
    private val channelName = "纯音乐"

    private val handler = Handler(Looper.getMainLooper(), Handler.Callback {
        val position = mediaPlayer!!.currentPosition//当前位置
        val duration = mediaPlayer!!.duration//持续时间
        if (duration > 0) {
            // 计算进度（获取进度条最大刻度*当前音乐播放位置 / 当前音乐时长）

            playerUpdateListener?.onPlaying(position, duration)
        }
        false
    })


    private val mMediaSessionCallback = object : MediaSessionCompat.Callback() {

        override fun onPlayFromSearch(query: String?, extras: Bundle?) {
            //            Uri uri = extras.getParcelable(AudioPlayerService.PARAM_TRACK_URI);
            //            onPlayFromUri(uri, null);
            onPlayFromUri(null, null)
        }

        //播放网络歌曲
        //就是activity和notification的播放的回调方法。都会走到这里进行加载网络音频
        override fun onPlayFromUri(uri: Uri?, extras: Bundle?) {
            if (songIndex >= songList.size || songIndex < 0) {

                playerUpdateListener?.onError(500, "列表为空")

                return
            }
            val song = songList[songIndex]
            try {
                when (playbackState?.state) {
                    PlaybackStateCompat.STATE_PLAYING,
                    PlaybackStateCompat.STATE_PAUSED,
                    PlaybackStateCompat.STATE_NONE -> {
                        MediaPlayerReset()
                        //设置播放地址
                        mediaPlayer?.setDataSource(song.playUrl)
                        //异步进行播放
                        mediaPlayer?.prepareAsync()
                        //设置当前状态为连接中
                        playbackState = PlaybackStateCompat.Builder()
                                .setState(PlaybackStateCompat.STATE_CONNECTING, 0, 1.0f)
                                .build()
                        //告诉MediaSession当前最新的音频状态。
                        mediaSession?.setPlaybackState(playbackState)
                        //设置音频信息；
                        mediaSession?.setMetadata(getMusicEntity(song.name, song.singer, song.albumName))
                    }
                }
            } catch (e: IOException) {
            }

        }

        override fun onPause() {
            when (playbackState!!.state) {
                PlaybackStateCompat.STATE_PLAYING -> {
                    mediaPlayer!!.pause()
                    playbackState = PlaybackStateCompat.Builder()
                            .setState(PlaybackStateCompat.STATE_PAUSED, 0, 1.0f)
                            .build()
                    mediaSession!!.setPlaybackState(playbackState)
                    updateNotification()
                }
            }
        }

        override fun onPlay() {
            when (playbackState!!.state) {
                PlaybackStateCompat.STATE_PAUSED -> {
                    mediaPlayer!!.start()
                    playbackState = PlaybackStateCompat.Builder()
                            .setState(PlaybackStateCompat.STATE_PLAYING, 0, 1.0f)
                            .build()
                    mediaSession!!.setPlaybackState(playbackState)
                    updateNotification()
                }
            }
        }

        //下一曲
        override fun onSkipToNext() {
            super.onSkipToNext()
            if (songIndex < songList.size - 1)
                songIndex++
            else
                songIndex = 0
            onPlayFromUri(null, null)

        }

        //上一曲
        override fun onSkipToPrevious() {
            super.onSkipToPrevious()
            if (songIndex > 0)
                songIndex--
            else
                songIndex = songList.size - 1

            onPlayFromUri(null, null)
        }
    }

    /**
     * 重置player和seekbar进度
     */
    private fun MediaPlayerReset() {
        mediaPlayer!!.reset()
    }


    /**
     * 初始化各服务
     *
     * @param mContext
     */
    fun initService(mContext: Context) {
        this.mContext = mContext


        val notificationManager = MediaPlayerHelper.mContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
            //            channel.enableLights(true); //是否在桌面icon右上角展示小红点
            //            channel.setLightColor(Color.GREEN); //小红点颜色
            //            channel.setShowBadge(true); //是否在久按桌面图标时显示此渠道的通知
            notificationManager.createNotificationChannel(channel)
        }


        //传递播放的状态信息
        playbackState = PlaybackStateCompat.Builder().setState(PlaybackStateCompat.STATE_NONE, 0, 1.0f)
                .build()

        //初始化MediaSessionCompant
        mediaSession = MediaSessionCompat(mContext, MusicAction.SESSION_TAG)
        mediaSession?.setCallback(mMediaSessionCallback)//设置播放控制回调
        //设置可接受媒体控制
        mediaSession?.isActive = true
        mediaSession?.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS or MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS)
        mediaSession?.setPlaybackState(playbackState)//状态回调

        //初始化MediaPlayer
        mediaPlayer = MediaPlayer()
        // 设置音频流类型
        mediaPlayer?.setAudioStreamType(AudioManager.STREAM_MUSIC)

        //播放前的准备动作回调
        mediaPlayer?.setOnPreparedListener {
            it?.start()
            playbackState = PlaybackStateCompat.Builder()
                    .setState(PlaybackStateCompat.STATE_PLAYING, 0, 1.0f)
                    .build()
            mediaSession?.setPlaybackState(playbackState)
            updateNotification()
            playerUpdateListener?.onPrepared(mediaPlayer)

        }
        //缓冲更新
        mediaPlayer?.setOnBufferingUpdateListener { mediaPlayer, percent ->
            playerUpdateListener?.onBufferingUpdate(mediaPlayer, percent)
        }
        /**
         * 完成
         */
        mediaPlayer?.setOnCompletionListener {
            playbackState = PlaybackStateCompat.Builder()
                    .setState(PlaybackStateCompat.STATE_PAUSED, 0, 1.0f)
                    .build()
            mediaSession?.setPlaybackState(playbackState)
            MediaPlayerReset()
            playerUpdateListener?.onCompletion(it)
        }
        /**
         * 错误
         */
        mediaPlayer?.setOnErrorListener { mp, what, extra ->
            playerUpdateListener?.onError(what, null)
            return@setOnErrorListener false
        }


        // 初始化MediaController
        try {
            mediaController = MediaControllerCompat(mContext, mediaSession!!.sessionToken)
        } catch (e: RemoteException) {
            e.printStackTrace()
        }
    }

    fun destoryService() {
        if (mediaPlayer != null) {
            mediaPlayer?.release()
            mediaPlayer = null
        }
        if (mediaSession != null) {
            mediaSession?.release()
            mediaSession = null
        }
    }

    private fun createAction(iconResId: Int, title: String, action: String): NotificationCompat.Action {
        val intent = Intent(mContext, MusicService::class.java)
        intent.action = action
        val pendingIntent = PendingIntent.getService(mContext, 1, intent, 0)
        return NotificationCompat.Action(iconResId, title, pendingIntent)
    }

    /**
     * 更新通知栏
     */
    private fun updateNotification() {
        val playPauseAction = if (playbackState?.state == PlaybackStateCompat.STATE_PLAYING)
            createAction(R.mipmap.img_pause, "Pause", MusicAction.ACTION_PAUSE)
        else
            createAction(R.mipmap.img_play, "Play", MusicAction.ACTION_PLAY)


        val notificationCompat = NotificationCompat.Builder(mContext, channelId)
                .setContentTitle(songList[songIndex].name)
                .setContentText(songList[songIndex].singer)
                .setOngoing(playbackState?.state == PlaybackStateCompat.STATE_PLAYING)
                .setShowWhen(false)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(false)
                .addAction(createAction(R.mipmap.img_last_one_music, "last", MusicAction.ACTION_LAST))
                .addAction(playPauseAction)
                .addAction(createAction(R.mipmap.img_next_music, "next", MusicAction.ACTION_NEXT))
                .setStyle(android.support.v4.media.app.NotificationCompat.MediaStyle()
                        .setMediaSession(mediaSession?.sessionToken)
                        .setShowActionsInCompactView(1, 2))

        val notificationManager222 = mContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager222.notify(1454, notificationCompat.build())

        updateSeekBar()
    }

    /**
     * seekbar每秒更新一格进度
     */
    private fun updateSeekBar() {
        // 计时器每一秒更新一次进度条
        val timerTask = object : TimerTask() {
            override fun run() {
                //mediaPlayer在播放,并且回调不为空
                if (playerUpdateListener != null && mediaPlayer?.isPlaying == true) {
                    this@MediaPlayerHelper.handler.sendEmptyMessage(0) //发送消息
                }
            }
        }
        // 每一秒触发一次
        mTimer.schedule(timerTask, 0, 1000)
    }


    fun setMediaPlayerUpdateListener(listener: MediaPlayerUpdateCallBack) {
        this.playerUpdateListener = listener
    }

    /**
     * 设置通知(mediasession歌曲)信息
     */
    private fun getMusicEntity(musicName: String?, Singer: String?, album: String?): MediaMetadataCompat {
        val mediaMetadataCompat = MediaMetadataCompat.Builder().build()
        mediaMetadataCompat.bundle.putString(MediaMetadataCompat.METADATA_KEY_AUTHOR, Singer)//歌手
        mediaMetadataCompat.bundle.putString(MediaMetadataCompat.METADATA_KEY_ALBUM, album)//专辑
        mediaMetadataCompat.bundle.putString(MediaMetadataCompat.METADATA_KEY_TITLE, musicName)//title
        return mediaMetadataCompat
    }

    /**
     * 回调
     */
    interface MediaPlayerUpdateCallBack {
        /**
         * 播放完成
         */
        fun onCompletion(mediaPlayer: MediaPlayer?)

        /**
         *缓存中
         */
        fun onBufferingUpdate(mediaPlayer: MediaPlayer, percent: Int)

        /**
         *播放中
         */
        fun onPlaying(currentPosition: Int, duration: Int)

        /**
         * 准备
         */
        fun onPrepared(mediaPlayer: MediaPlayer?)

        /**
         * 错误
         */
        fun onError(what: Int, msg: String? = null)
    }

}
