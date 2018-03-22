package xyz.yhsj.music.entity.xiami

import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken

import java.io.Reader

data class XiamiMusic(

        @field:SerializedName("data")
        val data: Data? = null,

        @field:SerializedName("state")
        val state: Int? = null,

        @field:SerializedName("message")
        val message: String? = null,

        @field:SerializedName("request_id")
        val requestId: String? = null
) {

    class Deserializer : ResponseDeserializable<XiamiMusic> {
        override fun deserialize(reader: Reader): XiamiMusic? {
            return Gson().fromJson(reader, XiamiMusic::class.java)
        }
    }

    class ListDeserializer : ResponseDeserializable<List<XiamiMusic>> {
        override fun deserialize(reader: Reader): List<XiamiMusic> {
            val type = object : TypeToken<List<XiamiMusic>>() {}.type
            return Gson().fromJson(reader, type)
        }
    }
}