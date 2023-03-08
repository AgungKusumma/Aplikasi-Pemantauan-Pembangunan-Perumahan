package com.capstoneproject.basnasejahtera.model

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreference private constructor(private val dataStore: DataStore<Preferences>) {

    fun getUser(): Flow<UserModel> {
        return dataStore.data.map { preferences ->
            UserModel(
                preferences[NAME_KEY] ?: "",
                preferences[EMAIL_KEY] ?: "",
                preferences[PASSWORD_KEY] ?: "",
                preferences[ROLE_KEY] ?: "",
                preferences[STATE_KEY] ?: false,
            )
        }
    }

    fun getDataUserKonsumen(): Flow<DataKonsumen> {
        return dataStore.data.map { preferences ->
            DataKonsumen(
                preferences[ID_KONSUMEN] ?: 0,
            )
        }
    }

    suspend fun saveData(role: String) {
        dataStore.edit {
            it[ROLE_KEY] = role
            Log.d("Role", "Account : $role")
        }
    }

    suspend fun saveDataKonsumen(id: Int) {
        dataStore.edit {
            it[ID_KONSUMEN] = id
            Log.d("ID", "Konsumen : $id")
        }
    }

    suspend fun login() {
        dataStore.edit { preferences ->
            preferences[STATE_KEY] = true
        }
    }

    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences[STATE_KEY] = false
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserPreference? = null

        private val NAME_KEY = stringPreferencesKey("name")
        private val EMAIL_KEY = stringPreferencesKey("email")
        private val PASSWORD_KEY = stringPreferencesKey("kata_sandi")
        private val ROLE_KEY = stringPreferencesKey("role")
        private val STATE_KEY = booleanPreferencesKey("state")

        private val ID_KONSUMEN = intPreferencesKey("id")

        fun getInstance(dataStore: DataStore<Preferences>): UserPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}