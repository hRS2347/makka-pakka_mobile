package com.example.makka_pakka.repo

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.makka_pakka.utils.GsonUtil
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import java.io.IOException

class DataStoreRepository(
    private val _dataStore: DataStore<Preferences>
) {
    /**
     * 写入DataStore（String）
     */
    suspend fun writeString2DataStore(key: String, value: String) {
        _dataStore.edit { settings ->
            settings[stringPreferencesKey(key)] = value
        }
        return
    }

    /**
     * 读取DataStore（String）
     */
    suspend fun readStringFromDataStore(key: String): Flow<String?> =
        _dataStore.data.catch {
            Log.e("LoginRepository", "Error reading preferences$it")
            if (it is IOException) {
                emit(emptyPreferences())
            } else {
                throw it
            }
        }.map {
            it[stringPreferencesKey(key)] ?: ""
        }

    suspend fun getCurrentUser(): Flow<String?> {
        return readStringFromDataStore(CURRENT_USER)
    }

    suspend fun setCurrentUser(user: String) {
        writeString2DataStore(CURRENT_USER, user)
    }

    // 读取token,外层调用时不用协程
    val tokenFlow = _dataStore.data.catch {
        if (it is IOException) {
            Log.e("DataStoreRepository", "Error reading preferences$it")
            emit(emptyPreferences())
        } else {
            throw it
        }
    }.map {
        it[stringPreferencesKey(TOKEN)] ?: ""
    }

    // 写入token
    suspend fun saveToken(token: String) {
        _dataStore.edit { settings ->
            settings[stringPreferencesKey(TOKEN)] = token
        }
    }

    fun getHistorySearch(uid: Int?): Flow<String?> {
        val key = HISTORY_SEARCH + uid
        val flow = _dataStore.data.catch {
            Log.e("LoginRepository", "Error reading preferences$it")
            if (it is IOException) {
                emit(emptyPreferences())
            } else {
                throw it
            }
        }.map {
            it[stringPreferencesKey(key)] ?: ""
        }
        return flow
    }

    suspend fun saveHistorySearch(id: Int?, search: String) {
        try {
            val key = HISTORY_SEARCH + id
            //存一个List 的json
            //先取出
            val old = getHistorySearch(id).firstOrNull() ?: ""
            //拼接
            val list = if (old.isEmpty()) {
                mutableListOf()
            } else {
                GsonUtil.fromJsonToMuList(old, String::class.java)
            } as MutableList
            val newList = mutableListOf<String>()
            //如果已经存在，先删除
            if (list.contains(search)) {
                list.remove(search)
            }
            newList.add(search)
            newList.addAll(list)
            writeString2DataStore(key, Gson().toJson(newList))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun clearHistorySearch(id: Int?) {
        val key = HISTORY_SEARCH + id
        writeString2DataStore(key, "")
    }

    companion object {
        private const val CURRENT_USER = "user"
        private val TOKEN = "token"
        private const val HISTORY_SEARCH = "history_search"
    }
}