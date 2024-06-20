package com.example.makka_pakka.main.search

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.makka_pakka.model.RoomInfo
import com.example.makka_pakka.model.UserInfo
import com.example.makka_pakka.utils.HttpUtil
import com.example.makka_pakka.utils.GsonUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Callback

class SearchResultViewModel(
) : ViewModel() {

    val errorMsg = MutableLiveData<String>()
    val searchState = MutableLiveData(SearchState.USER)
    val searchKey = MutableLiveData<String>()

    val requestResult = MutableLiveData<List<Any>>()

    fun search(page: Int) {
        // 搜索结果
        Log.d("SearchResultViewModel", "requestResult")
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                HttpUtil.search(
                    searchKey.value!!,
                    page,
                    searchState.value!!.typeCode,
                    object : Callback {
                        override fun onFailure(call: okhttp3.Call, e: java.io.IOException) {
                            Log.d("SearchResultViewModel", "onResponse: $e")
                            viewModelScope.launch {
                                withContext(Dispatchers.Main) {
                                    errorMsg.postValue("网络错误")
                                }
                            }
                        }

                        override fun onResponse(
                            call: okhttp3.Call,
                            response: okhttp3.Response
                        ) {
                            val result = response.body?.string()
                            Log.d("SearchResultViewModel", "onResponse: $result")
                            viewModelScope.launch {
                                try {
                                    withContext(Dispatchers.Main) {
                                        requestResult.postValue(
                                            when (searchState.value!!) {
                                                SearchState.USER -> GsonUtil.fromJsonToListResponse(
                                                    result,
                                                    UserInfo::class.java
                                                ).data

                                                SearchState.ROOM -> GsonUtil.fromJsonToListResponse(
                                                    result,
                                                    RoomInfo::class.java
                                                ).data
                                            }
                                        )
                                    }
                                } catch (e: Exception) {
                                    withContext(Dispatchers.Main) {
                                        errorMsg.postValue(e.message)
                                    }
                                }
                            }
                        }
                    })
            }
//            withContext(Dispatchers.Main) {
//                withContext(Dispatchers.IO) {
//                    delay(1000)
//                }
//                if (page>3){
//                    requestResult.postValue(emptyList())
//                    return@withContext
//                }
//                if (searchKey.value == "搜不到的人" && searchState.value == SearchState.USER) {
//                    requestResult.postValue(emptyList())
//                    return@withContext
//                }
//                requestResult.postValue(
//                    when (searchState.value!!) {
//                        SearchState.USER -> listOf(
//                            MyApplication.instance.testUser,
//                            MyApplication.instance.testUser,
//                            MyApplication.instance.testUser,
//                            MyApplication.instance.testUser,
//                            MyApplication.instance.testUser,
//                            MyApplication.instance.testUser,
//                            MyApplication.instance.testUser,
//                            MyApplication.instance.testUser,
//                            MyApplication.instance.testUser,
//                        )
//
//                        SearchState.ROOM -> listOf(
//                            MyApplication.instance.testRoom,
//                            MyApplication.instance.testRoom,
//                            MyApplication.instance.testRoom,
//                            MyApplication.instance.testRoom,
//                            MyApplication.instance.testRoom,
//                            MyApplication.instance.testRoom,
//                            MyApplication.instance.testRoom,
//                        )
//                    }
//                )
//            }
        }
    }

    enum class SearchState(val typeCode: Int, val value: String, val classType: Class<*>) {
        USER(
            1, "用户", UserInfo::class.java
        ),
        ROOM(
            0, "直播间", RoomInfo::class.java
        )
    }

    companion object {
        val tabList = listOf(SearchState.USER, SearchState.ROOM)
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>, extras: CreationExtras
            ): T {
                val application =
                    checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])
                return SearchResultViewModel(
                ) as T
            }
        }
    }
}