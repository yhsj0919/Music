package xyz.yhsj.music.view.search

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.media.MediaPlayer
import android.support.design.widget.Snackbar
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.support.v7.widget.Toolbar
import android.widget.Toast
import com.jaeger.library.StatusBarUtil
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_search.*
import xyz.yhsj.music.R
import xyz.yhsj.music.impl.Impl
import xyz.yhsj.music.impl.NeteaseImpl
import xyz.yhsj.music.impl.QQImpl
import xyz.yhsj.music.impl.XiamiImpl
import xyz.yhsj.music.utils.BroadcastAction
import xyz.yhsj.music.utils.LogUtil
import xyz.yhsj.music.view.base.BaseActivity
import xyz.yhsj.music.view.search.adapter.SearchListAdapter
import java.util.concurrent.TimeUnit


@SuppressLint("Registered")
/**
 * 搜索页面
 */
class SearchActivity : BaseActivity() {

    override val layoutId: Int = R.layout.activity_search

    override fun getToolbar(): Toolbar? = toolbar

    lateinit var listAdapter: SearchListAdapter

    private var musicImpl: Impl = QQImpl

    private var mPlayer: MediaPlayer? = null


    override fun init() {
        supportActionBar!!.title = ""
        StatusBarUtil.setColor(this@SearchActivity, resources.getColor(R.color.color_qq), 0)

        mPlayer = MediaPlayer()

        mPlayer?.setOnErrorListener { mp, what, extra ->

            mp.reset()// 重置
            if (what < 0) {
                Snackbar.make(recyclerView, "播放失败,可能因为该歌曲不存在,或者需要付费,请换个网站试试", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show()
            }
            return@setOnErrorListener true
        }

        //默认刚进去就打开搜索栏
        searchView.isIconified = false

        listAdapter = SearchListAdapter(recyclerView)

        recyclerView.layoutManager = LinearLayoutManager(this)

        recyclerView.adapter = listAdapter

        //添加Android自带的分割线
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        initListener()
    }

    private fun initListener() {
        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.rb_qq -> {
                    toolbarLay.setBackgroundColor(resources.getColor(R.color.color_qq))
                    radioGroup.setBackgroundColor(resources.getColor(R.color.color_qq))
                    StatusBarUtil.setColor(this@SearchActivity, resources.getColor(R.color.color_qq), 0)
                    musicImpl = QQImpl
                    search()
                }
                R.id.rb_netease -> {
                    toolbarLay.setBackgroundColor(resources.getColor(R.color.color_netease))
                    radioGroup.setBackgroundColor(resources.getColor(R.color.color_netease))
                    StatusBarUtil.setColor(this@SearchActivity, resources.getColor(R.color.color_netease), 0)
                    musicImpl = NeteaseImpl
                    search()
                }
                R.id.rb_xiami -> {
                    toolbarLay.setBackgroundColor(resources.getColor(R.color.color_xiami))
                    radioGroup.setBackgroundColor(resources.getColor(R.color.color_xiami))
                    StatusBarUtil.setColor(this@SearchActivity, resources.getColor(R.color.color_xiami), 0)
                    musicImpl = XiamiImpl
                    search()
                }
            }
        }

        listAdapter.setOnItemClickListener { viewGroup, view, i ->
            LogUtil.e("点击的结果", listAdapter.data[i].toString())
            Toast.makeText(this@SearchActivity, "开始播放:${listAdapter.data[i].name}", Toast.LENGTH_SHORT).show()

            val intent = Intent(BroadcastAction.ACTION_OPT_MUSIC_PLAY)
            intent.putExtra(BroadcastAction.ACTION_OPT_MUSIC_PLAY, listAdapter.data[i])
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent)


//            mPlayer?.reset()// 重置
//
//            try {
//                //调用setDataSource方法，传入音频文件的http位置，此时处于Initialized状态
//                mPlayer?.setDataSource(listAdapter.data[i].playUrl)
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//            mPlayer?.prepareAsync()
//
//            Observable.timer(3, TimeUnit.SECONDS)
//                    .subscribeOn(Schedulers.newThread())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe {
//                        mPlayer?.start()
//                    }

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
     *搜索
     */
    @SuppressLint("CheckResult")
    fun search(key: String? = searchView.query.toString()) {

        if (key.isNullOrEmpty()) {
            swipeRefreshLayout.isRefreshing = false
            return
        }
        swipeRefreshLayout.isRefreshing = true
        if (musicImpl == null) {
            musicImpl = QQImpl
        }
        musicImpl.search(key!!)
                .subscribe { t1, t2 ->
                    swipeRefreshLayout.isRefreshing = false
                    listAdapter.data = t1
                }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mPlayer?.isPlaying == true) {
            mPlayer?.stop()
        }
    }

}
