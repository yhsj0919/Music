package xyz.yhsj.music

import android.content.Intent
import android.support.design.widget.Snackbar
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*
import xyz.yhsj.music.service.MusicService
import xyz.yhsj.music.view.base.BaseActivity
import xyz.yhsj.music.view.search.SearchActivity


class MainActivity : BaseActivity() {
    override val layoutId: Int = R.layout.activity_main

    override fun getToolbar(): Toolbar? = toolbar

    override fun init() {

        startService(Intent(this, MusicService::class.java))

        supportActionBar!!.setDisplayHomeAsUpEnabled(false)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()

        }
    }

    override fun getMenuId(): Int = R.menu.menu_main

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.search -> {
                startActivity(Intent(this@MainActivity, SearchActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}