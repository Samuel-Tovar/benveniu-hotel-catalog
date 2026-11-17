
package com.example.hotelcatalog.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        HabitacionEntity::class,
        ReservaEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class HotelDatabase : RoomDatabase() {

    abstract fun habitacionDao(): HabitacionDao
    abstract fun reservaDao(): ReservaDao

    companion object {

        @Volatile
        private var INSTANCE: HotelDatabase? = null

        fun obtenerDatabase(context: Context): HotelDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    HotelDatabase::class.java,
                    "benveniu_hotel_db"
                ).build()

                INSTANCE = instance
                instance
            }
        }
    }
}
