package com.simplifynow.cryptowatcher.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
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
class WalletPreferencesImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : WalletPreferences {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "wallets")
    private val key = stringPreferencesKey("wallet_pairs")

    override suspend fun saveWalletPairs(walletPairs: List<WalletPreferences.WalletPair>) {
        val jsonString = Json.encodeToString(walletPairs)
        context.dataStore.edit { prefs ->
            prefs[key] = jsonString
        }
    }

    override fun getWalletPairs(): Flow<List<WalletPreferences.WalletPair>> {
        return context.dataStore.data.map { prefs ->
            val jsonString = prefs[key]
            jsonString?.let { Json.decodeFromString<List<WalletPreferences.WalletPair>>(it) } ?: emptyList()
        }
    }

    override suspend fun addWalletPair(pair: WalletPreferences.WalletPair) {
        context.dataStore.edit { prefs ->
            val currentList =
                prefs[key]?.let { Json.decodeFromString<List<WalletPreferences.WalletPair>>(it) }
                    ?: emptyList()
            val updatedList = currentList + pair
            prefs[key] = Json.encodeToString(updatedList)
        }
    }

    override suspend fun removeWalletPair(pair: WalletPreferences.WalletPair) {
        context.dataStore.edit { prefs ->
            val currentList =
                prefs[key]?.let { Json.decodeFromString<List<WalletPreferences.WalletPair>>(it) }
                    ?: emptyList()
            val updatedList = currentList - pair
            prefs[key] = Json.encodeToString(updatedList.toList())
        }
    }
}