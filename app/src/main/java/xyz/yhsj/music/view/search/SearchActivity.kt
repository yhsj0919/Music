package xyz.yhsj.music.view.search

import android.annotation.SuppressLint
import android.content.Intent
import android.support.annotation.ColorRes
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.support.v7.widget.Toolbar
import android.view.animation.AccelerateInterpolator
import android.widget.Toast
import com.jaeger.library.StatusBarUtil
import com.lzx.musiclibrary.aidl.listener.OnPlayerEventListener
import com.lzx.musiclibrary.aidl.model.SongInfo
import kotlinx.android.synthetic.main.activity_search.*
import xyz.yhsj.kmusic.impl.MusicImpl
import xyz.yhsj.kmusic.site.MusicSite
import xyz.yhsj.music.R
import xyz.yhsj.music.utils.LogUtil
import xyz.yhsj.music.utils.rxSearch
import xyz.yhsj.music.view.base.BaseActivity
import xyz.yhsj.music.view.search.adapter.SearchListAdapter
import com.lzx.musiclibrary.manager.MusicManager
import com.sunfusheng.glideimageview.GlideImageLoader
import top.wefor.circularanim.CircularAnim
import xyz.yhsj.music.utils.toSongInfo
import xyz.yhsj.music.view.play.PlayActivity


@SuppressLint("Registered")
/**
 * 搜索页面
 */
class SearchActivity : BaseActivity(), OnPlayerEventListener {
    override val layoutId: Int = R.layout.activity_search

    override fun getToolbar(): Toolbar? = toolbar

    lateinit var listAdapter: SearchListAdapter

    private var site = MusicSite.QQ

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
        initListener()
    }

    @SuppressLint("SetTextI18n")
    private fun initListener() {
        MusicManager.get().addPlayerEventListener(this)
        action_play.setOnClickListener {
            if (MusicManager.isPlaying()) {
                MusicManager.get().pauseMusic()
            } else {
                MusicManager.get().resumeMusic()
            }
        }

        action_previous.setOnClickListener {
            if (MusicManager.get().hasPre()) {
                MusicManager.get().playPre()
            } else {
                Toast.makeText(this, "没有上一首了", Toast.LENGTH_SHORT).show()
            }
        }

        action_next.setOnClickListener {
            if (MusicManager.get().hasNext()) {
                MusicManager.get().playNext()
            } else {
                Toast.makeText(this, "没有下一首了", Toast.LENGTH_SHORT).show()
            }
        }

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
            song_name.text = "${listAdapter.data[i].title}-${listAdapter.data[i].author}"
            song_album.text = listAdapter.data[i].albumName
            GlideImageLoader.create(song_pic).loadImage(listAdapter.data[i].pic, R.mipmap.ic_launcher)

            val musicList = listAdapter.data.map {
                it.toSongInfo()
            }
            MusicManager.get().playMusic(musicList, i)
        }

        swipeRefreshLayout.setOnRefreshListener { search() }
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

        play_Layout.setOnClickListener {
            CircularAnim.fullActivity(this@SearchActivity, play_Layout)
                    .colorOrImageRes(R.color.color_qq)  //注释掉，因为该颜色已经在App.class 里配置为默认色
                    .deployReturnAnimator { animator ->
                        //this .setDuration() with override CircularAnim.setDuration().
                        animator.interpolator = AccelerateInterpolator()
                    }
                    .go {
                        startActivity(Intent(this, PlayActivity::class.java))
                    }
        }
    }


    override fun onMusicSwitch(music: SongInfo) {
        song_name.text = "${music.songName}-${music.artist}"
        song_album.text = music.albumInfo.albumName
        GlideImageLoader.create(song_pic).loadImage(music.songCover, R.mipmap.ic_launcher)
    }

    override fun onPlayerStart() {
        action_play.text = "停"
    }

    override fun onPlayerPause() {
        action_play.text = "播"
    }

    override fun onPlayCompletion() {
        action_play.text = "播"
    }

    override fun onPlayerStop() {
        action_play.text = "播"
    }

    override fun onError(errorMsg: String?) {
        Toast.makeText(this@SearchActivity, "出错了：$errorMsg", Toast.LENGTH_SHORT).show()
    }

    override fun onAsyncLoading(isFinishLoading: Boolean) {
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

    override fun onDestroy() {
        super.onDestroy()
        MusicManager.get().removePlayerEventListener(this)
    }
}
