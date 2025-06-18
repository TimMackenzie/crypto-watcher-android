package com.simplifynow.cryptowatcher.data.local

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.simplifynow.cryptowatcher.data.local.datastore.WalletPreferencesDataStoreImpl
import com.simplifynow.cryptowatcher.data.local.room.WalletPreferencesDatabase
import com.simplifynow.cryptowatcher.data.local.room.WalletPreferencesRoomImpl
import junit.framework.TestCase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test
import kotlin.jvm.java

class WalletPreferencesImplTest {
    // WalletPreferencesImpl is a singleton; add to companion object to ensure there is only one copy
    companion object {
        private val testContext = ApplicationProvider.getApplicationContext<Context>()
        private val walletPreferencesDataStore = WalletPreferencesDataStoreImpl(testContext)

        private val testDb = Room.inMemoryDatabaseBuilder(
            testContext,
            WalletPreferencesDatabase::class.java
        ).allowMainThreadQueries().build()

        val walletPreferencesRoom = WalletPreferencesRoomImpl(testDb.walletDao())

    }

    @Test
    fun test_add_pairs() {
        test_add_pairs(walletPreferencesDataStore)
        test_add_pairs(walletPreferencesRoom)
    }

    private fun test_add_pairs(walletPreferences: WalletPreferences) = runTest {
        val initialPair = WalletPreferences.WalletPair(WalletPreferences.WalletType.ETH, "wallet1")
        val newPair = WalletPreferences.WalletPair(WalletPreferences.WalletType.WAX, "wallet2")

        walletPreferences.saveWalletPairs(listOf(initialPair))
        val pairs1 = walletPreferences.getWalletPairs().first()
        TestCase.assertEquals(1, pairs1.size)

        walletPreferences.addWalletPair(newPair)

        val pairs2 = walletPreferences.getWalletPairs().first()
        TestCase.assertNotNull(pairs2)
        TestCase.assertEquals(2, pairs2.size)
        Assert.assertTrue(pairs2.containsAll(listOf(initialPair, newPair)) == true)
    }

    @Test
    fun test_remove_pairs() {
        test_remove_pairs(walletPreferencesDataStore)
        test_remove_pairs(walletPreferencesRoom)
    }

    private fun test_remove_pairs(walletPreferences: WalletPreferences) = runTest {
        val pair1 = WalletPreferences.WalletPair(WalletPreferences.WalletType.ETH, "wallet1")
        val pair2 = WalletPreferences.WalletPair(WalletPreferences.WalletType.WAX, "wallet2")

        walletPreferences.saveWalletPairs(listOf(pair1, pair2))
        val pairs1 = walletPreferences.getWalletPairs().first()
        TestCase.assertEquals(2, pairs1.size)

        walletPreferences.removeWalletPair(pair1)

        val pairs2 = walletPreferences.getWalletPairs().first()
        TestCase.assertEquals(1, pairs2.size)
        Assert.assertTrue(pairs2.contains(pair2))
        Assert.assertFalse(pairs2.contains(pair1))
    }
}