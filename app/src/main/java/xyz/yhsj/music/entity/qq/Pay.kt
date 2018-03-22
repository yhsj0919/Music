package xyz.yhsj.music.entity.qq


import com.google.gson.annotations.SerializedName

data class Pay(

	@field:SerializedName("payplay")
	val payplay: Int? = null,

	@field:SerializedName("payalbum")
	val payalbum: Int? = null,

	@field:SerializedName("paydownload")
	val paydownload: Int? = null,

	@field:SerializedName("paytrackmouth")
	val paytrackmouth: Int? = null,

	@field:SerializedName("paytrackprice")
	val paytrackprice: Int? = null,

	@field:SerializedName("payalbumprice")
	val payalbumprice: Int? = null,

	@field:SerializedName("payinfo")
	val payinfo: Int? = null
)