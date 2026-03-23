package com.rafabs.sp4u.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rafabs.sp4u.data.local.entity.Shape

@Dao
interface ShapeDao {
    @Query("SELECT * FROM shapes WHERE shapeId = :shapeId ORDER BY shapePtSequence ASC")
    suspend fun getShapePoints(shapeId: String): List<Shape>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(shapes: List<Shape>)

    @Query("DELETE FROM shapes")
    suspend fun deleteAll()

    @Query("DELETE FROM shapes WHERE source = :source")
    suspend fun deleteBySource(source: String)
}