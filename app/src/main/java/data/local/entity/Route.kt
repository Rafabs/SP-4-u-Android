package com.rafabs.sp4u.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "routes")
data class Route(
    @PrimaryKey val routeId: String,
    val shortName: String,
    val longName: String,
    val color: String,
    val textColor: String,
    val source: String  // "SPTRANS" ou "ARTESP"
)