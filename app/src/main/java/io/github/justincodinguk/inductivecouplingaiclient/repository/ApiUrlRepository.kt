package io.github.justincodinguk.inductivecouplingaiclient.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.map

class ApiUrlRepository(private val dataStore: DataStore<Preferences>) {
    val apiUrlPrefsFlow = dataStore.data.map {
        val apiUrl = it[stringPreferencesKey("api_url")] ?: ""
        apiUrl
    }

    suspend fun updateApiUrl(newUrl: String) {
        dataStore.edit {
            it[stringPreferencesKey("api_url")] = newUrl
        }
    }
}