
package com.example.hotelcatalog.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Update
import androidx.room.Delete
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ReservaDao {

    @Query("SELECT * FROM reservas ORDER BY id DESC")
    fun obtenerReservas(): Flow<List<ReservaEntity>>

    @Query("SELECT * FROM reservas WHERE id = :id LIMIT 1")
    suspend fun obtenerPorId(id: Int): ReservaEntity?

    @Query("SELECT * FROM reservas WHERE habitacionId = :habitacionId")
    fun obtenerPorHabitacion(
        habitacionId: Int
    ): Flow<List<ReservaEntity>>

    @Insert
    suspend fun insertar(reserva: ReservaEntity)

    @Update
    suspend fun actualizar(reserva: ReservaEntity)

    @Delete
    suspend fun eliminar(reserva: ReservaEntity)

    @Query("UPDATE reservas SET estado = :estado WHERE id = :id")
    suspend fun cambiarEstado(id: Int, estado: String)
}
