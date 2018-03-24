package xyz.yhsj.music.service

import android.annotation.SuppressLint
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.MediaPlayer
import android.os.IBinder
import android.support.design.widget.Snackbar
import android.support.v4.content.LocalBroadcastManager
import android.widget.Toast
import com.cantrowitz.rxbroadcast.RxBroadcast
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_search.*


import java.io.IOException
import java.util.ArrayList

import xyz.yhsj.music.entity.common.Song
import xyz.yhsj.music.utils.BroadcastAction
import xyz.yhsj.music.utils.LogUtil
import java.util.concurrent.TimeUnit

/**
 * Created by AchillesL on 2016/11/18.
 */

class MusicService : Service() {

    private var mCurrentMusicIndex = 0
    private var mIsMusicPause = false
    private val mMusicDatas = ArrayList<Song>()

    private var mMediaPlayer: MediaPlayer? = MediaPlayer()

    private var broadcast: Disposable? = null

    private var timer: Disposable? = null


    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        initBoardCastReceiver()

        mMediaPlayer!!.setOnCompletionListener {
            LogUtil.e("MusicService", "播放完成")
            timer?.dispose()
            sendMusicCompleteBroadCast()
        }

        mMediaPlayer?.setOnErrorListener { mp, what, extra ->
            timer?.dispose()

            LogUtil.e("MusicService", "出错了")
            mp.reset()// 重置
            if (what < 0) {
                Toast.makeText(this, "播放失败,可能因为该歌曲不存在,或者需要付费,请换个网站试试", Toast.LENGTH_LONG).show()
            }
            return@setOnErrorListener true
        }
    }

    @SuppressLint("CheckResult")
    private fun initBoardCastReceiver() {
        val intentFilter = IntentFilter()

        intentFilter.addAction(BroadcastAction.ACTION_OPT_MUSIC_PLAY)
        intentFilter.addAction(BroadcastAction.ACTION_OPT_MUSIC_PAUSE)
        intentFilter.addAction(BroadcastAction.ACTION_OPT_MUSIC_NEXT)
        intentFilter.addAction(BroadcastAction.ACTION_OPT_MUSIC_LAST)
        intentFilter.addAction(BroadcastAction.ACTION_OPT_MUSIC_SEEK_TO)

        broadcast = RxBroadcast.fromLocalBroadcast(this, intentFilter)
                .subscribe { intent ->
                    val action = intent.action
                    when (action) {
                        BroadcastAction.ACTION_OPT_MUSIC_PLAY -> {

                            val song = intent.getSerializableExtra(BroadcastAction.ACTION_OPT_MUSIC_PLAY)
                            if (song is Song) {
                                mMusicDatas.add(song)
                            }
                            if (song != null) {
                                play(mMusicDatas.lastIndex)
                            } else {
                                play(mCurrentMusicIndex)
                            }
                        }
                        BroadcastAction.ACTION_OPT_MUSIC_PAUSE -> pause()
                        BroadcastAction.ACTION_OPT_MUSIC_LAST -> last()
                        BroadcastAction.ACTION_OPT_MUSIC_NEXT -> next()
                        BroadcastAction.ACTION_OPT_MUSIC_SEEK_TO -> seekTo(intent)
                    }
                }
    }

    /**
     *  播放
     */
    @SuppressLint("CheckResult")
    private fun play(index: Int) {
        if (index >= mMusicDatas.size) return
        if (mCurrentMusicIndex == index && mIsMusicPause) {
            mMediaPlayer!!.start()
        } else {
            mMediaPlayer?.reset()// 重置

            try {
                mMediaPlayer!!.setDataSource(mMusicDatas[index].playUrl)
            } catch (e: IOException) {
                e.printStackTrace()
            }
            mMediaPlayer!!.prepareAsync()

            mCurrentMusicIndex = index
            mIsMusicPause = false

            Observable.timer(3, TimeUnit.SECONDS)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        mMediaPlayer?.start()
                        val duration = mMediaPlayer!!.duration
                        sendMusicDurationBroadCast(duration)
                    }
        }

        timer = Observable
                .interval(1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {

                    LogUtil.e("MusicService", "时间： " + mMediaPlayer?.duration + ">>>>>" + mMediaPlayer?.currentPosition)
                }

        sendMusicStatusBroadCast(BroadcastAction.ACTION_STATUS_MUSIC_PLAY)
    }

    /**
     * 暂停
     */
    private fun pause() {
        mMediaPlayer!!.pause()
        mIsMusicPause = true
        sendMusicStatusBroadCast(BroadcastAction.ACTION_STATUS_MUSIC_PAUSE)
    }

    /**
     * 停止
     */
    private fun stop() {
        mMediaPlayer!!.stop()
    }

    /**
     * 下一首
     */
    private operator fun next() {
        if (mCurrentMusicIndex + 1 < mMusicDatas.size) {
            play(mCurrentMusicIndex + 1)
        } else {
            stop()
        }
    }

    /**
     * 上一首
     */
    private fun last() {
        if (mCurrentMusicIndex != 0) {
            play(mCurrentMusicIndex - 1)
        }
    }

    /**
     * 跳转到
     */
    private fun seekTo(intent: Intent) {
        if (mMediaPlayer!!.isPlaying) {
            val position = intent.getIntExtra(BroadcastAction.PARAM_MUSIC_SEEK_TO, 0)
            mMediaPlayer!!.seekTo(position)
        }
    }

    /**
     * 音乐完成
     */
    private fun sendMusicCompleteBroadCast() {
        val intent = Intent(BroadcastAction.ACTION_STATUS_MUSIC_COMPLETE)
        intent.putExtra(BroadcastAction.PARAM_MUSIC_IS_OVER, mCurrentMusicIndex == mMusicDatas.size - 1)
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
    }

    /**
     * 音乐总时间
     */
    private fun sendMusicDurationBroadCast(duration: Int) {
        val intent = Intent(BroadcastAction.ACTION_STATUS_MUSIC_DURATION)
        intent.putExtra(BroadcastAction.PARAM_MUSIC_DURATION, duration)
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
    }

    /**
     * 音乐当前时间
     */
    private fun sendMusicStatusBroadCast(action: String) {
        val intent = Intent(action)
        if (action == BroadcastAction.ACTION_STATUS_MUSIC_PLAY) {
            intent.putExtra(BroadcastAction.PARAM_MUSIC_CURRENT_POSITION, mMediaPlayer!!.currentPosition)
        }
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
    }


    override fun onDestroy() {
        super.onDestroy()
        mMediaPlayer!!.release()
        mMediaPlayer = null
        if (broadcast != null) {
            broadcast?.dispose()
        }
    }

}
