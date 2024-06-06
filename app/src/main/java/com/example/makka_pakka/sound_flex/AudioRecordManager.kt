package com.example.makka_pakka.sound_flex


import android.Manifest
import android.content.pm.PackageManager
import android.media.AudioRecord
import android.util.Log
import androidx.core.app.ActivityCompat
import com.example.makka_pakka.MyApplication
import java.io.File

object AudioRecordManager {
    //    系统的录音机
    private var record: AudioRecord? = null

    //    标记是否在录音
    private var is_recording = false


    //    管写入和保存录音数据的
    //    录音文件
    var recordFile: File? = null
        private set

    //    开始录音
    fun startRecord(): Boolean {
//        先创建录音器对象, 同时再检查一下权限
        if (record == null) {
            if (ActivityCompat.checkSelfPermission(
                    MyApplication.instance.applicationContext!!,
                    Manifest.permission.RECORD_AUDIO
                ) != PackageManager.PERMISSION_GRANTED
            ) {
//                ToastUtil.showShort("没给录音权限，我不好办啊")
                Log.e("AudioRecordManager", "no permission")
                return false
            }
            record = AudioRecord(
                AudioRecordConfig.RECORD_SOURCE,
                AudioRecordConfig.RECORD_SAMPLE_RATE /*192000*/,
                AudioRecordConfig.CHANNEL_CONFIG,
                AudioRecordConfig.RECORD_FORMAT,
                4 * AudioRecordConfig.getMinBUfferSize()
            )
            Log.e("exception", "exception")
        }
        if (is_recording) {
            return false
        }
        is_recording = true
        //        先创建一下录音文件
        DataManager.prepareSaveRecord()
        //        开始录音
        record!!.startRecording()
        //        开个子线程采集录音信息
        Thread {
            val data = ByteArray(AudioRecordConfig.getMinBUfferSize())
            var result: Int
            //            只要还在录
            while (is_recording) {
//                只要还能录到东西
                result = record!!.read(data, 0, AudioRecordConfig.getMinBUfferSize())
                //                    接收到不正常的玩意儿
                if (result < 0) {
                    break
                }
                //                就保存起来, 将录到的音(字节)写入到文件中
                DataManager.saveRecordBytes(data)
            }
            Log.d("record", "Ending~~")
        }.start()
        return true
    }

    //    停止录音
    fun stopRecord() {
        try {
            //        更改标记位, 是放系统录音器资源
            is_recording = false
            record!!.stop()
            record!!.release()
            record = null
            //        数据管理器停止写入
            DataManager.stopRecord()
            //        获取录音文件
            recordFile = DataManager.file
        }catch (e: Exception){
            Log.e("exception", "exception")
        }
    }
}