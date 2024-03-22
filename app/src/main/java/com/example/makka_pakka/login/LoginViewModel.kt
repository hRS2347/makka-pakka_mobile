package com.example.makka_pakka.login

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.CreationExtras
import kotlinx.coroutines.CoroutineExceptionHandler


class LoginViewModel(
) : ViewModel() {
    //email Or Phone
//    val isEmail: MutableLiveData<Boolean> by lazy {
//        MutableLiveData<Boolean>(false)
//    }

    val isLoading: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>(false)
    }

    //异常处理,处理协程中的异常
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Log.e("LoginAndRegisterViewModel", "doRegister: ${throwable.message}")
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>, extras: CreationExtras
            ): T {
                val application = checkNotNull(extras[APPLICATION_KEY])
                return LoginViewModel(
                ) as T
            }
        }
    }
}
