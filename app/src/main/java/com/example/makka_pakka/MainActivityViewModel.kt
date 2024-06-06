package com.example.makka_pakka

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
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

class MainActivityViewModel : ViewModel() {

    val errorMsg = MutableLiveData<String>()

    val isPredictTabOn = MutableLiveData(false)
    val isPredictRunningOn = MutableLiveData(false)
    val isLocked = MutableLiveData(true)
    private var isRecording = false
    var recordingTime = MutableLiveData(20) //录制的时间,单位为0.1s,总共2s
    val predictResult = MutableLiveData<Int>()
    private var lockState = LockState.LOCKED
    val waveImgUrl = MutableLiveData("")
    var selectedIndex = 0


    enum class LockState(val code: Int) {
        //解锁顺序是1-2
        LOCKED(0), FIRST(1), SECOND(2)
    }

    private fun changeLockState(gid: Int) {
        if (gid == 0) { // 识别到锁定手势，直接锁定
            lockState = LockState.LOCKED
            isLocked.postValue(true)
            Log.i(TAG, "lock state: $lockState")
            return
        }
        if (lockState == LockState.LOCKED) {
            if (gid == 1) {
                lockState = LockState.FIRST
            }
        } else if (lockState == LockState.FIRST) {
            lockState = when (gid) {
                2 -> {
                    LockState.SECOND
                }

                1 -> {
                    LockState.FIRST
                }

                else -> {
                    LockState.LOCKED
                }
            }
        }
        Log.i(TAG, "lock state: $lockState")
        isLocked.postValue(lockState != LockState.SECOND)
    }

    fun switchPredictTabState() {
        isPredictTabOn.value = !isPredictTabOn.value!!
        if (!isPredictTabOn.value!!) {
            isPredictRunningOn.value = false
            AudioRecordManager.stopRecord()
        } else {
            isPredictRunningOn.value = true
            runPredict()
        }
    }

    fun switchPredictRunningState() {
        isPredictRunningOn.value = !isPredictRunningOn.value!!
        if (!isPredictRunningOn.value!!) {
            AudioRecordManager.stopRecord()
        } else {
            runPredict()
        }
    }

    fun runPredict() {
        Log.i(TAG, "runPredict")
        if (isRecording || !isPredictRunningOn.value!!) {
            return
        }
        // 0.3s后开始
        viewModelScope.launch {
            // 1. Start recording
            // 2. Wait for 2 second
            recordingTime.value = 23
            isRecording = true
            while (recordingTime.value!! > 0) {
                if (recordingTime.value==21){
                    AudioRecordManager.startRecord()
                }
                recordingTime.value = recordingTime.value!! - 1
                if (recordingTime.value!! == 0) {
                    _runPredict()
                }
                delay(100)
                if (!isPredictRunningOn.value!!) {
                    isRecording = false
                    break
                }
            }
        }
    }

    private fun _runPredict() {
        isRecording = false
        AudioRecordManager.stopRecord()
        //启动预测
        HttpUtil.handGestureRecognize(MyApplication.instance.currentUser.value!!.id!!,
            AudioRecordManager.recordFile!!,
            object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    viewModelScope.launch(Dispatchers.Main) {
                        errorMsg.postValue(e.message)
                        isPredictRunningOn.postValue(false)
                    }
                }

                override fun onResponse(call: Call, response: Response) {
                    viewModelScope.launch(Dispatchers.Main) {
                        try {
                            if (isPredictRunningOn.value == true) {
                                if (response.isSuccessful) {
                                    val result = response.body?.string()
                                    Log.i(TAG, result!!)
                                    changeLockState(result.toInt())
                                    predictResult.postValue(result.toInt())
                                    waveImgUrl.postValue(
                                        HttpUtil.getWaveImgUrl(
                                            MyApplication.instance.currentUser.value!!.id!!
                                        )
                                    )

                                }
                                withContext(Dispatchers.Default) {
                                    delay(1000)  //延迟1s
                                    runPredict() //继续预测
                                }
                            }
                        } catch (e: Exception) {
                            errorMsg.postValue(e.message)
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
                return MainActivityViewModel() as T
            }
        }
        private const val TAG = "MainActivityViewModel"

    }
}