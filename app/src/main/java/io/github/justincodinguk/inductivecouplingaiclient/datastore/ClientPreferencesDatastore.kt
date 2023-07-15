package io.github.justincodinguk.inductivecouplingaiclient.datastore

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore

data class ClientPreferences(val url: String)

private const val NAME = "api_url_prefs"

val Context.dataStore by preferencesDataStore(
    name = NAME
)