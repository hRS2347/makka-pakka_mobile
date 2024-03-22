package com.example.makka_pakka.repo

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
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

    fun checkDepressionAttackModelPath(uid: String): Flow<String>{
        val key = ATTACK_PATH_PREFIX + uid
        val flow = _dataStore.data.map {
            it[stringPreferencesKey(key)] ?: ""
        }
        return flow
    }

    companion object {
        private const val ATTACK_PATH_PREFIX = "attack_model_path_"
    }
}