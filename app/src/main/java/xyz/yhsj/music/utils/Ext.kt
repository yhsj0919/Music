package xyz.yhsj.music.utils

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import xyz.yhsj.kmusic.entity.MusicResp
import xyz.yhsj.kmusic.entity.Song
import xyz.yhsj.kmusic.impl.MusicImpl
import xyz.yhsj.kmusic.site.MusicSite

fun MusicImpl.rxSearch(key: String, page: Int = 1, num: Int = 10, site: MusicSite = MusicSite.QQ) = Observable.create<MusicResp<List<Song>>> {
    it.onNext(this.search(key, page, num, site))
    it.onComplete()
}
        .subscribeOn(Schedulers.newThread())
        .observeOn(AndroidSchedulers.mainThread())
