package com.rafabs.sp4u.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "stops")
data class Stop(
    @PrimaryKey val stopId: String,
    val stopName: String,
    val stopLat: Double,
    val stopLon: Double,
    val source: String  // "SPTRANS" ou "ARTESP"
)