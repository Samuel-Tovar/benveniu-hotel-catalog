
package com.example.hotelcatalog.data

class HotelRepository(
    private val habitacionDao: HabitacionDao,
    private val reservaDao: ReservaDao
) {
    // HABITACIONES

    val habitaciones = habitacionDao.obtenerHabitaciones()

    suspend fun obtenerHabitacion(id: Int): HabitacionEntity? {
        return habitacionDao.obtenerPorId(id)
    }

    suspend fun insertarHabitacion(habitacion: HabitacionEntity) {
        habitacionDao.insertar(habitacion)
    }

    suspend fun actualizarHabitacion(habitacion: HabitacionEntity) {
        habitacionDao.actualizar(habitacion)
    }

    // RESERVAS

    val reservas = reservaDao.obtenerReservas()

    suspend fun obtenerReserva(id: Int): ReservaEntity? {
        return reservaDao.obtenerPorId(id)
    }

    suspend fun insertarReserva(reserva: ReservaEntity) {
        reservaDao.insertar(reserva)
    }

    suspend fun actualizarReserva(reserva: ReservaEntity) {
        reservaDao.actualizar(reserva)
    }

    suspend fun eliminarReserva(reserva: ReservaEntity) {
        reservaDao.eliminar(reserva)
    }

    suspend fun cambiarEstadoReserva(id: Int, estado: String) {
        reservaDao.cambiarEstado(id, estado)
    }
}
