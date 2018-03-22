package xyz.yhsj.music.entity.netease


import com.google.gson.annotations.SerializedName


data class M(

	@field:SerializedName("br")
	val br: Int? = null,

	@field:SerializedName("fid")
	val fid: Int? = null,

	@field:SerializedName("size")
	val size: Int? = null,

	@field:SerializedName("vd")
	val vd: Double? = null
)