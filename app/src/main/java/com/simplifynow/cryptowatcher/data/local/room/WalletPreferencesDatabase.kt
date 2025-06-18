package com.simplifynow.cryptowatcher.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [WalletEntity::class],
    version = 1,
    exportSchema = false
)
abstract class WalletPreferencesDatabase : RoomDatabase() {
    abstract fun walletDao(): WalletDao
}