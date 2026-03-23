package com.rafabs.sp4u.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rafabs.sp4u.data.local.entity.Stop
import kotlinx.coroutines.flow.Flow

@Dao
interface StopDao {
    @Query("SELECT * FROM stops WHERE stopId IN (SELECT stopId FROM stop_times WHERE tripId IN (SELECT tripId FROM trips WHERE routeId = :routeId)) LIMIT 200")
    fun getStopsForRoute(routeId: String): Flow<List<Stop>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(stops: List<Stop>)

    @Query("DELETE FROM stops")
    suspend fun deleteAll()

    @Query("DELETE FROM stops WHERE source = :source")
    suspend fun deleteBySource(source: String)
}