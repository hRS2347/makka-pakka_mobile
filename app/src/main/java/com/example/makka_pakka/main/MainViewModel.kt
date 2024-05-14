package com.example.makka_pakka.main

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.CreationExtras
import kotlinx.coroutines.CoroutineExceptionHandler


class MainViewModel(
) : ViewModel() {
    //首页推荐的list
    val recommendList = MutableLiveData<List<String>>()

    fun refresh() {
        //首页推荐的list
        Log.d("MainViewModel", "refresh: ")
    }

    //异常处理,处理协程中的异常
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Log.e("LoginAndRegisterViewModel", "doRegister: ${throwable.message}")
    }


}
