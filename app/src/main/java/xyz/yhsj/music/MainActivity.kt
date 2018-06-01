package xyz.yhsj.music

import android.content.Intent
import android.support.design.widget.Snackbar
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.animation.AccelerateInterpolator
import kotlinx.android.synthetic.main.activity_main.*
import top.wefor.circularanim.CircularAnim
import xyz.yhsj.kmusic.impl.MusicImpl
import xyz.yhsj.music.utils.LogUtil
import xyz.yhsj.music.view.base.BaseActivity
import xyz.yhsj.music.view.search.SearchActivity
import kotlin.concurrent.thread

class MainActivity : BaseActivity() {
    override val layoutId: Int = R.layout.activity_main

    override fun getToolbar(): Toolbar? = toolbar

    override fun init() {




        supportActionBar!!.setDisplayHomeAsUpEnabled(false)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()

            thread {

                MusicImpl.search(key = "薛之谦").data?.forEach {

                    LogUtil.e(mMsg = it.code)
                    LogUtil.e(mMsg = it.msg)
                    LogUtil.e(mMsg = it.title)
                    LogUtil.e(mMsg = it.songid)
                    LogUtil.e(mMsg = it.link)
                    LogUtil.e(mMsg = it.author)
                    LogUtil.e(mMsg = it.pic)
                    LogUtil.e(mMsg = it.url)
                    LogUtil.e(mMsg = it.lrc)
                    LogUtil.e(mMsg = "")
                }
            }

//            startActivity(Intent(this, PlayActivity::class.java))
        }
    }

    override fun getMenuId(): Int = R.menu.menu_main

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.search -> {
//               val  searchView = MenuItemCompat.getActionView(item)
//                       LogUtil.e(mMsg = searchView)

                // 先将颜色展出铺满，然后启动新的Activity
                CircularAnim.fullActivity(this@MainActivity, toolbar)
                        .colorOrImageRes(R.color.color_qq)  //注释掉，因为该颜色已经在App.class 里配置为默认色
                        .deployReturnAnimator { animator ->
                            //this .setDuration() with override CircularAnim.setDuration().
                            animator.interpolator = AccelerateInterpolator()
                        }
                        .go {
                            startActivity(Intent(this@MainActivity, SearchActivity::class.java))
                        }


                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}