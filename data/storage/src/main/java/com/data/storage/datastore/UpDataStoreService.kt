package com.data.storage.datastore

import android.content.Context
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object UpDataStoreService {
    private lateinit var appContext: Context

    fun init(context: Context) {
        appContext = context.applicationContext
    }

    var recentQueries: String
        get() = runBlocking {
            UpDataStore.read(appContext, UpDataStoreKey.RecentQueries, "").first()
        }
        set(value) = runBlocking {
            UpDataStore.write(appContext, UpDataStoreKey.RecentQueries, value)
        }
}