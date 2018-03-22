package xyz.yhsj.music.entity.netease


import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import java.io.Reader


data class NeteaseMusic(

        @field:SerializedName("result")
        val result: Result? = null,

        @field:SerializedName("code")
        val code: Int? = null

) {
    class Deserializer : ResponseDeserializable<NeteaseMusic> {
        override fun deserialize(reader: Reader): NeteaseMusic? {
            return Gson().fromJson(reader, NeteaseMusic::class.java)
        }
    }

    class ListDeserializer : ResponseDeserializable<List<NeteaseMusic>> {
        override fun deserialize(reader: Reader): List<NeteaseMusic> {
            val type = object : TypeToken<List<NeteaseMusic>>() {}.type
            return Gson().fromJson(reader, type)
        }
    }
}