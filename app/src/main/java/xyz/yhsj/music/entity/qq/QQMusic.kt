package xyz.yhsj.music.entity.qq

import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import java.io.Reader

data class QQMusic(

        @field:SerializedName("code")
        val code: Int? = null,

        @field:SerializedName("data")
        val result: Data? = null,

        @field:SerializedName("subcode")
        val subcode: Int? = null,

        @field:SerializedName("time")
        val time: Int? = null,

        @field:SerializedName("message")
        val message: String? = null,

        @field:SerializedName("tips")
        val tips: String? = null,

        @field:SerializedName("notice")
        val notice: String? = null
) {

    class Deserializer : ResponseDeserializable<QQMusic> {
        override fun deserialize(reader: Reader): QQMusic? {
            return Gson().fromJson(reader, QQMusic::class.java)
        }
    }

    class ListDeserializer : ResponseDeserializable<List<QQMusic>> {
        override fun deserialize(reader: Reader): List<QQMusic> {
            val type = object : TypeToken<List<QQMusic>>() {}.type
            return Gson().fromJson(reader, type)
        }
    }
}