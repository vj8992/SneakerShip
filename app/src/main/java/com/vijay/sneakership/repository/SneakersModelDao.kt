package com.vijay.sneakership.repository

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vijay.sneakership.model.SneakersModel

@Dao
interface SneakersModelDao {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertAll(sneakersModels: List<SneakersModel>)

  @Query("SELECT * FROM sneakers LIMIT :limit OFFSET :offset")
  suspend fun getAllSneakers(limit: Int, offset: Int) : List<SneakersModel>

  @Query("SELECT * FROM sneakers WHERE name LIKE :queryString OR colorway LIKE :queryString")
  suspend fun getSearchSneakers(queryString: String) : List<SneakersModel>

  @Query("DELETE FROM sneakers")
  suspend fun clearAllSneakers()
}