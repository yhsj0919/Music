package xyz.yhsj.music.view.base

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.*

/**
 * BaseFragment
 */

abstract class BaseFragment : Fragment() {

    abstract val layoutId: Int

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(layoutId, container, false)
        init(view)
        return view
    }

    abstract fun init(view: View)

}
