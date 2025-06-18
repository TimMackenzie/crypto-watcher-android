package com.simplifynow.cryptowatcher.data.local.room

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object WalletPreferencesDatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): WalletPreferencesDatabase {
        return Room.databaseBuilder(
            appContext,
            WalletPreferencesDatabase::class.java,
            "wallets.db"
        ).build()
    }

    @Provides
    fun provideWalletDao(db: WalletPreferencesDatabase): WalletDao = db.walletDao()
}