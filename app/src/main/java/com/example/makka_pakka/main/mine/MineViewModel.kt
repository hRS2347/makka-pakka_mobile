package com.example.makka_pakka.main.mine

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.makka_pakka.LOGIN
import com.example.makka_pakka.MyApplication
import com.example.makka_pakka.R
import com.example.makka_pakka.utils.HttpUtil
import com.example.makka_pakka.sound_flex.AudioRecordManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException

class MineViewModel : ViewModel() {

    // 每4s换一个gif
    var currentGestureGifIndex = MutableLiveData(0)
    val errorMsg = MutableLiveData<String>()

    val waveImgUrl = MutableLiveData("")
    fun changeGestureGif() {
        currentGestureGifIndex.value = (currentGestureGifIndex.value!! + 1) % gestureGifList.size
    }

    var guidanceIndex = MutableLiveData(-1) //到达了第几步

    var recordingTime = MutableLiveData(20) //录制的时间,单位为0.1s,总共2s

    var isRecordingOrUploading = MutableLiveData(false) //是否正在录制或上传

    var isRecordDone = MutableLiveData(false) //是否录制完成

    // 开始录制,录制时间为2s,每0.1s更新一次
    fun startRecording() {
        recordingTime.value = -1
        AudioRecordManager.startRecord()
        isRecordingOrUploading.value = true
        viewModelScope.launch {
            delay(300)
            recordingTime.value = 20
            while (recordingTime.value!! > 0) {
                recordingTime.value = recordingTime.value!! - 1
                if (recordingTime.value!! == 0) {
                    AudioRecordManager.stopRecord()
                    //post for pre img
                    HttpUtil.postForPreImg(MyApplication.instance.currentUser.value!!.id!!,
                        AudioRecordManager.recordFile!!,
                        object : Callback {
                            override fun onFailure(call: Call, e: IOException) {
                                viewModelScope.launch {
                                    withContext(Dispatchers.Main) {
                                        isRecordingOrUploading.value = false
                                        errorMsg.postValue(e.message)
                                    }
                                }
                            }

                            override fun onResponse(call: Call, response: Response) {
                                viewModelScope.launch {
                                    withContext(Dispatchers.Main) {
                                        isRecordingOrUploading.value = false
                                        if (response.body?.string() == "ok") {
                                            isRecordDone.value = true
                                            isRecordingOrUploading.value = false
                                            //刷新预览图
                                            waveImgUrl.postValue(
                                                HttpUtil.getWaveImgUrl(
                                                    MyApplication.instance.currentUser.value!!.id!!
                                                )
                                            )
                                        } else {
                                            errorMsg.postValue("录制失败")
                                        }
                                    }
                                }
                            }
                        })

                }
                delay(100)
            }
        }
    }

    fun uploadGesture() {
        isRecordingOrUploading.value = true
        HttpUtil.saveHandGesture(
            MyApplication.instance.currentUser.value!!.id!!,
            AudioRecordManager.recordFile!!,
            object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    viewModelScope.launch {
                        withContext(Dispatchers.Main) {
                            isRecordingOrUploading.value = false
                            errorMsg.postValue(e.message)
                        }
                    }
                }

                override fun onResponse(call: Call, response: Response) {
                    viewModelScope.launch {
                        withContext(Dispatchers.Main) {
                            isRecordingOrUploading.value = false
                            val str = response.body?.string()
                            when (str) {
                                "ok" -> {
                                    guidanceIndex.value = guidanceIndex.value!! + 1
                                }

                                "again" -> {
                                    errorMsg.postValue("存在相似手势，请重新录制")
                                }

                                else -> {
                                    errorMsg.postValue("上传失败")
                                }
                            }
                        }
                    }
                }
            })
    }

    fun loadConfig() {
        isRecordingOrUploading.value = true
        HttpUtil.getHandGestureConfig(
            MyApplication.instance.currentUser.value!!.id!!,
            object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    viewModelScope.launch {
                        withContext(Dispatchers.Main) {
                            isRecordingOrUploading.value = false
                            errorMsg.postValue(e.message)
                        }
                    }
                }

                override fun onResponse(call: Call, response: Response) {
                    viewModelScope.launch {
                        withContext(Dispatchers.Main) {
                            isRecordingOrUploading.value = false
                            try {
                                if (response.code == 200) {
                                    guidanceIndex.value = response.body?.string()?.toInt() ?: 0
                                } else {
                                    errorMsg.postValue("加载失败")
                                }
                            } catch (e: Exception) {
                                errorMsg.postValue("加载失败")
                            }
                        }
                    }
                }
            })
    }

    fun resetGesture() {
        isRecordingOrUploading.value = true
        HttpUtil.clearHandGestureConfig(
            MyApplication.instance.currentUser.value!!.id!!,
            object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    viewModelScope.launch {
                        isRecordingOrUploading.value = false
                        withContext(Dispatchers.Main) {
                            errorMsg.postValue(e.message)
                        }
                    }
                }

                override fun onResponse(call: Call, response: Response) {
                    viewModelScope.launch {
                        withContext(Dispatchers.Main) {
                            isRecordingOrUploading.value = false
                            if (response.body?.string() == "ok") {
                                loadConfig()
                            } else {
                                errorMsg.postValue("重置失败")
                            }
                        }
                    }
                }
            })
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>, extras: CreationExtras
            ): T {
                val application =
                    checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])
                return MineViewModel() as T
            }
        }


        val guidanceList = listOf(
            "录制0号手势，用于关闭控制功能",
            "上下，录制1号手势，该手势用于增大音量（观看），选择下一个（首页）",
            "下上，录制2号手势，该手势用于减小音量（观看），选择上一个（首页）",
            "两圈，录制3号手势，该手势用于返回（观看），刷新列表（首页）",
            "上，录制4号手势，该手势用于提高亮度（观看），进入直播间（首页）",
            "下，录制5号手势，该手势用于降低亮度（观看）,切换页面（首页）",
            "你已完成全部设置"
        )

        val gestureGifList = listOf(
            R.drawable.gesture_1,
            R.drawable.gesture_2,
            R.drawable.gesture_3,
            R.drawable.gesture_4,
            R.drawable.gesture_5
        )

        const val GESTURE_CHANGE_COUNT_IN_SECOND = 4
        const val MSG_INDEX_GESTURE_GIF_CHANGE = 0x1001
    }
}