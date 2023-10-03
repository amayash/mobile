package com.example.myapplication.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("data_store")

class DataStoreManager(private val context : Context) {
    suspend fun saveSettings(settingData: SettingData) {
        context.dataStore.edit { pref ->
            pref[booleanPreferencesKey("isDarkTheme")] = settingData.isDarkTheme
        }
    }
    fun getSettings() = context.dataStore.data.map { pref ->
        return@map SettingData(
            pref[booleanPreferencesKey("isDarkTheme")] ?: true
        )
    }
}