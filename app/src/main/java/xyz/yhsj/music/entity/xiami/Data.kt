package xyz.yhsj.music.entity.xiami

import com.google.gson.annotations.SerializedName

data class Data(

	@field:SerializedName("next")
	val next: Int? = null,

	@field:SerializedName("total")
	val total: Int? = null,

	@field:SerializedName("previous")
	val previous: Int? = null,

	@field:SerializedName("songs")
	val songs: List<SongsItem>? = null
)