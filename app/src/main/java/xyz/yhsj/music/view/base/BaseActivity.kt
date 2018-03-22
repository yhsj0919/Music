package xyz.yhsj.music.view.base

import android.os.Bundle
import android.support.v4.view.WindowCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem

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
            supportActionBar!!.title = ""
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


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
