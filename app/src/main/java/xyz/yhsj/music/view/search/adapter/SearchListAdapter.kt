package xyz.yhsj.music.view.search.adapter

import android.support.v7.widget.RecyclerView
import xyz.yhsj.adapter.BaseRecyclerViewAdapter
import xyz.yhsj.helper.ViewHolderHelper
import xyz.yhsj.music.R
import xyz.yhsj.music.entity.common.Song


/**
 * 列表适配器
 */
class SearchListAdapter(recyclerView: RecyclerView) : BaseRecyclerViewAdapter<Song>(recyclerView, R.layout.search_list_item) {
    override fun bindData(helper: ViewHolderHelper, index: Int, data: Song) {
        helper.setText(R.id.name, data.name)
        helper.setText(R.id.singer, data.singer + data.albumName.let { " - " + it })

    }
}