package xyz.yhsj.music.entity.netease

import com.google.gson.annotations.SerializedName

data class Al(

	@field:SerializedName("picUrl")
	val picUrl: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("tns")
	val tns: List<Any?>? = null,

	@field:SerializedName("pic_str")
	val picStr: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("pic")
	val pic: Long? = null
)