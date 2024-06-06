package com.example.makka_pakka.main.home_page

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.makka_pakka.main.mine.MineViewModel
import com.example.makka_pakka.model.LiveInfo
import com.example.makka_pakka.utils.GsonUtil
import com.example.makka_pakka.utils.HttpUtil
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException
import kotlin.random.Random


class MainViewModel(
) : ViewModel() {
    //首页推荐的list
    val recommendList: MutableLiveData<List<LiveInfo>> = MutableLiveData(listOf())
    val recommendListLoading = MutableLiveData(false)

    fun refresh(isMock: Boolean = false) {
        //首页推荐的list
        Log.d("MainViewModel", "refresh: ")
        recommendListLoading.value = true
        if (isMock) //去获取
            viewModelScope.launch(exceptionHandler) {
                withContext(Dispatchers.IO) {
                    HttpUtil.liveList(
                        object : Callback {
                            override fun onFailure(call: Call, e: IOException) {
                                Log.e("MainViewModel", "onFailure: ${e.message}")
                                viewModelScope.launch {
                                    withContext(Dispatchers.Main) {
                                        recommendListLoading.postValue(false)
                                        recommendList.postValue(listOf())
                                    }
                                }
                            }

                            override fun onResponse(call: Call, response: Response) {
                                viewModelScope.launch {
                                    try {
                                        val body = response.body?.string()
                                        Log.d("MainViewModel", "list: $body")
                                        val list =
                                            GsonUtil.fromJsonToMuList(body, LiveInfo::class.java)
                                        withContext(Dispatchers.Main) {
//                                        recommendList.postValue(list)
                                            recommendList.value = list
                                            recommendListLoading.postValue(false)
                                        }
                                    } catch (e: Exception) {
                                        Log.e("MainViewModel", "onResponse: ${e.message}")
                                        recommendList.postValue(listOf())
                                        recommendListLoading.postValue(false)
                                    }
                                }
                            }
                        })
                }
            }
        else {
            //模拟数据
            viewModelScope.launch {
                val list = mutableListOf<LiveInfo>()
                for (i in 0..10) {
                    list.add(
                        LiveInfo(
                            i,
                            "title${
                                Random.nextInt(100)*System.currentTimeMillis()*(i+1) % 10000
                            }",
                            "https://i0.hdslb.com/bfs/archive/05e93bf62c7d1de7e2ce1833eecd497a71ad0b37.jpg",
                            "https://i0.hdslb.com/bfs/archive/05e93bf62c7d1de7e2ce1833eecd497a71ad0b37.jpg",
                            "Alan"
                        )
                    )
                }
                Log.d("MainViewModel", "list: $list")
                recommendList.postValue(list)
                recommendListLoading.postValue(false)
            }
        }


    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>, extras: CreationExtras
            ): T {
                val application =
                    checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])
                return MainViewModel() as T
            }
        }
    }

    //异常处理,处理协程中的异常
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Log.e("LoginAndRegisterViewModel", "doRegister: ${throwable.message}")
    }


}
