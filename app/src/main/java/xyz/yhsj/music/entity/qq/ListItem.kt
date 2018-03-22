package xyz.yhsj.music.entity.qq


import com.google.gson.annotations.SerializedName

data class ListItem(

        @field:SerializedName("preview")
        val preview: Preview? = null,

        @field:SerializedName("songname_hilight")
        val songnameHilight: String? = null,

        @field:SerializedName("belongCD")
        val belongCD: Int? = null,

        @field:SerializedName("newStatus")
        val newStatus: Int? = null,

        @field:SerializedName("singer")
        val singer: List<SingerItem> = ArrayList(),

        @field:SerializedName("albumname_hilight")
        val albumnameHilight: String? = null,

        @field:SerializedName("lyric_hilight")
        val lyricHilight: String? = null,

        @field:SerializedName("nt")
        val nt: Long? = 2272276937,

        @field:SerializedName("songmid")
        val songmid: String? = null,

        @field:SerializedName("pure")
        val pure: Int? = null,

        @field:SerializedName("type")
        val type: Int? = null,

        @field:SerializedName("chinesesinger")
        val chinesesinger: Int? = null,

        @field:SerializedName("switch")
        val jsonMemberSwitch: Int? = null,

        @field:SerializedName("albumname")
        val albumname: String? = null,

        @field:SerializedName("vid")
        val vid: String? = null,

        @field:SerializedName("stream")
        val stream: Int? = null,

        @field:SerializedName("tag")
        val tag: Int? = null,

        @field:SerializedName("ver")
        val ver: Int? = null,

        @field:SerializedName("isonly")
        val isonly: Int? = null,

        @field:SerializedName("grp")
        val grp: List<Any?>? = null,

        @field:SerializedName("docid")
        val docid: String? = null,

        @field:SerializedName("albummid")
        val albummid: String? = null,

        @field:SerializedName("albumid")
        val albumid: Int? = null,

        @field:SerializedName("msgid")
        val msgid: Int? = null,

        @field:SerializedName("pay")
        val pay: Pay? = null,

        @field:SerializedName("size128")
        val size128: Int? = null,

        @field:SerializedName("sizeflac")
        val sizeflac: Int? = null,

        @field:SerializedName("sizeogg")
        val sizeogg: Int? = null,

        @field:SerializedName("songname")
        val name: String? = null,

        @field:SerializedName("size320")
        val size320: Int? = null,

        @field:SerializedName("strMediaMid")
        val strMediaMid: String? = null,

        @field:SerializedName("media_mid")
        val mediaMid: String? = null,

        @field:SerializedName("t")
        val t: Int? = null,

        @field:SerializedName("lyric")
        val lyric: String? = null,

        @field:SerializedName("sizeape")
        val sizeape: Int? = null,

        @field:SerializedName("pubtime")
        val pubtime: Int? = null,

        @field:SerializedName("interval")
        val interval: Int? = null,

        @field:SerializedName("alertid")
        val alertid: Int? = null,

        @field:SerializedName("cdIdx")
        val cdIdx: Int? = null,

        @field:SerializedName("songid")
        val songid: Int? = null
)