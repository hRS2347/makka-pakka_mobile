package com.example.makka_pakka

import android.app.Application
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.MutableLiveData
import com.example.makka_pakka.model.RoomInfo
import com.example.makka_pakka.model.UserInfo
import com.example.makka_pakka.repo.DataStoreRepository
import com.example.makka_pakka.repo.WebViewUrlRepo
import com.example.makka_pakka.utils.FileUtil
import com.example.makka_pakka.utils.HttpUtil
import com.example.makka_pakka.utils.GsonUtil
import com.google.gson.Gson
import com.tencent.smtt.export.external.interfaces.PermissionRequest
import com.tencent.smtt.sdk.QbSdk
import com.tencent.smtt.sdk.WebChromeClient
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Callback


private val Context.dataStore by preferencesDataStore(
    name = "makka-pakka"
)

class MyApplication : Application() {
    val testUser: UserInfo = UserInfo(
        114514,
        "114514@1919.com",
        "PPT之神陶喆",
        "https://i0.hdslb.com/bfs/archive/05e93bf62c7d1de7e2ce1833eecd497a71ad0b37.jpg",
        0,
        "广东省 广州市 天河区",
        "1999-09-09",
        "2021-09-09",
        1,
        "Hey，你好！"
    )
    val testRoom: RoomInfo = RoomInfo(
        1919,
        114514,
        114514,
        "https://i0.hdslb.com/bfs/archive/05e93bf62c7d1de7e2ce1833eecd497a71ad0b37.jpg",
        "PPT之神陶喆发布会",
        "Hey，你好！"
    )

    var currentUser: MutableLiveData<UserInfo?> = MutableLiveData()
    var gson = Gson()


    val webChromeClient = object : WebChromeClient() {
        override fun onPermissionRequest(request: PermissionRequest?) {
            request?.grant(request.resources)
        }
    }

    companion object {
        lateinit var instance: MyApplication
    }

    val dataStoreRepository by lazy {
        DataStoreRepository(
            instance.dataStore
        )
    }

    lateinit var webViewUrlRepo: WebViewUrlRepo


    var currentToken = ""

    @OptIn(DelicateCoroutinesApi::class)
    fun saveToken(token: String) {
        currentToken = token
        GlobalScope.launch {
            Log.d("MyApplication", "saveToken: $token")
            dataStoreRepository.saveToken(token)
        }
    }


    override fun onCreate() {
        super.onCreate()
        instance = this

        QbSdk.setDownloadWithoutWifi(true)
        QbSdk.initX5Environment(applicationContext, object : QbSdk.PreInitCallback {
            override fun onCoreInitFinished() {
                // 内核初始化完成，可能为系统内核，也可能为系统内核
                Log.d("MyApplication", "onCoreInitFinished")
                Log.d("MyApplication", "X5内核版本：" + QbSdk.getTbsVersion(applicationContext))

            }
            /**
             * 预初始化结束
             * 由于X5内核体积较大，需要依赖网络动态下发，所以当内核不存在的时候，默认会回调false，此时将会使用系统内核代替
             * @param isX5 是否使用X5内核
             */
            override fun onViewInitFinished(isX5: Boolean) {
                if(isX5){//true
                    Log.e("腾讯X5", " onViewInitFinished 加载 成功 $isX5");
                }else{
                    Log.e("腾讯X5", " onViewInitFinished 加载 失败！！！使用原生安卓webview $isX5");
                }
            }
        })
        webViewUrlRepo = WebViewUrlRepo(dataStoreRepository)
    }


    fun userInfoChange(userInfo: UserInfo?) {
        if (userInfo == null) return
        HttpUtil.updateUserInfo(userInfo, object : Callback {
            override fun onFailure(call: okhttp3.Call, e: java.io.IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                if (response.code == 200) {
//                    val body = response.body?.string()
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

    fun getUserInfo() {
        GlobalScope.launch {
            HttpUtil.getUserInfo(object : Callback {
                override fun onFailure(call: okhttp3.Call, e: java.io.IOException) {
                    e.printStackTrace()
                }

                override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                    if (response.code == 200) {
                        val body = response.body?.string()
                        Log.i("MyApplication", "new user Info: $body")
                        val myResponse = GsonUtil.fromJsonToResponse(body, UserInfo::class.java)
                        currentUser.postValue(myResponse.data)


                        GlobalScope.launch {
                            dataStoreRepository.setCurrentUser(gson.toJson(myResponse.data))
                        }
                    } else {
                        Log.e("MyApplication", "onResponse: ${response.body?.string()}")
                    }
                }
            })
        }
    }

    fun testAsyncJump(onGet: (Unit) -> Unit) {
        GlobalScope.launch {
            onGet(Unit)
        }
    }

    fun avatarChange(avatar: Uri) {
        val userInfo = currentUser.value ?: return
        HttpUtil.uploadAvatar(FileUtil.getRealPathFromUri(this, avatar)!!, object : Callback {
            override fun onFailure(call: okhttp3.Call, e: java.io.IOException) {
                Log.e("111", e.message.toString())
            }

            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                if (response.code == 200) {
                    getUserInfo()
                    GlobalScope.launch {
                        dataStoreRepository.setCurrentUser(gson.toJson(userInfo))
                    }
                } else {
                    Log.e("MyApplication", "onResponse: ${response.body?.string()}")
                }
            }
        })
    }

    fun logout() {
        GlobalScope.launch {
            currentToken = ""
            withContext(Dispatchers.IO) {
                dataStoreRepository.writeString2DataStore("token", "")
            }
            withContext(Dispatchers.Main) {
                currentUser.postValue(null)
            }
        }
    }
}