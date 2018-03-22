package xyz.yhsj.music.entity.qq


import com.google.gson.annotations.SerializedName

data class Zhida(

        @field:SerializedName("type")
        val type: Int? = null,

        @field:SerializedName("chinesesinger")
        val chinesesinger: Int? = null
)