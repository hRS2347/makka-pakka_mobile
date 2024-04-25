package com.example.makka_pakka.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.makka_pakka.MyApplication
import com.example.makka_pakka.repo.DataStoreRepository
import com.example.makka_pakka.utils.gson.GsonUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchViewModel(
    private val dataStoreRepository: DataStoreRepository
) : ViewModel() {

    var historySearch =
        dataStoreRepository.getHistorySearch(MyApplication.instance.currentUser.value?.id)
            .map {
                //转为List
                if (it.isNullOrBlank()) {
                    return@map listOf<String>()
                }
                GsonUtil.fromJsonToMuList(it, String::class.java)
            }
            .asLiveData()

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

    val matchedResultList = MutableLiveData<List<String>>(listOf(
        "Apple",
        "Banana",
        "Cherry",
        "Date",
        "Fig",
        "Grape",
        "Kiwi",
        "Lemon",
        "Mango"
    ))


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