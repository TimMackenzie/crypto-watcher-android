package com.simplifynow.cryptowatcher.data.local

import com.simplifynow.cryptowatcher.BuildConfig
import com.simplifynow.cryptowatcher.data.local.datastore.WalletPreferencesDataStoreImpl
import com.simplifynow.cryptowatcher.data.local.room.WalletPreferencesRoomImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * This module is needed to support DI with Hilt; it injects a WalletPreferences implementation.
 * The build flavor determines which implementation is selected.
 */
@Module
@InstallIn(SingletonComponent::class)
object WalletPreferencesModule {

    @Provides
    @Singleton
    fun provideWalletPreferences(
        dataStoreImpl: WalletPreferencesDataStoreImpl,
        roomImpl: WalletPreferencesRoomImpl
    ): WalletPreferences {
        return if (BuildConfig.USE_ROOM) {
            roomImpl
        } else {
            dataStoreImpl
        }
    }
}