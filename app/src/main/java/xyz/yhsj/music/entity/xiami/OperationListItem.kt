package xyz.yhsj.music.entity.xiami

import com.google.gson.annotations.SerializedName

data class OperationListItem(

	@field:SerializedName("purpose")
	val purpose: Int? = null,

	@field:SerializedName("upgrade_role")
	val upgradeRole: Int? = null
)