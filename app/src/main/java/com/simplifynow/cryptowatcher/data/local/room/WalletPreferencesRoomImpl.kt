package com.simplifynow.cryptowatcher.data.local.room

import android.util.Log
import androidx.room.*
import com.simplifynow.cryptowatcher.data.local.WalletPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Entity(tableName = "wallet_pairs")
data class WalletEntity(
    @PrimaryKey val address: String,
    val type: String
)

@Dao
interface WalletDao {
    @Query("SELECT * FROM wallet_pairs")
    fun getAll(): Flow<List<WalletEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(wallet: WalletEntity)

    @Delete
    suspend fun delete(wallet: WalletEntity)

    @Query("DELETE FROM wallet_pairs")
    suspend fun clear()
}

@Singleton
class WalletPreferencesRoomImpl @Inject constructor(
    private val dao: WalletDao
) : WalletPreferences {

    override suspend fun saveWalletPairs(walletPairs: List<WalletPreferences.WalletPair>) {
        dao.clear()
        walletPairs.forEach {
            dao.insert(WalletEntity(address = it.address, type = it.type.name))
        }
    }

    override fun getWalletPairs(): Flow<List<WalletPreferences.WalletPair>> {
        Log.d("getWalletPairs",  "Using Room")
        return dao.getAll().map { list ->
            list.map { WalletPreferences.WalletPair(WalletPreferences.WalletType.valueOf(it.type), it.address) }
        }
    }

    override suspend fun addWalletPair(pair: WalletPreferences.WalletPair) {
        dao.insert(WalletEntity(address = pair.address, type = pair.type.name))
    }

    override suspend fun removeWalletPair(pair: WalletPreferences.WalletPair) {
        dao.delete(WalletEntity(address = pair.address, type = pair.type.name))
    }
}