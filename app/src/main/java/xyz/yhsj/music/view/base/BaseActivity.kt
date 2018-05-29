package xyz.yhsj.music.view.base

import android.os.Bundle
import android.support.v4.view.WindowCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import xyz.yhsj.music.R

/**
 * BaseActivity
 */
abstract class BaseActivity : AppCompatActivity() {

    /**
     * 返回布局id
     */
    protected abstract val layoutId: Int

    final override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(WindowCompat.FEATURE_ACTION_MODE_OVERLAY)
        setContentView(layoutId)
        val _toolBar = getToolbar()
        if (_toolBar != null) {
            setSupportActionBar(_toolBar)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setHomeAsUpIndicator(R.mipmap.ic_return)
        }
        init()
    }

    /**
     * 返回toolbar
     */
    protected abstract fun getToolbar(): Toolbar?

    /**
     * 初始化功能
     */
    protected abstract fun init()


    open fun getMenuId(): Int = 0

    final override fun onCreateOptionsMenu(menu: Menu): Boolean {

        val menuId = getMenuId()

        return if (menuId != 0) {
            menuInflater.inflate(menuId, menu)
            true
        } else {
            return super.onCreateOptionsMenu(menu)
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onPause() {
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)
        super.onPause()
    }
}
