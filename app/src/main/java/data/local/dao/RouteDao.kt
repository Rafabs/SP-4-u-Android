package com.rafabs.sp4u.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rafabs.sp4u.data.local.entity.Route
import kotlinx.coroutines.flow.Flow

@Dao
interface RouteDao {
    @Query("SELECT * FROM routes ORDER BY shortName ASC")
    fun getAllRoutes(): Flow<List<Route>>

    @Query("SELECT * FROM routes WHERE source = :source AND (shortName LIKE '%' || :query || '%' OR longName LIKE '%' || :query || '%')")
    fun searchRoutes(query: String, source: String): Flow<List<Route>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(routes: List<Route>)

    @Query("DELETE FROM routes")
    suspend fun deleteAll()

    @Query("SELECT * FROM routes WHERE source = :source ORDER BY shortName ASC")
    fun getRoutesBySource(source: String): Flow<List<Route>>

    @Query("SELECT * FROM routes LIMIT 5")
    suspend fun getSampleAll(): List<Route>

    @Query("SELECT * FROM routes WHERE shortName LIKE :letreiro || '%' AND source = :source LIMIT 1")
    suspend fun getRouteByLetreiro(letreiro: String, source: String): Route?

    @Query("SELECT routeId FROM routes WHERE shortName LIKE :letreiro || '%' AND source = :source LIMIT 1")
    suspend fun getRouteIdByLetreiro(letreiro: String, source: String): String?
    @Query("DELETE FROM routes WHERE source = :source")
    suspend fun deleteBySource(source: String)
}