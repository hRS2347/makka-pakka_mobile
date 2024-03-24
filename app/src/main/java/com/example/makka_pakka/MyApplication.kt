package com.example.makka_pakka

import android.app.Application
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.MutableLiveData
import androidx.navigation.Navigation
import com.example.makka_pakka.model.UserInfo
import com.example.makka_pakka.repo.DataStoreRepository
import com.example.makka_pakka.utils.HttpUtil
import com.google.gson.Gson
import com.tencent.smtt.sdk.QbSdk
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.Callback

private val Context.dataStore by preferencesDataStore(
    name = "makka-pakka"
)

class MyApplication : Application() {
    var currentUser: MutableLiveData<UserInfo?> = MutableLiveData()
    var gson = Gson()

    companion object {
        lateinit var instance: MyApplication
    }

    val dataStoreRepository by lazy {
        DataStoreRepository(
            instance.dataStore
        )
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        QbSdk.initX5Environment(applicationContext, object : QbSdk.PreInitCallback {
            override fun onCoreInitFinished() {
                // 内核初始化完成，可能为系统内核，也可能为系统内核
            }

            /**
             * 预初始化结束
             * 由于X5内核体积较大，需要依赖网络动态下发，所以当内核不存在的时候，默认会回调false，此时将会使用系统内核代替
             * @param isX5 是否使用X5内核
             */
            override fun onViewInitFinished(isX5: Boolean) = Unit
        })
    }

    suspend fun initUserInfo(): Boolean {
        val query = dataStoreRepository.getCurrentUser().first()
        return if (query.isNullOrBlank() or query.isNullOrEmpty()) {
            currentUser.value = null
            false
        } else {
            currentUser.value = gson.fromJson(query, UserInfo::class.java)
            true
        }
    }

    fun userInfoChange(userInfo: UserInfo?) {
        if (userInfo == null) return
        HttpUtil.updateUserInfo(userInfo, object : Callback {
            override fun onFailure(call: okhttp3.Call, e: java.io.IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                if (response.code == 200) {
                    val body = response.body?.string()
                    currentUser.postValue(userInfo)
                    GlobalScope.launch {
                        dataStoreRepository.setCurrentUser(gson.toJson(userInfo))
                    }
                } else {
                    Log.e("MyApplication", "onResponse: ${response.body?.string()}")
                }
            }
        })
    }

    fun avatarChange(avatar: Uri) {
        val userInfo = currentUser.value ?: return
        HttpUtil.uploadAvatar(avatar.path!!, object : Callback {
            override fun onFailure(call: okhttp3.Call, e: java.io.IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                if (response.code == 200) {
                    val body = response.body?.string()
                    userInfo.avatarUrl = body
                    currentUser.postValue(userInfo)
                    GlobalScope.launch {
                        dataStoreRepository.setCurrentUser(gson.toJson(userInfo))
                    }
                } else {
                    Log.e("MyApplication", "onResponse: ${response.body?.string()}")
                }
            }
        })
    }

}