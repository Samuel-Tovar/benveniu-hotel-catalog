
package com.example.hotelcatalog.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "habitaciones")
data class HabitacionEntity(
    @PrimaryKey
    val id: Int,
    val nombre: String,
    val descripcion: String,
    val precio: Double,
    val capacidad: Int,
    val disponible: Boolean = true
)
