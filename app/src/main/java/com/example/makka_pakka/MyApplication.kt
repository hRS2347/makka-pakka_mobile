package com.example.makka_pakka

import android.app.Application
import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.example.makka_pakka.repo.DataStoreRepository
import com.tencent.smtt.sdk.QbSdk

private val Context.dataStore by preferencesDataStore(
    name = "makka-pakka"
)

class MyApplication : Application() {
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
            override fun onViewInitFinished(isX5: Boolean)= Unit
        })
    }

}