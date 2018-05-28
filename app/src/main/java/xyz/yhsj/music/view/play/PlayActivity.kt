package xyz.yhsj.music.view.play

import android.support.v7.widget.Toolbar
import xyz.yhsj.music.R
import xyz.yhsj.music.view.base.BaseActivity

class PlayActivity : BaseActivity() {

    override val layoutId: Int = R.layout.activity_play

    override fun getToolbar(): Toolbar? = null
    override fun init() {
        val song = intent.getSerializableExtra("datal")

    }

}
