package com.data.storage.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.datastore: DataStore<Preferences> by preferencesDataStore(name = "up_data_store")

internal sealed class UpDataStoreKey<T>(val key: Preferences.Key<T>) {
    object RecentQueries : UpDataStoreKey<String>(stringPreferencesKey("recent_queries"))
}


internal object UpDataStore {
    suspend fun <T> write(context: Context, key: UpDataStoreKey<T>, value: T) {
        context.datastore.edit { preferences ->
            preferences[key.key] = value
        }
    }

    fun <T> read(context: Context, key: UpDataStoreKey<T>, default: T): Flow<T> {
        return context.datastore.data.map { preferences ->
            preferences[key.key] ?: default
        }
    }
}
