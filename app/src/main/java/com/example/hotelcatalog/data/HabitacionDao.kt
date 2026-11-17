
package com.example.hotelcatalog.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitacionDao {

    @Query("SELECT * FROM habitaciones ORDER BY id ASC")
    fun obtenerHabitaciones(): Flow<List<HabitacionEntity>>

    @Query("SELECT * FROM habitaciones WHERE id = :id LIMIT 1")
    suspend fun obtenerPorId(id: Int): HabitacionEntity?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertar(habitacion: HabitacionEntity)

    @Update
    suspend fun actualizar(habitacion: HabitacionEntity)
}
