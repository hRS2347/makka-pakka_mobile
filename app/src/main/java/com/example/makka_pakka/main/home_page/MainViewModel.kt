package com.example.makka_pakka.main.home_page

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
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


class MainViewModel(
) : ViewModel() {
    //首页推荐的list
    val recommendList = MutableLiveData<List<LiveInfo>>()
    val recommendListLoading = MutableLiveData(false)
    fun refresh() {
        //首页推荐的list
        Log.d("MainViewModel", "refresh: ")
        recommendListLoading.value = true
        //去获取
//        viewModelScope.launch(exceptionHandler) {
//            withContext(Dispatchers.IO) {
//                HttpUtil.recommendation(object : Callback {
//                    override fun onFailure(call: Call, e: IOException) {
//                        viewModelScope.launch {
//                            withContext(Dispatchers.Main) {
//                                recommendListLoading.postValue(false)
//                                recommendList.postValue(listOf())
//                            }
//                        }
//                    }
//
//                    override fun onResponse(call: Call, response: Response) {
//                        viewModelScope.launch {
//                            val body = response.body?.string()
//                            Log.d("MainViewModel", "onResponse: $body")
//                            val list =
//                                GsonUtil.fromJsonToMuList(body, LiveInfo::class.java)
//                            withContext(Dispatchers.Main) {
//                                recommendList.postValue(list)
//                                recommendListLoading.postValue(false)
//                            }
//                        }
//                    }
//                })
//            }
//        }
        //模拟数据
        viewModelScope.launch {
            val list = listOf(
                LiveInfo(1, "title1",
                    "https://i0.hdslb.com/bfs/archive/05e93bf62c7d1de7e2ce1833eecd497a71ad0b37.jpg",
                    "name1"),
                LiveInfo(2, "title2",
                    "https://i0.hdslb.com/bfs/archive/05e93bf62c7d1de7e2ce1833eecd497a71ad0b37.jpg",
                    "name2"),
                LiveInfo(3, "title3",
                    "https://i0.hdslb.com/bfs/archive/05e93bf62c7d1de7e2ce1833eecd497a71ad0b37.jpg",
                    "name3"),
                LiveInfo(4, "title4",
                    "https://i0.hdslb.com/bfs/archive/05e93bf62c7d1de7e2ce1833eecd497a71ad0b37.jpg",
                    "name4"),
                LiveInfo(5, "title5",
                    "https://i0.hdslb.com/bfs/archive/05e93bf62c7d1de7e2ce1833eecd497a71ad0b37.jpg",
                    "name5"),
            )
            recommendList.postValue(list)
            recommendListLoading.postValue(false)
        }

    }

    //异常处理,处理协程中的异常
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Log.e("LoginAndRegisterViewModel", "doRegister: ${throwable.message}")
    }


}
