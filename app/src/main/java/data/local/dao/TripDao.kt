package com.rafabs.sp4u.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rafabs.sp4u.data.local.entity.Trip

@Dao
interface TripDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(trips: List<Trip>)

    @Query("SELECT shapeId FROM trips WHERE routeId = :routeId LIMIT 1")
    suspend fun getShapeIdForRoute(routeId: String): String?

    @Query("DELETE FROM trips WHERE source = :source")
    suspend fun deleteBySource(source: String)
}