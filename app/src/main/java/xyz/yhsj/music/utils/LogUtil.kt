package xyz.yhsj.music.utils

import android.util.Log

object LogUtil {
    /**
     * 截断输出日志
     *
     * @param msg
     */
    fun e(tag: String = "Log", mMsg: Any?) {
        var msg = mMsg.toString()
        if (tag.isEmpty() || msg.isEmpty())
            return

        val segmentSize = 3 * 1024
        val length = msg.length.toLong()
        if (length <= segmentSize) {// 长度小于等于限制直接打印
            Log.e(tag, msg)
        } else {
            while (msg.length > segmentSize) {// 循环分段打印日志
                val logContent = msg.substring(0, segmentSize)
                msg = msg.replace(logContent, "")
                Log.e(tag, logContent)
            }
            Log.e(tag, msg)// 打印剩余日志
        }
    }
}
