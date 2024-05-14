package com.example.makka_pakka.main.search

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.makka_pakka.MyApplication
import com.example.makka_pakka.repo.DataStoreRepository
import com.example.makka_pakka.utils.HttpUtil
import com.example.makka_pakka.utils.gson.GsonUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttp
import okhttp3.Response
import okio.IOException

class SearchViewModel(
    private val dataStoreRepository: DataStoreRepository
) : ViewModel() {

    val historySearch =
        dataStoreRepository.getHistorySearch(MyApplication.instance.currentUser.value?.id)
            .map {
                //转为List
                if (it.isNullOrBlank()) {
                    return@map listOf<String>()
                }
                GsonUtil.fromJsonToMuList(it, String::class.java)
            }
            .asLiveData()

    val matchedResult = MutableLiveData<List<String>>()
    val errorMsg = MutableLiveData<String>()

    fun saveHistorySearch(search: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                dataStoreRepository.saveHistorySearch(
                    MyApplication.instance.currentUser.value?.id,
                    search
                )
            }
        }
    }

    fun clearHistorySearch() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                dataStoreRepository.clearHistorySearch(MyApplication.instance.currentUser.value?.id)
            }
        }
    }

    fun getTheMatchedResult(str: String) {
        //clear
        matchedResult.value = listOf()
        HttpUtil.searchMatch(str, object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("SearchViewModel", "getTheMatchedResult", e)
            }

            override fun onResponse(call: Call, response: Response) {
                try {
                    val result = response.body?.string()
                    Log.e("SearchViewModel", "match:"+result.toString())
                    val list =
                        GsonUtil.fromJsonToListResponse(result, String::class.java)
                    viewModelScope.launch(
                        Dispatchers.Main
                    ) {
                        matchedResult.value = list.data
                    }
                } catch (e: Exception) {
                    Log.e("SearchViewModel", "getTheMatchedResult", e)
                    viewModelScope.launch(
                        Dispatchers.Main
                    ) {
                        errorMsg.value = e.message
                    }
                }
            }
        })
//        viewModelScope.launch(
//            Dispatchers.Main
//        ) {
//            matchedResult.value = listOf(
//                "密钥算法hi",
//                "密钥算法hi21asf",
//                "密钥算法hi21asasfe",
//                "asfsadfdsf密钥算法hi21asasfe",
//                "asfsadfdsf密i21asasfe",
//            )
//        }
        Log.d("SearchViewModel", "getTheMatchedResult: $str")
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>, extras: CreationExtras
            ): T {
                val application =
                    checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])
                return SearchViewModel(
                    (application as MyApplication).dataStoreRepository

                ) as T
            }
        }
    }
}