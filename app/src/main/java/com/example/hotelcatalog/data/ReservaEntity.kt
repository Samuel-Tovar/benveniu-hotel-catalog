
package com.example.hotelcatalog.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "reservas",
    foreignKeys = [
        ForeignKey(
            entity = HabitacionEntity::class,
            parentColumns = ["id"],
            childColumns = ["habitacionId"],
            onDelete = ForeignKey.RESTRICT
        )
    ],
    indices = [Index(value = ["habitacionId"])]
)
data class ReservaEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val habitacionId: Int,
    val nombreHuesped: String,
    val fechaEntrada: String,
    val fechaSalida: String,
    val estado: String = "Activa"
)
