package token_storage

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.data.storage.datastore.datastore
import com.domain.entity.LoginResult
import kotlinx.coroutines.flow.first

object TokenStoreService {
    private lateinit var appContext: Context
    private val ACCESS_TOKEN = stringPreferencesKey("access")
    private val REFRESH_TOKEN = stringPreferencesKey("refresh")
//    private val EXPIRE_AT = longPreferencesKey("expires")

    fun init(context: Context) {
        appContext = context.applicationContext
    }

    suspend fun save(loginResult: LoginResult) {
        appContext.datastore.edit { preferences ->
            preferences[ACCESS_TOKEN] = loginResult.accessToken
            preferences[REFRESH_TOKEN] = loginResult.refreshToken
//            preferences[EXPIRE_AT] = System.currentTimeMillis() + loginResult.expiresIn * 1_000
        }
        Log.d("[로그인 끝 토큰저장]", "access: ${loginResult.accessToken} / refresh: ${loginResult.refreshToken} / expire: ${loginResult.expiresIn}")
    }

    suspend fun accessToken(): String {
        return appContext.datastore.data.first()[ACCESS_TOKEN] ?: ""
    }

    suspend fun refreshToken(): String {
        return appContext.datastore.data.first()[REFRESH_TOKEN] ?: ""
    }

    suspend fun clear() {
        appContext.datastore.edit { prefs ->
            prefs.remove(ACCESS_TOKEN)
            prefs.remove(REFRESH_TOKEN)
//            prefs.remove(EXPIRE_AT)
        }
    }

//    suspend fun isAccessTokenValid(): Boolean {
//        return System.currentTimeMillis() < expireAt()
//    }

//    private suspend fun expireAt(): Long {
//        return appContext.datastore.data.first()[EXPIRE_AT] ?: 0
//    }
}