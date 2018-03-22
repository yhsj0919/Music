package xyz.yhsj.music.entity.qq


import com.google.gson.annotations.SerializedName

data class Data(

        @field:SerializedName("song")
        val song: Song? = null,

        @field:SerializedName("zhida")
        val zhida: Zhida? = null,

        @field:SerializedName("semantic")
        val semantic: Semantic? = null,

        @field:SerializedName("qc")
        val qc: List<Any?>? = null,

        @field:SerializedName("totaltime")
        val totaltime: Int? = null,

        @field:SerializedName("keyword")
        val keyword: String? = null,

        @field:SerializedName("priority")
        val priority: Int? = null,

        @field:SerializedName("taglist")
        val taglist: List<Any?>? = null
)