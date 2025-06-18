package com.simplifynow.cryptowatcher.data.local.datastore

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.simplifynow.cryptowatcher.data.local.WalletPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation for WalletPreferences using DataStore
 * This is instantiated via injection and also receives context via injection
 */
@Singleton
class WalletPreferencesDataStoreImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : WalletPreferences {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "wallets")
    private val key = stringPreferencesKey("wallet_pairs")

    override suspend fun saveWalletPairs(walletPairs: List<WalletPreferences.WalletPair>) {
        val jsonString = Json.Default.encodeToString(walletPairs)
        context.dataStore.edit { prefs ->
            prefs[key] = jsonString
        }
    }

    override fun getWalletPairs(): Flow<List<WalletPreferences.WalletPair>> {
        Log.d("getWalletPairs",  "Using DataStore")
        return context.dataStore.data.map { prefs ->
            val jsonString = prefs[key]
            jsonString?.let { Json.Default.decodeFromString<List<WalletPreferences.WalletPair>>(it) } ?: emptyList()
        }
    }

    override suspend fun addWalletPair(pair: WalletPreferences.WalletPair) {
        context.dataStore.edit { prefs ->
            val currentList =
                prefs[key]?.let { Json.Default.decodeFromString<List<WalletPreferences.WalletPair>>(it) }
                    ?: emptyList()
            val updatedList = currentList + pair
            prefs[key] = Json.Default.encodeToString(updatedList)
        }
    }

    override suspend fun removeWalletPair(pair: WalletPreferences.WalletPair) {
        context.dataStore.edit { prefs ->
            val currentList =
                prefs[key]?.let { Json.Default.decodeFromString<List<WalletPreferences.WalletPair>>(it) }
                    ?: emptyList()
            val updatedList = currentList - pair
            prefs[key] = Json.Default.encodeToString(updatedList.toList())
        }
    }
}