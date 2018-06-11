package xyz.yhsj.music.view.play

import android.support.v7.widget.Toolbar
import android.widget.SeekBar
import android.widget.Toast
import com.jaeger.library.StatusBarUtil
import com.lzx.musiclibrary.aidl.listener.OnPlayerEventListener
import com.lzx.musiclibrary.aidl.model.SongInfo
import com.lzx.musiclibrary.manager.MusicManager
import com.lzx.musiclibrary.manager.TimerTaskManager
import kotlinx.android.synthetic.main.activity_play.*
import me.wcy.lrcview.LrcUtils
import xyz.yhsj.music.R
import xyz.yhsj.music.view.base.BaseActivity


class PlayActivity : BaseActivity(), OnPlayerEventListener {
    lateinit var mTimerTaskManager: TimerTaskManager

    override val layoutId: Int = R.layout.activity_play

    override fun getToolbar(): Toolbar? = toolbar
    override fun init() {
        StatusBarUtil.setTranslucent(this, 0)

        mTimerTaskManager = TimerTaskManager()

        mTimerTaskManager.setUpdateProgressTask(this::updateProgress)


        val music: SongInfo? = MusicManager.get().currPlayingMusic
        supportActionBar?.title = music?.songName ?: ""
        supportActionBar?.subtitle = music?.artist ?: ""

        lrcView.loadLrc(music?.tempInfo?.temp_1?.replace("<[0-9]*>".toRegex(), ""))

        if (MusicManager.isPlaying()) {
            mTimerTaskManager.scheduleSeekBarUpdate()
            btn_play.setImageResource(R.drawable.play_btn_pause_selector)
        }

        initListener()
    }

    fun initListener() {
        btn_play.setOnClickListener {
            if (MusicManager.isPlaying()) {
                MusicManager.get().pauseMusic()
            } else {
                MusicManager.get().resumeMusic()
            }
        }
        btn_prev.setOnClickListener {
            if (MusicManager.get().hasPre()) {
                MusicManager.get().playPre()
            } else {
                Toast.makeText(this, "没有上一首了", Toast.LENGTH_SHORT).show()
            }
        }
        btn_next.setOnClickListener {
            if (MusicManager.get().hasNext()) {
                MusicManager.get().playNext()
            } else {
                Toast.makeText(this, "没有下一首了", Toast.LENGTH_SHORT).show()
            }
        }



        MusicManager.get().addPlayerEventListener(this)
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onStopTrackingTouch(seekBar: SeekBar) {
                MusicManager.get().seekTo(seekBar.progress)
                seekBar.tag = null
            }

            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                seekBar.tag = "????????"
            }
        })

        lrcView.setOnPlayClickListener { time ->
            MusicManager.get().seekTo(time.toInt())
            true
        }
    }


    override fun onMusicSwitch(music: SongInfo) {
        supportActionBar?.title = music.songName ?: ""
        supportActionBar?.subtitle = music.artist ?: ""

        lrcView.loadLrc((music.tempInfo?.temp_1?.replace("<[0-9]*>".toRegex(), "")))
    }

    override fun onPlayerStart() {
        mTimerTaskManager.scheduleSeekBarUpdate()
        btn_play.setImageResource(R.drawable.play_btn_pause_selector)
    }

    override fun onPlayerPause() {
        mTimerTaskManager.stopSeekBarUpdate()
        btn_play.setImageResource(R.drawable.play_btn_play_selector)

    }

    override fun onPlayCompletion() {
        btn_play.setImageResource(R.drawable.play_btn_play_selector)
    }

    override fun onPlayerStop() {
        btn_play.setImageResource(R.drawable.play_btn_play_selector)

    }

    override fun onError(errorMsg: String?) {
        btn_play.setImageResource(R.drawable.play_btn_play_selector)

    }

    override fun onAsyncLoading(isFinishLoading: Boolean) {

    }


    /**
     * 更新进度
     */
    private fun updateProgress() {
        val progress = MusicManager.get().progress
        val bufferProgress = MusicManager.get().bufferedPosition
        if (seekBar.tag == null) {
            seekBar.progress = progress.toInt()
        }
        tv_play_time.text = LrcUtils.formatTime(progress)
        lrcView.updateTime(progress)//传递的数据是播放器的时间格式转化为long数据

        seekBar.max = MusicManager.get().duration
        tv_song_time.text = LrcUtils.formatTime(MusicManager.get().duration.toLong())
    }

    override fun onDestroy() {
        super.onDestroy()
        mTimerTaskManager.onRemoveUpdateProgressTask()
        MusicManager.get().removePlayerEventListener(this)
    }
}
