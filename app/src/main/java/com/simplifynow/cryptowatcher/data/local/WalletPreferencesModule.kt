package com.simplifynow.cryptowatcher.data.local

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * This module is needed to support DI with Hilt; it injects a WalletPreferences implementation
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class WalletPreferencesModule {

    @Binds
    abstract fun bindWalletPreferences(
        impl: WalletPreferencesDataStoreImpl
    ): WalletPreferences
}