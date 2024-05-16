package com.example.makka_pakka.repo

import android.util.Log
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class WebViewUrlRepo(
    private val ds: DataStoreRepository
) {
    lateinit var BASE_URL: String
    lateinit var AUDIENCE: String
    lateinit var BROADCAST: String
    lateinit var MY_ROOM: String
    lateinit var ROOM: String

    init {
        GlobalScope.launch {
            getUrl(BASE_URL_KEY)
            getUrl(AUDIENCE_KEY)
            getUrl(BROADCAST_KEY)
            getUrl(MY_ROOM_KEY)
            getUrl(ROOM_KEY)
        }
    }

    suspend fun getUrl(key: String) = ds.readStringFromDataStore(key).firstOrNull().let {
        if(it.isNullOrBlank()) when (key) {
            BASE_URL_KEY -> {
                BASE_URL = BASE_URL_DEFAULT
                BASE_URL_DEFAULT
            }

            AUDIENCE_KEY ->{
                AUDIENCE = AUDIENCE_DEFAULT
                AUDIENCE_DEFAULT
            }

            BROADCAST_KEY ->{
                BROADCAST = BROADCAST_DEFAULT
                BROADCAST_DEFAULT
            }

            MY_ROOM_KEY ->{
                MY_ROOM = MY_ROOM_DEFAULT
                MY_ROOM_DEFAULT
            }

            ROOM_KEY ->{
                ROOM = ROOM_DEFAULT
                ROOM_DEFAULT
            }
            else -> ""
        }
        else {
            when (key) {
                BASE_URL_KEY -> {
                    Log.e("WebViewUrlRepo", "getUrl: $it")
                    BASE_URL = it
                }

                AUDIENCE_KEY ->{
                    AUDIENCE = it
                }

                BROADCAST_KEY ->{
                    BROADCAST = it
                }

                MY_ROOM_KEY ->{
                    MY_ROOM = it
                }

                ROOM_KEY ->{
                    ROOM = it
                }
            }
            it
        }
    }

    suspend fun setUrl(key: String, url: String) {
        ds.writeString2DataStore(key, url)
    }

    val list = listOf(
        BASE_URL_KEY,
        AUDIENCE_KEY,
        BROADCAST_KEY,
        MY_ROOM_KEY,
        ROOM_KEY
    )

    companion object {
        const val BASE_URL_KEY = "BASE_URL_KEY"
        const val AUDIENCE_KEY = "AUDIENCE_KEY"
        const val BROADCAST_KEY = "BROADCAST_KEY"
        const val MY_ROOM_KEY = "MY_ROOM_KEY"
        const val ROOM_KEY = "ROOM_KEY"


        const val BASE_URL_DEFAULT = "https://www.baidu.com"
        const val AUDIENCE_DEFAULT = "https://www.baidu.com"
        const val BROADCAST_DEFAULT = "https://www.baidu.com"
        const val MY_ROOM_DEFAULT = "https://www.baidu.com"
        const val ROOM_DEFAULT = "https://www.baidu.com"

    }
}