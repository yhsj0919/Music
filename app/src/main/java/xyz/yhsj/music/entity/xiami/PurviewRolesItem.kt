package xyz.yhsj.music.entity.xiami

import com.google.gson.annotations.SerializedName


data class PurviewRolesItem(

	@field:SerializedName("operation_list")
	val operationList: List<OperationListItem>? = null,

	@field:SerializedName("quality")
	val quality: String? = null
)