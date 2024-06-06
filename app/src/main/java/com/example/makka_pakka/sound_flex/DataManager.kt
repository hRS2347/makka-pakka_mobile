package com.example.makka_pakka.sound_flex

import android.util.Log
import com.example.makka_pakka.MyApplication
import com.example.makka_pakka.utils.FileUtil
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.util.UUID

/*
保存录音数据的
*/
object DataManager {
    //    录音的文件名, 实际上是根据UUID随机生成的
    private var key: String? = null

    //    录音文件的输出流
    private var file_output_stream: BufferedOutputStream? = null

    //    录音文件
    var file: File? = null
        private set

    //    录音前准备
    fun prepareSaveRecord() {
//        随机生成文件名
        key = UUID.randomUUID().toString()
        //        利用这个文件名, 以及文件工具file_util, 创建一个pcm文件
        file = File(
            FileUtil.getExternalStoragePath(
                MyApplication.instance.applicationContext,
                "record"
            ) + key + ".pcm"
        )
        //        初始化文件输出流
        try {
            file_output_stream = BufferedOutputStream(FileOutputStream(file, true))
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
    }

    //    往文件中写入字节数据. 来一个写一个
    fun saveRecordBytes(data: ByteArray?) {
        if (file_output_stream == null) {
            Log.e("save record bytes", "输出流未初始化")
            return
        }
        try {
            file_output_stream!!.write(data)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    //    停录. 刷新文件输出流, 并释放
    fun stopRecord() {
        try {
            file_output_stream!!.flush()
            file_output_stream!!.close()
            file_output_stream = null
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}