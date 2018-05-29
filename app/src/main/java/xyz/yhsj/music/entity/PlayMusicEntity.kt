package xyz.yhsj.music.entity

import xyz.yhsj.kmusic.entity.Song
import xyz.yhsj.mediabrowser.client.model.IMusicInfo

data class PlayMusicEntity(
        //歌曲id
        var songid: String? = null,
        //来源网站
        var type: String? = null,
        //歌曲地址
        var link: String? = null,
        //歌名
        var songName: String? = null,
        //演唱者
        var author: String? = null,
        //歌词
        var lrc: String? = null,
        //播放地址
        var url: String? = null,
        //封面
        var pic: String? = null,
        //专辑名
        var albumName: String? = null

) : IMusicInfo {

    constructor(song: Song) : this() {

        this.songid = song.songid
        this.type = song.type
        this.link = song.link
        this.songName = song.title
        this.author = song.author
        this.lrc = song.lrc
        this.url = song.url
        this.pic = song.pic
        this.albumName = song.albumName


    }

    override fun getMediaId() = songid

    override fun getSource() = url

    override fun getArtUrl() = pic

    override fun getTitle() = songName

    override fun getDescription() = type

    override fun getArtist() = author

    override fun getAlbum() = albumName

    override fun getAlbumArtUrl() = pic

    override fun getGenre() = type

    override fun freeType() = "1"

    override fun getDuration() = 277L * 1000
}