package com.rafabs.sp4u.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "trips")
data class Trip(
    @PrimaryKey val tripId: String,
    val routeId: String,
    val shapeId: String,
    val source: String,
    val directionId: Int = 0
)