package xyz.yhsj.music.entity.xiami

import com.google.gson.annotations.SerializedName

data class SongsItem(

	@field:SerializedName("listen_file")
	val listenFile: String? = null,

	@field:SerializedName("artist_name")
	val artistName: String? = null,

	@field:SerializedName("artist_logo")
	val artistLogo: String? = null,

	@field:SerializedName("purview_roles")
	val purviewRoles: List<PurviewRolesItem>? = null,

	@field:SerializedName("song_name")
	val songName: String? = null,

	@field:SerializedName("artist_id")
	val artistId: Int? = null,

	@field:SerializedName("demo")
	val demo: Int? = null,

	@field:SerializedName("song_id")
	val songId: Int? = null,

	@field:SerializedName("is_play")
	val isPlay: Int? = null,

	@field:SerializedName("need_pay_flag")
	val needPayFlag: Int? = null,

	@field:SerializedName("lyric")
	val lyric: String? = null,

	@field:SerializedName("album_name")
	val albumName: String? = null,

	@field:SerializedName("play_counts")
	val playCounts: Int? = null,

	@field:SerializedName("album_logo")
	val albumLogo: String? = null,

	@field:SerializedName("album_id")
	val albumId: Int? = null
)