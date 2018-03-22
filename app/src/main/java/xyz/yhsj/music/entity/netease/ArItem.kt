package xyz.yhsj.music.entity.netease

import com.google.gson.annotations.SerializedName

data class ArItem(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("tns")
	val tns: List<Any?>? = null,

	@field:SerializedName("alias")
	val alias: List<Any?>? = null,

	@field:SerializedName("id")
	val id: Int? = null
)