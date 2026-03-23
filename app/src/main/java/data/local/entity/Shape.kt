package com.rafabs.sp4u.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shapes")
data class Shape(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val shapeId: String,
    val shapePtLat: Double,
    val shapePtLon: Double,
    val shapePtSequence: Int,
    val source: String  // "SPTRANS" ou "ARTESP"
)