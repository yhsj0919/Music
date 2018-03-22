package xyz.yhsj.music.entity.netease

import com.google.gson.annotations.SerializedName


data class Result(

	@field:SerializedName("songs")
	val songs: List<SongsItem> = ArrayList(),

	@field:SerializedName("songCount")
	val songCount: Int? = null
)