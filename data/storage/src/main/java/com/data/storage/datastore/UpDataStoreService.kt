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

    var recentCompanyIDs: String
        get() = runBlocking {
            UpDataStore.read(appContext, UpDataStoreKey.RecentCompanyIDs, "").first()
        }
        set(value) = runBlocking {
            UpDataStore.write(appContext, UpDataStoreKey.RecentCompanyIDs, value)
        }

    var lastKnownLocation: String
        get() = runBlocking {
            UpDataStore.read(appContext, UpDataStoreKey.LastKnownLocation, "").first()
        }
        set(value) = runBlocking {
            UpDataStore.write(appContext, UpDataStoreKey.LastKnownLocation, value)
        }

    var needOnBoardingScene: Boolean
        get() = runBlocking {
            UpDataStore.read(appContext, UpDataStoreKey.NeedOnBoardingScene, true).first()
        }
        set(value) = runBlocking {
            UpDataStore.write(appContext, UpDataStoreKey.NeedOnBoardingScene, value)
        }
}