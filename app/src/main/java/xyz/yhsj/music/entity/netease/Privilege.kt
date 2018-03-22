package xyz.yhsj.music.entity.netease


import com.google.gson.annotations.SerializedName


data class Privilege(

	@field:SerializedName("st")
	val st: Int? = null,

	@field:SerializedName("flag")
	val flag: Int? = null,

	@field:SerializedName("subp")
	val subp: Int? = null,

	@field:SerializedName("fl")
	val fl: Int? = null,

	@field:SerializedName("fee")
	val fee: Int? = null,

	@field:SerializedName("dl")
	val dl: Int? = null,

	@field:SerializedName("cp")
	val cp: Int? = null,

	@field:SerializedName("cs")
	val cs: Boolean? = null,

	@field:SerializedName("toast")
	val toast: Boolean? = null,

	@field:SerializedName("maxbr")
	val maxbr: Int? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("pl")
	val pl: Int? = null,

	@field:SerializedName("sp")
	val sp: Int? = null,

	@field:SerializedName("payed")
	val payed: Int? = null
)