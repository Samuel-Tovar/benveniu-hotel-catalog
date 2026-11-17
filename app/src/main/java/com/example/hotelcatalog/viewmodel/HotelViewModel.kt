
package com.example.hotelcatalog.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.hotelcatalog.data.HabitacionEntity
import com.example.hotelcatalog.data.HotelRepository
import com.example.hotelcatalog.data.ReservaEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HotelViewModel(
    private val repository: HotelRepository
) : ViewModel() {

    // HABITACIONES

    fun cargarHabitacionesIniciales() {
        viewModelScope.launch {

            val habitacionesIniciales = listOf(
                HabitacionEntity(
                    1, "Habitación Individual",
                    "1 cama individual • WiFi • TV • Baño privado",
                    120000.0, 1, true
                ),
                HabitacionEntity(
                    2, "Habitación Doble",
                    "2 camas • WiFi • TV • Aire acondicionado",
                    180000.0, 2, true
                ),
                HabitacionEntity(
                    3, "Habitación Matrimonial",
                    "Cama doble • WiFi • TV • Minibar",
                    210000.0, 2, false
                ),
                HabitacionEntity(
                    4, "Habitación Familiar",
                    "3 camas • WiFi • TV • Desayuno incluido",
                    280000.0, 4, true
                ),
                HabitacionEntity(
                    5, "Suite Junior",
                    "Cama Queen • WiFi • Minibar • Zona de descanso",
                    350000.0, 2, true
                ),
                HabitacionEntity(
                    6, "Suite Presidencial",
                    "Cama King • Jacuzzi • Balcón • Sala privada",
                    450000.0, 2, true
                )
            )

            habitacionesIniciales.forEach { habitacion ->
                repository.insertarHabitacion(habitacion)
            }
        }
    }

    val habitaciones = repository.habitaciones.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun insertarHabitacion(habitacion: HabitacionEntity) {
        viewModelScope.launch {
            repository.insertarHabitacion(habitacion)
        }
    }

    fun actualizarHabitacion(habitacion: HabitacionEntity) {
        viewModelScope.launch {
            repository.actualizarHabitacion(habitacion)
        }
    }

    // RESERVAS

    val reservas = repository.reservas.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun insertarReserva(reserva: ReservaEntity) {
        viewModelScope.launch {
            repository.insertarReserva(reserva)
        }
    }

    fun actualizarReserva(reserva: ReservaEntity) {
        viewModelScope.launch {
            repository.actualizarReserva(reserva)
        }
    }

    fun eliminarReserva(reserva: ReservaEntity) {
        viewModelScope.launch {
            repository.eliminarReserva(reserva)
        }
    }

    fun cambiarEstadoReserva(id: Int, estado: String) {
        viewModelScope.launch {
            repository.cambiarEstadoReserva(id, estado)
        }
    }
}

// Permite crear el ViewModel con su Repository
class HotelViewModelFactory(
    private val repository: HotelRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {
        if (modelClass.isAssignableFrom(HotelViewModel::class.java)) {
            return HotelViewModel(repository) as T
        }

        throw IllegalArgumentException(
            "ViewModel desconocido"
        )
    }
}
