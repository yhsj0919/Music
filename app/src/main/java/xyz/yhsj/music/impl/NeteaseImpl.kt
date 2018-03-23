package xyz.yhsj.music.impl

import android.util.Log
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.fuel.rx.rx_responseObject
import com.github.kittinunf.result.Result
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import xyz.yhsj.music.entity.common.Singer
import xyz.yhsj.music.entity.common.Song
import xyz.yhsj.music.entity.netease.NeteaseMusic
import xyz.yhsj.music.utils.LogUtil

object NeteaseImpl : Impl {

    override fun search(key: String): Single<List<Song>> {
        return "http://music.163.com/api/cloudsearch/pc"
                .httpPost(listOf("s" to key, "offset" to 0, "limit" to 10, "type" to 1))
                .header(mapOf(
                        "Accept" to "*/*",
                        "Host" to "music.163.com",
                        "User-Agent" to "curl/7.51.0",
                        "Referer" to "http://music.163.com",
                        "Cookie" to "appver=2.0.2"
                ))
                .rx_responseObject(NeteaseMusic.Deserializer())
                .map { (_, t2) ->
                    return@map when (t2) {
                        is Result.Failure -> {
                            val ex = t2.getException()
                            Log.e("netease", "error", ex)
                            emptyList()
                        }
                        is Result.Success -> {
                            val data = t2.get()
                            data.result?.songs
                        }
                    }
                }
                .map {
                    val songs = it
                            .map {
                                val song = Song()
                                song.name = it.name
                                song.songId = it.id.toString()
                                song.albumName = it.al?.name
                                song.singer = it.singer[0].name
                                song.playUrl = "http://music.163.com/song/media/outer/url?id=${it.id}.mp3"
                                song.picUrl = it.al?.picUrl
                                song.lrcUrl = "http://music.163.com/api/song/lyric?id=${it.id}&lv=1&kv=1&tv=-1"
                                song.source = "netease"

                                song

                            }
                    songs
                }
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
    }

    override fun getUrl(id: String) {
        "http://music.163.com/song/media/outer/url?id=$id.mp3"
    }
}