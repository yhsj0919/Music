package xyz.yhsj.music.utils

import android.content.Context
import com.lzx.musiclibrary.aidl.model.AlbumInfo
import com.lzx.musiclibrary.aidl.model.SongInfo
import com.lzx.musiclibrary.aidl.model.TempInfo
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import xyz.yhsj.kmusic.entity.MusicResp
import xyz.yhsj.kmusic.entity.Song
import xyz.yhsj.kmusic.impl.MusicImpl
import xyz.yhsj.kmusic.site.MusicSite
import android.util.TypedValue


fun MusicImpl.rxSearch(key: String, page: Int = 1, num: Int = 10, site: MusicSite = MusicSite.QQ) = Observable.create<MusicResp<List<Song>>> {
    it.onNext(this.search(key, page, num, site))
    it.onComplete()
}
        .subscribeOn(Schedulers.newThread())
        .observeOn(AndroidSchedulers.mainThread())


/**
 * 转换播放器支持的对象
 */
fun Song.toSongInfo(): SongInfo {
    val songInfo = SongInfo()
    songInfo.songId = this.songid
    songInfo.songName = this.title
    songInfo.songCover = this.pic
    songInfo.songUrl = this.url
    songInfo.type = this.type
    songInfo.artist = this.author
    songInfo.downloadUrl = this.url
    songInfo.site = this.link
    val albumInfo = AlbumInfo()
    albumInfo.albumName = this.albumName
    songInfo.albumInfo = albumInfo
    val tempInfo = TempInfo()
    tempInfo.temp_1 = this.lrc
    songInfo.tempInfo = tempInfo
    return songInfo
}

fun Context.sp2px(spVal: Float): Int {
    val result = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP, spVal, this.resources.displayMetrics).toInt()
    return result
}