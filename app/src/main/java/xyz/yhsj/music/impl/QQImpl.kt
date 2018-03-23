package xyz.yhsj.music.impl

import android.util.Log
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.rx.rx_responseObject
import com.github.kittinunf.result.Result
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import xyz.yhsj.music.entity.common.Song
import xyz.yhsj.music.entity.qq.QQMusic

object QQImpl : Impl {
    override fun search(key: String): Single<List<Song>> {
        return "https://c.y.qq.com/soso/fcgi-bin/client_search_cp?cr=1&p=1&n=10&format=json&w=$key"
                .httpGet()
                .rx_responseObject(QQMusic.Deserializer())
                .map { (_, t2) ->
                    return@map when (t2) {
                        is Result.Failure -> {
                            val ex = t2.getException()
                            Log.e("qq", "error", ex)
                            emptyList()
                        }
                        is Result.Success -> {
                            val data = t2.get()
                            data.result?.song?.list
                        }
                    }
                }
                .map {
                    val songs = it
                            .map {
                                val song = Song()
                                song.name = it.name
                                song.songId = it.songmid
                                song.albumName = it.albumname
                                song.singer = it.singer[0].name
                                song.playUrl = "http://ws.stream.qqmusic.qq.com/C100${it.songmid}.m4a?fromtag=38"
                                song.picUrl = "https://y.gtimg.cn/music/photo_new/T002R300x300M000${it.albummid}.jpg?max_age=2592000"
                                song.lrcUrl = "http://music.qq.com/miniportal/static/lyric/${(it.songid ?: 0) % 100}/${it.songid}.xml"
                                song.source = "qq"
                                song
                            }
                    songs
                }
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
    }

    override fun getUrl(id: String) {
        "http://ws.stream.qqmusic.qq.com/C100$id.m4a?fromtag=38"
    }
}