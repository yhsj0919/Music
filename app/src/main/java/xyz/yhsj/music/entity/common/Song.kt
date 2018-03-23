package xyz.yhsj.music.entity.common


data class Song(
        //歌曲名
        var name: String? = null,
        //专辑名
        var albumName: String? = null,
        //来源
        var source: String? = null,
        //歌手
        var singer: String? = null,
        //歌曲Id
        var songId: String? = null,
        //播放地址
        var playUrl: String? = null,
        //歌手封面地址
        var picUrl: String? = null,
        //歌词
        var lrcUrl: String? = null

)