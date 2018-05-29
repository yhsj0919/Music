package xyz.yhsj.music.view.search

import android.annotation.SuppressLint
import android.content.Intent
import android.media.MediaPlayer
import android.support.annotation.ColorRes
import android.support.design.widget.Snackbar
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.support.v7.widget.Toolbar
import android.widget.Toast
import com.jaeger.library.StatusBarUtil
import kotlinx.android.synthetic.main.activity_search.*
import xyz.yhsj.kmusic.impl.MusicImpl
import xyz.yhsj.kmusic.site.MusicSite
import xyz.yhsj.mediabrowser.client.MusicManager
import xyz.yhsj.music.R
import xyz.yhsj.music.utils.LogUtil
import xyz.yhsj.music.utils.rxSearch
import xyz.yhsj.music.view.base.BaseActivity
import xyz.yhsj.music.view.play.PlayActivity
import xyz.yhsj.music.view.search.adapter.SearchListAdapter
import android.support.v4.media.MediaMetadataCompat
import xyz.yhsj.mediabrowser.client.listener.OnSaveRecordListener
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import xyz.yhsj.music.entity.PlayMusicEntity


@SuppressLint("Registered")
/**
 * 搜索页面
 */
class SearchActivity : BaseActivity() {

    override val layoutId: Int = R.layout.activity_search

    override fun getToolbar(): Toolbar? = toolbar

    lateinit var listAdapter: SearchListAdapter

    private var site = MusicSite.QQ

    var mMusicManager: MusicManager? = null

    private var mIsPlaying: Boolean = false

    override fun init() {
        supportActionBar!!.title = ""
        StatusBarUtil.setColor(this@SearchActivity, resources.getColor(R.color.color_qq), 0)

        //默认刚进去就打开搜索栏
        searchView.isIconified = false

        listAdapter = SearchListAdapter(recyclerView)

        recyclerView.layoutManager = LinearLayoutManager(this)

        recyclerView.adapter = listAdapter

        //添加Android自带的分割线
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))


        if (mMusicManager == null) {
            mMusicManager = MusicManager.getInstance()
        }
        mMusicManager?.init(this)

        initListener()
    }

    private fun initListener() {

        action_play.setOnClickListener {
            if (mIsPlaying) {
                if (mMusicManager != null) {
                    mMusicManager?.pause()
                }
            } else {
                if (mMusicManager != null) {
                    mMusicManager?.play()
                }
            }
        }


        action_next.setOnClickListener {
            if (mMusicManager != null) {
                mMusicManager?.skipToNext()
            }

        }

        action_previous.setOnClickListener {
            if (mMusicManager != null) {
                mMusicManager?.skipToPrevious()
            }

        }


        // 音频变化的监听类
        mMusicManager?.addOnAudioStatusListener(mAudioStatusChangeListener);
        // 记录播放记录的监听
        mMusicManager?.addOnRecorListener(mOnRecordListener);


        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            listAdapter.clear()

            when (checkedId) {
                R.id.rb_qq -> {
                    changeColor(R.color.color_qq)
                    site = MusicSite.QQ
                    search()
                }
                R.id.rb_netease -> {
                    changeColor(R.color.color_netease)
                    site = MusicSite.NETEASE
                    search()
                }
                R.id.rb_xiami -> {
                    changeColor(R.color.color_xiami)
                    site = MusicSite.XIAMI
                    search()
                }
                R.id.rb_baidu -> {
                    changeColor(R.color.color_baidu)
                    site = MusicSite.BAIDU
                    search()
                }
                R.id.rb_kugou -> {
                    changeColor(R.color.color_kugou)
                    site = MusicSite.KUGOU
                    search()
                }

                R.id.rb_kuwo -> {
                    changeColor(R.color.color_kuwo)
                    site = MusicSite.KUWO
                    search()
                }

                R.id.rb_migu -> {
                    changeColor(R.color.color_migu)
                    site = MusicSite.MIGU
                    search()
                }
            }
        }

        listAdapter.setOnItemClickListener { viewGroup, view, i ->

            LogUtil.e("点击的结果", listAdapter.data[i].toString())

            Toast.makeText(this@SearchActivity, "开始播放:${listAdapter.data[i].title}", Toast.LENGTH_SHORT).show()

            val musicList = listAdapter.data.map {
                PlayMusicEntity(it)
            }

            mMusicManager?.playMusicList(musicList, i)

            mMusicManager?.play()

        }

        swipeRefreshLayout.setOnRefreshListener {
            search()
        }
        searchView.setOnCloseListener { true }
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query.isNullOrEmpty()) {
                    Toast.makeText(this@SearchActivity, "请输入查询内容", Toast.LENGTH_SHORT).show()
                } else {
                    search(query!!)
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                return false
            }
        })
    }

    /**
     * 修改页面颜色
     */
    fun changeColor(@ColorRes colorRes: Int) {
        toolbarLay.setBackgroundColor(resources.getColor(colorRes))
        radioGroup.setBackgroundColor(resources.getColor(colorRes))
        StatusBarUtil.setColor(this@SearchActivity, resources.getColor(colorRes), 0)
    }


    /**
     *搜索
     */
    @SuppressLint("CheckResult")
    fun search(key: String? = searchView.query.toString()) {

        if (key.isNullOrEmpty()) {
            swipeRefreshLayout.isRefreshing = false
            return
        }
        swipeRefreshLayout.isRefreshing = true

        MusicImpl.rxSearch(key = key!!, site = site)
                .subscribe {
                    swipeRefreshLayout.isRefreshing = false
                    if (it.code == 200) {
                        listAdapter.data = it.data
                    } else {
                        Toast.makeText(this@SearchActivity, it.msg, Toast.LENGTH_SHORT).show()
                    }
                }
    }


    private fun setControlBg(isPlaying: Boolean) {
        if (isPlaying) {
            action_play.text = "停"
        } else {
            action_play.text = "播"
        }
    }


    /**
     * 音频变化回调
     */
    var mAudioStatusChangeListener: MusicManager.OnAudioStatusChangeListener = object : MusicManager.OnAudioStatusChangeListener {
        override fun onPlaybackStateChanged(state: PlaybackStateCompat) {
            // 播放音频 状态变化
            onMediaPlaybackStateChanged(state)
        }

        override fun onMetadataChanged(metadata: MediaMetadataCompat) {
            // 播放音频变化的回调
            onMediaMetadataChanged(metadata)
        }

        override fun onQueueChanged(queue: List<MediaSessionCompat.QueueItem>) {
            // TODO 播放队列发生变化
        }
    }

    /**
     * 记录播放位置的回调
     */
    var mOnRecordListener: OnSaveRecordListener = OnSaveRecordListener { mediaMetadataCompat, postion ->

        LogUtil.e(mMsg = postion)

        // TODO 保存播放记录用
    }

    /**
     * 音频播放状态变化的回调
     *
     * @param playbackState
     */
    private fun onMediaPlaybackStateChanged(playbackState: PlaybackStateCompat?) {
        if (playbackState == null) {
            return
        }
        // 正在播放
        mIsPlaying = playbackState.state == PlaybackStateCompat.STATE_PLAYING

        // 更新UI
        setControlBg(mIsPlaying)

        /**
         * 设置播放进度
         */
        val progress = playbackState.position.toInt()

        println("播放进度: ${progress}   ${playbackState.playbackSpeed}")

        when (playbackState.state) {
            PlaybackStateCompat.STATE_PLAYING -> {
                println("正在播放")
            }
            PlaybackStateCompat.STATE_PAUSED -> println("暂停播放")
        }
    }


    /**
     * 播放音频数据 发生变化的回调
     *
     * @param mediaMetadata
     */
    private fun onMediaMetadataChanged(mediaMetadata: MediaMetadataCompat?) {
        if (mediaMetadata == null) {
            return
        }
        // 音频的标题
        val title = mediaMetadata.getString(MediaMetadataCompat.METADATA_KEY_TITLE) + "-" +
                // 音频作者
                mediaMetadata.getString(MediaMetadataCompat.METADATA_KEY_ARTIST)

        song_name.text = title

        // 音频专辑
        song_album.text = mediaMetadata.getString(MediaMetadataCompat.METADATA_KEY_ALBUM)

        // 音频图片
        //        mAlbumArtImg.setImageBitmap(MusicLibrary.getAlbumBitmap(
        //                MainActivity.this,
        //                mediaMetadata.getString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID)));

        // 进度条
        val max = mediaMetadata.getLong(MediaMetadataCompat.METADATA_KEY_DURATION).toInt() ?: 0
        println("这是个进度条：$max")
    }


}
