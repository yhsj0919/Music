package xyz.yhsj.music.entity.qq

import com.google.gson.annotations.SerializedName


data class Semantic(

	@field:SerializedName("curnum")
	val curnum: Int? = null,

	@field:SerializedName("curpage")
	val curpage: Int? = null,

	@field:SerializedName("totalnum")
	val totalnum: Int? = null,

	@field:SerializedName("list")
	val list: List<Any?>? = null
)