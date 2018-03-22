package xyz.yhsj.music

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*
import xyz.yhsj.music.impl.NeteaseImpl
import xyz.yhsj.music.impl.QQImpl
import xyz.yhsj.music.impl.XiamiImpl
import xyz.yhsj.music.utils.LogUtil
import xyz.yhsj.music.view.search.SearchActivity


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()

            QQImpl.search("难念的经").subscribe { t1, t2 ->
                t1.forEach {
                    LogUtil.e(it.source, it.name + " 歌手：" + it.singer + " 专辑：" + it.albumName)
                }
            }

            NeteaseImpl.search("难念的经")
                    .subscribe { t1, t2 ->
                        t1.forEach {
                            LogUtil.e(it.source, it.name + " 歌手：" + it.singer+ " 专辑：" + it.albumName)
                        }
                    }

            XiamiImpl.search("难念的经")
                    .subscribe { t1, t2 ->
                        t1.forEach {
                            LogUtil.e(it.source, it.name + " 歌手：" + it.singer + " 专辑：" + it.albumName)
                        }
                    }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            R.id.search -> {
                startActivity(Intent(this@MainActivity, SearchActivity::class.java))
                true
            }


            else -> super.onOptionsItemSelected(item)
        }
    }
}