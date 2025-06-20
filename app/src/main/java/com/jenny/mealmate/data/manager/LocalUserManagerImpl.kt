package com.jenny.mealmate.data.manager

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.jenny.mealmate.domain.manager.LocalUserManager
import com.jenny.mealmate.util.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LocalUserManagerImpl(
    private val context: Context
) : LocalUserManager {
    override suspend fun saveAppEntry() {
        context.dataStore.edit {
            settings -> settings[PreferencesKeys.APP_ENTRY] = true
        }
    }

    override fun readAppEntry(): Flow<Boolean> {
        return context.dataStore.data.map { preferences ->
            preferences[PreferencesKeys.APP_ENTRY]?: false
        }
    }

    override suspend fun saveUserAuth(id: String, name: String, email: String, token: String, createdAt: String) {
        context.dataStore.edit {
            it[PreferencesKeys.USER_ID] = id
            it[PreferencesKeys.USER_NAME] = name
            it[PreferencesKeys.USER_EMAIL] = email
            it[PreferencesKeys.USER_TOKEN] = token
            it[PreferencesKeys.USER_CREATED_AT] = createdAt
        }
    }

    override fun readIsUserLoggedIn(): Flow<Boolean> {
        return context.dataStore.data.map { it[PreferencesKeys.USER_TOKEN] != null }
    }

    override suspend fun clearUserAuth() {
        context.dataStore.edit { it.clear() }
    }
}

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = Constants.USER_SETTINGS_PREF)

private object PreferencesKeys {
    val APP_ENTRY = booleanPreferencesKey(name = Constants.APP_ENTRY_PREF_KEY)
    val USER_ID = stringPreferencesKey(name = Constants.USER_ID_PREF_KEY)
    val USER_NAME = stringPreferencesKey(name = Constants.USER_NAME_PREF_KEY)
    val USER_CREATED_AT = stringPreferencesKey(name = Constants.USER_CREATED_AT)
    val USER_EMAIL = stringPreferencesKey(name = Constants.USER_EMAIL_PREF_KEY)
    val USER_TOKEN = stringPreferencesKey(name = Constants.USER_TOKEN_PREF_KEY)
}