package xyz.yhsj.music

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.animation.AccelerateInterpolator
import com.tbruyelle.rxpermissions2.RxPermissions
import kotlinx.android.synthetic.main.activity_main.*
import top.wefor.circularanim.CircularAnim
import xyz.yhsj.music.view.base.BaseActivity
import xyz.yhsj.music.view.play.PlayActivity
import xyz.yhsj.music.view.search.SearchActivity

class MainActivity : BaseActivity() {
    override val layoutId: Int = R.layout.activity_main

    override fun getToolbar(): Toolbar? = toolbar

    override fun init() {

        supportActionBar!!.setDisplayHomeAsUpEnabled(false)

        fab.setOnClickListener {

            RxPermissions(this)
                    .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .subscribe {
                        if (it) {
                            CircularAnim.fullActivity(this@MainActivity, fab)
                                    .colorOrImageRes(R.color.colorPrimary)  //注释掉，因为该颜色已经在App.class 里配置为默认色
                                    .deployReturnAnimator { animator ->
                                        //this .setDuration() with override CircularAnim.setDuration().
                                        animator.interpolator = AccelerateInterpolator()
                                    }
                                    .go {
                                        startActivity(Intent(this, PlayActivity::class.java))
                                    }
                        } else {
                            AlertDialog.Builder(this)
                                    .setTitle("提示")
                                    .setMessage("app需要开启权限才能使用此功能")
                                    .setPositiveButton("设置") { _, _ ->
                                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                        intent.data = Uri.parse("package:" + this.packageName)
                                        this.startActivity(intent)
                                    }
                                    .setNegativeButton("取消", null)
                                    .create()
                                    .show()
                        }

                    }
        }
    }

    override fun getMenuId(): Int = R.menu.menu_main

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.search -> {
//               val  searchView = MenuItemCompat.getActionView(item)
//                       LogUtil.e(mMsg = searchView)

                RxPermissions(this)
                        .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .subscribe {
                            if (it) {
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

                            } else {
                                AlertDialog.Builder(this)
                                        .setTitle("提示")
                                        .setMessage("app需要开启权限才能使用此功能")
                                        .setPositiveButton("设置") { _, _ ->
                                            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                            intent.data = Uri.parse("package:" + this.packageName)
                                            this.startActivity(intent)
                                        }
                                        .setNegativeButton("取消", null)
                                        .create()
                                        .show()
                            }

                        }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}