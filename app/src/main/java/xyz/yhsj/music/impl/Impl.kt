package xyz.yhsj.music.impl

import io.reactivex.Single
import xyz.yhsj.music.entity.common.Song

interface Impl {

    fun search(key: String):Single<List<Song>>

    fun getUrl(id: String)

}