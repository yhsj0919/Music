package xyz.yhsj.music.entity.qq

import com.google.gson.annotations.SerializedName

data class SingerItem(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("name_hilight")
	val nameHilight: String? = null,

	@field:SerializedName("mid")
	val mid: String? = null,

	@field:SerializedName("id")
	val id: Int? = null
)