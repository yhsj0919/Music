package xyz.yhsj.music.impl

import android.util.Log
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.rx.rx_responseObject
import com.github.kittinunf.result.Result
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import xyz.yhsj.music.entity.common.Singer
import xyz.yhsj.music.entity.common.Song
import xyz.yhsj.music.entity.xiami.XiamiMusic
import xyz.yhsj.music.utils.LogUtil

object XiamiImpl : Impl {


    override fun search(key: String): Single<List<Song>> {
        return "http://api.xiami.com/web?key=$key&v=2.0&app_key=1&r=search/songs&page=1&limit=10"
                .httpGet()
                .header(mapOf(
                        "Referer" to "http://m.xiami.com/",
                        "User-Agent" to "Mozilla/5.0 (iPhone; CPU iPhone OS 9_1 like Mac OS X) AppleWebKit/601.1.46 (KHTML, like Gecko) Version/9.0 Mobile/13B143 Safari/601.1"
                ))
                .rx_responseObject(XiamiMusic.Deserializer())
                .map { (_, t2) ->
                    return@map when (t2) {
                        is Result.Failure -> {
                            val ex = t2.getException()
                            Log.e("qq", "error", ex)
                            emptyList()
                        }
                        is Result.Success -> {
                            val data = t2.get()
                            data.data?.songs
                        }
                    }
                }
                .map {
                    val songs = it
                            .map {
                                val song = Song()
                                song.name = it.songName
                                song.songId = it.songId.toString()
                                song.albumName = it.albumName
                                song.singer = it.artistName
                                song.playUrl = it.listenFile
                                song.lrcUrl = it.lyric
                                song.picUrl = it.artistLogo
                                song.source = "xiami"
                                song
                            }
                    songs
                }
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
    }

    override fun getUrl(id: String) {
    }

}