package com.rafabs.sp4u.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.rafabs.sp4u.data.local.dao.RouteDao
import com.rafabs.sp4u.data.local.dao.ShapeDao
import com.rafabs.sp4u.data.local.dao.StopDao
import com.rafabs.sp4u.data.local.dao.TripDao
import com.rafabs.sp4u.data.local.entity.Route
import com.rafabs.sp4u.data.local.entity.Shape
import com.rafabs.sp4u.data.local.entity.Stop
import com.rafabs.sp4u.data.local.entity.StopTime
import com.rafabs.sp4u.data.local.entity.Trip

@Database(
    entities = [
        Route::class,
        Stop::class,
        Shape::class,
        Trip::class,
        StopTime::class
    ],
    version = 4, // ← Atualizar DB ao fazer alterações
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun routeDao(): RouteDao
    abstract fun stopDao(): StopDao
    abstract fun shapeDao(): ShapeDao
    abstract fun tripDao(): TripDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "sp4u_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}