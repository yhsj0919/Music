package xyz.yhsj.music.entity.qq

import com.google.gson.annotations.SerializedName

data class Preview(

	@field:SerializedName("tryend")
	val tryend: Int? = null,

	@field:SerializedName("trybegin")
	val trybegin: Int? = null,

	@field:SerializedName("trysize")
	val trysize: Int? = null
)