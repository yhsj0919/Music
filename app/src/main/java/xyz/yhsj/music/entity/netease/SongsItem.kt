package xyz.yhsj.music.entity.netease

import com.google.gson.annotations.SerializedName

data class SongsItem(

	@field:SerializedName("no")
	val no: Int? = null,

	@field:SerializedName("rt")
	val rt: Any? = null,

	@field:SerializedName("copyright")
	val copyright: Int? = null,

	@field:SerializedName("fee")
	val fee: Int? = null,

	@field:SerializedName("rurl")
	val rurl: Any? = null,

	@field:SerializedName("privilege")
	val privilege: Privilege? = null,

	@field:SerializedName("mst")
	val mst: Int? = null,

	@field:SerializedName("pst")
	val pst: Int? = null,

	@field:SerializedName("pop")
	val pop: Double? = null,

	@field:SerializedName("dt")
	val dt: Int? = null,

	@field:SerializedName("rtype")
	val rtype: Int? = null,

	@field:SerializedName("s_id")
	val sId: Int? = null,

	@field:SerializedName("rtUrls")
	val rtUrls: List<Any?>? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("st")
	val st: Int? = null,

	@field:SerializedName("a")
	val A: Any? = null,

	@field:SerializedName("cd")
	val cd: String? = null,

	@field:SerializedName("publishTime")
	val publishTime: Long? = null,

	@field:SerializedName("cf")
	val cf: String? = null,

	@field:SerializedName("h")
	val H: H? = null,

	@field:SerializedName("mv")
	val mv: Int? = null,

	@field:SerializedName("al")
	val al: Al? = null,

	@field:SerializedName("l")
	val L: L? = null,

	@field:SerializedName("m")
	val M: M? = null,

	@field:SerializedName("cp")
	val cp: Int? = null,

	@field:SerializedName("alia")
	val alia: List<Any?>? = null,

	@field:SerializedName("djId")
	val djId: Int? = null,

	@field:SerializedName("crbt")
	val crbt: Any? = null,

	@field:SerializedName("ar")
	val singer: List<ArItem> = ArrayList(),

	@field:SerializedName("rtUrl")
	val rtUrl: Any? = null,

	@field:SerializedName("ftype")
	val ftype: Int? = null,

	@field:SerializedName("t")
	val T: Int? = null,

	@field:SerializedName("v")
	val V: Int? = null,

	@field:SerializedName("name")
	val name: String? = null
)