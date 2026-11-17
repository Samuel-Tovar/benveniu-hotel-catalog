package com.example.hotelcatalog

import android.os.Bundle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextButton
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import com.example.hotelcatalog.ui.theme.HotelAvailable
import com.example.hotelcatalog.ui.theme.HotelCatalogTheme
import com.example.hotelcatalog.ui.theme.HotelGold
import com.example.hotelcatalog.ui.theme.HotelReserved
import com.example.hotelcatalog.ui.theme.HotelUnavailable
import com.example.hotelcatalog.ui.theme.HotelWhite
import com.example.hotelcatalog.data.HotelDatabase
import com.example.hotelcatalog.data.HotelRepository
import com.example.hotelcatalog.viewmodel.HotelViewModel
import com.example.hotelcatalog.viewmodel.HotelViewModelFactory
import com.example.hotelcatalog.data.ReservaEntity
import java.util.Locale
import java.time.LocalDate
import java.time.format.DateTimeParseException
import java.time.temporal.ChronoUnit


// =====================================================
// MODELO DE DATOS
// =====================================================

data class Habitacion(
    val nombre: String,
    val categoria: String,
    val descripcion: String,
    val precio: Int,
    val capacidad: String,
    val disponible: Boolean,
    val destacada: Boolean,
    val imagen: Int
)


// =====================================================
// MAIN ACTIVITY
// =====================================================


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = HotelDatabase.obtenerDatabase(
            applicationContext
        )

        val repository = HotelRepository(
            habitacionDao = database.habitacionDao(),
            reservaDao = database.reservaDao()
        )

        val factory = HotelViewModelFactory(repository)

        setContent {
            val hotelViewModel: HotelViewModel = viewModel(
                factory = factory
            )

            HotelCatalogApp(
                hotelViewModel = hotelViewModel
            )
        }
    }
}



// =====================================================
// CONTROL GENERAL DEL TEMA
// =====================================================

@Composable
fun HotelCatalogApp(
    hotelViewModel: HotelViewModel
) {

    val temaSistemaOscuro = isSystemInDarkTheme()

    var temaOscuro by remember {
        mutableStateOf(temaSistemaOscuro)
    }


    HotelCatalogTheme(
        darkTheme = temaOscuro
    ) {

        BenveniuHotelApp(
            temaOscuro = temaOscuro,
            hotelViewModel = hotelViewModel,

            onCambiarTema = {
                temaOscuro = !temaOscuro
            }
        )
    }
}


// =====================================================
// APLICACIÓN PRINCIPAL
// =====================================================

@Composable
fun BenveniuHotelApp(
    temaOscuro: Boolean,
    hotelViewModel: HotelViewModel,
    onCambiarTema: () -> Unit
) {

    val navController = rememberNavController()
    LaunchedEffect(hotelViewModel) {
        hotelViewModel.cargarHabitacionesIniciales()
    }

    var habitacionParaReservar by remember {
        mutableStateOf<Habitacion?>(null)
    }

    var reservaParaEditar by remember {
        mutableStateOf<ReservaEntity?>(null)
    }

    var reservaParaEliminar by remember {
        mutableStateOf<ReservaEntity?>(null)
    }


    // =================================================
    // ESTADO COMPARTIDO
    // =================================================

    var noches by remember {
        mutableStateOf(1)
    }



    val reservasGuardadas by hotelViewModel.reservas.collectAsState()

    val habitacionesReservadas = reservasGuardadas
        .filter { it.estado == "Activa" }
        .mapNotNull { reserva ->
            when (reserva.habitacionId) {
                1 -> "Habitación Individual"
                2 -> "Habitación Doble"
                3 -> "Habitación Matrimonial"
                4 -> "Habitación Familiar"
                5 -> "Suite Junior"
                6 -> "Suite Presidencial"
                else -> null
            }
        }
        .toSet()



    // =================================================
    // COLECCIÓN DE HABITACIONES
    // =================================================

    val habitaciones = listOf(

        Habitacion(
            nombre = "Habitación Individual",
            categoria = "Individual",
            descripcion = "1 cama individual • WiFi • TV • Baño privado",
            precio = 120000,
            capacidad = "1 persona",
            disponible = true,
            destacada = false,
            imagen = R.drawable.habitacion_individual
        ),

        Habitacion(
            nombre = "Habitación Doble",
            categoria = "Doble",
            descripcion = "2 camas • WiFi • TV • Aire acondicionado",
            precio = 180000,
            capacidad = "2 personas",
            disponible = true,
            destacada = false,
            imagen = R.drawable.habitacion_doble
        ),

        Habitacion(
            nombre = "Habitación Matrimonial",
            categoria = "Matrimonial",
            descripcion = "Cama doble • WiFi • TV • Minibar",
            precio = 210000,
            capacidad = "2 personas",
            disponible = false,
            destacada = false,
            imagen = R.drawable.habitacion_matrimonial
        ),

        Habitacion(
            nombre = "Habitación Familiar",
            categoria = "Familiar",
            descripcion = "3 camas • WiFi • TV • Desayuno incluido",
            precio = 280000,
            capacidad = "4 personas",
            disponible = true,
            destacada = false,
            imagen = R.drawable.habitacion_familiar
        ),

        Habitacion(
            nombre = "Suite Junior",
            categoria = "Suites",
            descripcion = "Cama Queen • WiFi • Minibar • Zona de descanso",
            precio = 350000,
            capacidad = "2 personas",
            disponible = true,
            destacada = false,

            // Imagen temporal
            imagen = R.drawable.suite_junior_con_vista_panoramica
        ),

        Habitacion(
            nombre = "Suite Presidencial",
            categoria = "Suites",
            descripcion = "Cama King • Jacuzzi • Balcón • Sala privada",
            precio = 450000,
            capacidad = "2 personas",
            disponible = true,
            destacada = true,
            imagen = R.drawable.suite_presidencial
        )
    )

    reservaParaEditar?.let { reserva ->
        FormularioEditarReservaDialog(
            reserva = reserva,
            onGuardar = { reservaActualizada ->

                hotelViewModel.actualizarReserva(reservaActualizada)

                reservaParaEditar = null
            },
            onCerrar = {
                reservaParaEditar = null
            }
        )
    }

    reservaParaEliminar?.let { reserva ->

        AlertDialog(
            onDismissRequest = {
                reservaParaEliminar = null
            },

            title = {
                Text("Eliminar reserva")
            },

            text = {
                Text(
                    "¿Estás seguro de que deseas eliminar " +
                            "la reserva de ${reserva.nombreHuesped}? " +
                            "Esta acción no se puede deshacer."
                )
            },

            confirmButton = {
                TextButton(
                    onClick = {
                        hotelViewModel.eliminarReserva(reserva)
                        reservaParaEliminar = null
                    }
                ) {
                    Text(
                        text = "Eliminar",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },

            dismissButton = {
                TextButton(
                    onClick = {
                        reservaParaEliminar = null
                    }
                ) {
                    Text("Volver")
                }
            }
        )
    }


    habitacionParaReservar?.let { habitacion ->

        FormularioReservaDialog(
            nombreHabitacion = habitacion.nombre,

            onConfirmar = { nombre, entrada, salida ->

                val habitacionId = when (habitacion.nombre) {
                    "Habitación Individual" -> 1
                    "Habitación Doble" -> 2
                    "Habitación Matrimonial" -> 3
                    "Habitación Familiar" -> 4
                    "Suite Junior" -> 5
                    "Suite Presidencial" -> 6
                    else -> 0
                }

                if (habitacionId != 0) {

                    hotelViewModel.insertarReserva(
                        ReservaEntity(
                            habitacionId = habitacionId,
                            nombreHuesped = nombre,
                            fechaEntrada = entrada,
                            fechaSalida = salida,
                            estado = "Activa"
                        )
                    )
                }

                habitacionParaReservar = null
            },

            onCerrar = {
                habitacionParaReservar = null
            }
        )
    }


    // =================================================
    // SCAFFOLD PRINCIPAL
    // =================================================

    Scaffold(

        containerColor =
            MaterialTheme.colorScheme.background,


        // =================================================
        // BARRA SUPERIOR
        // =================================================

        topBar = {

            Surface(
                color =
                    MaterialTheme.colorScheme.primary,

                shadowElevation = 6.dp
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = 16.dp,
                            vertical = 12.dp
                        ),

                    verticalAlignment =
                        Alignment.CenterVertically
                ) {


                    // =================================================
                    // NOMBRE DEL HOTEL
                    // =================================================

                    Column(
                        modifier =
                            Modifier.weight(1f)
                    ) {

                        Text(
                            text =
                                "Benveniu Hotel",

                            style =
                                MaterialTheme.typography.headlineMedium,

                            fontWeight =
                                FontWeight.Bold,

                            color =
                                MaterialTheme.colorScheme.onPrimary
                        )


                        Text(
                            text =
                                "Luxury Room Collection",

                            style =
                                MaterialTheme.typography.bodyMedium,

                            color =
                                MaterialTheme.colorScheme.onPrimary
                        )
                    }


                    // =================================================
                    // BOTÓN CAMBIAR TEMA
                    // =================================================

                    Button(
                        onClick =
                            onCambiarTema,

                        shape =
                            RoundedCornerShape(20.dp),

                        colors =
                            ButtonDefaults.buttonColors(

                                containerColor =
                                    MaterialTheme.colorScheme.secondary,

                                contentColor =
                                    MaterialTheme.colorScheme.onSecondary
                            )
                    ) {

                        Text(
                            text =
                                if (temaOscuro) {

                                    "☀ Claro"

                                } else {

                                    "🌙 Oscuro"
                                },

                            fontWeight =
                                FontWeight.Bold
                        )
                    }
                }
            }
        },


        // =================================================
        // BARRA INFERIOR
        // =================================================

        bottomBar = {

            Surface(
                color =
                    MaterialTheme.colorScheme.surface,

                shadowElevation = 10.dp
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp),

                    horizontalArrangement =
                        Arrangement.SpaceEvenly
                ) {


                    OpcionNavegacion(
                        simbolo = "⌂",
                        texto = "Inicio",

                        onClick = {

                            navController.navigate("inicio") {

                                launchSingleTop = true
                            }
                        }
                    )


                    OpcionNavegacion(
                        simbolo = "▦",
                        texto = "Habitaciones",

                        onClick = {

                            navController.navigate("habitaciones") {

                                launchSingleTop = true
                            }
                        }
                    )


                    OpcionNavegacion(
                        simbolo = "▣",
                        texto = "Reservas",

                        onClick = {

                            navController.navigate("reservas") {

                                launchSingleTop = true
                            }
                        }
                    )
                }
            }
        }

    ) { innerPadding ->


        // =================================================
        // NAVEGACIÓN
        // =================================================

        NavHost(
            navController =
                navController,

            startDestination =
                "inicio",

            modifier =
                Modifier.padding(innerPadding)
        ) {


            // =================================================
            // INICIO
            // =================================================

            composable("inicio") {

                InicioScreen(
                    habitaciones =
                        habitaciones,

                    cantidadReservas =
                        habitacionesReservadas.size,

                    onVerHabitaciones = {

                        navController.navigate(
                            "habitaciones"
                        )
                    }
                )
            }


            // =================================================
            // HABITACIONES
            // =================================================

            composable("habitaciones") {

                HabitacionesScreen(
                    habitaciones =
                        habitaciones,

                    noches =
                        noches,

                    habitacionesReservadas =
                        habitacionesReservadas,

                    onAumentarNoches = {

                        if (noches < 30) {

                            noches++
                        }
                    },

                    onDisminuirNoches = {

                        if (noches > 1) {

                            noches--
                        }
                    },



                    onCambiarReserva = { habitacion ->

                        val habitacionId = when (habitacion.nombre) {
                            "Habitación Individual" -> 1
                            "Habitación Doble" -> 2
                            "Habitación Matrimonial" -> 3
                            "Habitación Familiar" -> 4
                            "Suite Junior" -> 5
                            "Suite Presidencial" -> 6
                            else -> 0
                        }

                        val reservaExistente = reservasGuardadas.find {
                            it.habitacionId == habitacionId &&
                                    it.estado == "Activa"
                        }

                        if (reservaExistente != null) {

                            // Cancelar una reserva existente
                            hotelViewModel.cambiarEstadoReserva(
                                reservaExistente.id,
                                "Cancelada"
                            )

                        } else if (habitacion.disponible && habitacionId != 0) {

                            // Abrir formulario para una reserva nueva
                            habitacionParaReservar = habitacion
                        }
                    }

                )
            }


            // =================================================
            // RESERVAS
            // =================================================

            composable("reservas") {

                ReservasScreen(
                    habitaciones = habitaciones,
                    habitacionesReservadas = habitacionesReservadas,
                    reservasGuardadas = reservasGuardadas,
                    noches = noches,

                    onEditarReserva = { reserva ->
                        reservaParaEditar = reserva
                    },

                    onEliminarReserva = { reserva ->
                        reservaParaEliminar = reserva
                    },

                    onCancelarReserva = { habitacion ->

                        val habitacionId = when (habitacion.nombre) {
                            "Habitación Individual" -> 1
                            "Habitación Doble" -> 2
                            "Habitación Matrimonial" -> 3
                            "Habitación Familiar" -> 4
                            "Suite Junior" -> 5
                            "Suite Presidencial" -> 6
                            else -> 0
                        }

                        val reserva = reservasGuardadas.find {
                            it.habitacionId == habitacionId &&
                                    it.estado == "Activa"
                        }

                        if (reserva != null) {
                            hotelViewModel.cambiarEstadoReserva(
                                reserva.id,
                                "Cancelada"
                            )
                        }
                    },

                    onIrHabitaciones = {

                        navController.navigate(
                            "habitaciones"
                        )
                    }
                )
            }
        }
    }
}


// =====================================================
// PANTALLA 1 - INICIO
// =====================================================

@Composable
fun InicioScreen(
    habitaciones: List<Habitacion>,
    cantidadReservas: Int,
    onVerHabitaciones: () -> Unit
) {

    LazyColumn(
        modifier =
            Modifier.fillMaxSize(),

        contentPadding =
            PaddingValues(20.dp),

        verticalArrangement =
            Arrangement.spacedBy(18.dp)
    ) {


        // =================================================
        // PRESENTACIÓN
        // =================================================

        item {

            Text(
                text =
                    "Bienvenido a Benveniu",

                style =
                    MaterialTheme.typography.headlineMedium,

                fontWeight =
                    FontWeight.Bold,

                color =
                    MaterialTheme.colorScheme.onBackground
            )


            Spacer(
                modifier =
                    Modifier.height(8.dp)
            )


            Text(
                text =
                    "Descubre una experiencia diseñada para el descanso, " +
                            "la comodidad y la elegancia.",

                style =
                    MaterialTheme.typography.bodyMedium,

                color =
                    MaterialTheme.colorScheme.onBackground
            )
        }


        // =================================================
        // ACCESO AL CATÁLOGO
        // =================================================

        item {

            Card(
                modifier =
                    Modifier.fillMaxWidth(),

                colors =
                    CardDefaults.cardColors(

                        containerColor =
                            MaterialTheme.colorScheme.surface
                    ),

                shape =
                    RoundedCornerShape(18.dp)
            ) {

                Column(
                    modifier =
                        Modifier.padding(18.dp)
                ) {

                    Text(
                        text =
                            "Tu estadía comienza aquí",

                        style =
                            MaterialTheme.typography.titleLarge,

                        fontWeight =
                            FontWeight.Bold,

                        color =
                            MaterialTheme.colorScheme.onSurface
                    )


                    Spacer(
                        modifier =
                            Modifier.height(8.dp)
                    )


                    Text(
                        text =
                            "Habitaciones seleccionadas: $cantidadReservas",

                        style =
                            MaterialTheme.typography.bodyMedium,

                        color =
                            MaterialTheme.colorScheme.onSurface
                    )


                    Spacer(
                        modifier =
                            Modifier.height(12.dp)
                    )


                    Button(
                        onClick =
                            onVerHabitaciones,

                        colors =
                            ButtonDefaults.buttonColors(

                                containerColor =
                                    MaterialTheme.colorScheme.primary,

                                contentColor =
                                    MaterialTheme.colorScheme.onPrimary
                            )
                    ) {

                        Text(
                            text =
                                "Explorar habitaciones"
                        )
                    }
                }
            }
        }


        // =================================================
        // DESTACADA
        // =================================================

        item {

            Text(
                text =
                    "Habitación destacada",

                style =
                    MaterialTheme.typography.titleLarge,

                fontWeight =
                    FontWeight.Bold,

                color =
                    MaterialTheme.colorScheme.onBackground
            )
        }


        items(
            habitaciones.filter {
                it.destacada
            }
        ) { habitacion ->

            Card(
                modifier =
                    Modifier.fillMaxWidth(),

                colors =
                    CardDefaults.cardColors(

                        containerColor =
                            MaterialTheme.colorScheme.surfaceVariant
                    ),

                shape =
                    RoundedCornerShape(20.dp),

                elevation =
                    CardDefaults.cardElevation(
                        defaultElevation = 8.dp
                    )
            ) {

                Column(
                    modifier =
                        Modifier.padding(12.dp)
                ) {

                    Image(
                        painter =
                            painterResource(
                                id =
                                    habitacion.imagen
                            ),

                        contentDescription =
                            habitacion.nombre,

                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp)
                            .clip(
                                RoundedCornerShape(
                                    16.dp
                                )
                            ),

                        contentScale =
                            ContentScale.Crop
                    )


                    Spacer(
                        modifier =
                            Modifier.height(12.dp)
                    )


                    Text(
                        text =
                            habitacion.nombre,

                        style =
                            MaterialTheme.typography.titleLarge,

                        fontWeight =
                            FontWeight.Bold,

                        color =
                            MaterialTheme.colorScheme.onSurfaceVariant
                    )


                    Text(
                        text =
                            "${formatearPrecio(habitacion.precio)} COP / noche",

                        style =
                            MaterialTheme.typography.titleMedium,

                        fontWeight =
                            FontWeight.Bold,

                        color =
                            MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}


// =====================================================
// PANTALLA 2 - HABITACIONES
// =====================================================

@Composable
fun HabitacionesScreen(
    habitaciones: List<Habitacion>,
    noches: Int,
    habitacionesReservadas: Set<String>,
    onAumentarNoches: () -> Unit,
    onDisminuirNoches: () -> Unit,
    onCambiarReserva: (Habitacion) -> Unit
) {


    // =================================================
    // ESTADOS DE ESTA PANTALLA
    // =================================================

    var categoriaSeleccionada by remember {
        mutableStateOf("Todas")
    }


    var ordenSeleccionado by remember {
        mutableStateOf("Normal")
    }


    // =================================================
    // CATEGORÍAS
    // =================================================

    val categorias = listOf(
        "Todas",
        "Individual",
        "Doble",
        "Matrimonial",
        "Familiar",
        "Suites"
    )


    // =================================================
    // FILTRAR
    // =================================================

    var habitacionesMostradas =

        if (
            categoriaSeleccionada ==
            "Todas"
        ) {

            habitaciones

        } else {

            habitaciones.filter {

                it.categoria ==
                        categoriaSeleccionada
            }
        }


    // =================================================
    // ORDENAR
    // =================================================

    habitacionesMostradas =

        when (
            ordenSeleccionado
        ) {

            "Precio ↑" ->

                habitacionesMostradas.sortedBy {

                    it.precio
                }


            "Precio ↓" ->

                habitacionesMostradas.sortedByDescending {

                    it.precio
                }


            "Nombre" ->

                habitacionesMostradas.sortedBy {

                    it.nombre
                }


            else ->

                habitacionesMostradas
        }


    // =================================================
    // INTERFAZ
    // =================================================

    LazyColumn(
        modifier =
            Modifier.fillMaxSize(),

        contentPadding =
            PaddingValues(16.dp),

        verticalArrangement =
            Arrangement.spacedBy(18.dp)
    ) {


        // =================================================
        // TÍTULO
        // =================================================

        item {

            Text(
                text =
                    "Nuestras habitaciones",

                style =
                    MaterialTheme.typography.headlineSmall,

                fontWeight =
                    FontWeight.Bold,

                color =
                    MaterialTheme.colorScheme.onBackground
            )


            Spacer(
                modifier =
                    Modifier.height(6.dp)
            )


            Text(
                text =
                    "Selecciona la habitación perfecta para tu estadía.",

                style =
                    MaterialTheme.typography.bodyMedium,

                color =
                    MaterialTheme.colorScheme.onBackground
            )
        }


        // =================================================
        // NOCHES
        // =================================================

        item {

            Card(
                modifier =
                    Modifier.fillMaxWidth(),

                colors =
                    CardDefaults.cardColors(

                        containerColor =
                            MaterialTheme.colorScheme.surface
                    ),

                shape =
                    RoundedCornerShape(16.dp)
            ) {

                Column(
                    modifier =
                        Modifier.padding(16.dp)
                ) {

                    Text(
                        text =
                            "Duración de la estadía",

                        style =
                            MaterialTheme.typography.titleMedium,

                        fontWeight =
                            FontWeight.Bold,

                        color =
                            MaterialTheme.colorScheme.onSurface
                    )


                    Spacer(
                        modifier =
                            Modifier.height(10.dp)
                    )


                    Row(
                        modifier =
                            Modifier.fillMaxWidth(),

                        horizontalArrangement =
                            Arrangement.SpaceBetween,

                        verticalAlignment =
                            Alignment.CenterVertically
                    ) {


                        Button(
                            onClick =
                                onDisminuirNoches,

                            enabled =
                                noches > 1,

                            colors =
                                ButtonDefaults.buttonColors(

                                    containerColor =
                                        MaterialTheme.colorScheme.primary,

                                    contentColor =
                                        MaterialTheme.colorScheme.onPrimary
                                )
                        ) {

                            Text("-")
                        }


                        Text(
                            text =
                                if (
                                    noches == 1
                                ) {

                                    "1 noche"

                                } else {

                                    "$noches noches"
                                },

                            style =
                                MaterialTheme.typography.titleLarge,

                            fontWeight =
                                FontWeight.Bold,

                            color =
                                MaterialTheme.colorScheme.onSurface
                        )


                        Button(
                            onClick =
                                onAumentarNoches,

                            enabled =
                                noches < 30,

                            colors =
                                ButtonDefaults.buttonColors(

                                    containerColor =
                                        MaterialTheme.colorScheme.primary,

                                    contentColor =
                                        MaterialTheme.colorScheme.onPrimary
                                )
                        ) {

                            Text("+")
                        }
                    }


                    Spacer(
                        modifier =
                            Modifier.height(10.dp)
                    )


                    Text(
                        text =
                            "Habitaciones reservadas: ${habitacionesReservadas.size}",

                        style =
                            MaterialTheme.typography.bodyMedium,

                        fontWeight =
                            FontWeight.Bold,

                        color =
                            MaterialTheme.colorScheme.primary
                    )
                }
            }
        }


        // =================================================
        // CATEGORÍAS
        // =================================================

        item {

            Text(
                text =
                    "Categorías",

                style =
                    MaterialTheme.typography.titleLarge,

                fontWeight =
                    FontWeight.Bold,

                color =
                    MaterialTheme.colorScheme.onBackground
            )
        }


        item {

            LazyRow(
                horizontalArrangement =
                    Arrangement.spacedBy(8.dp)
            ) {

                items(
                    categorias
                ) { categoria ->

                    val seleccionada =
                        categoria ==
                                categoriaSeleccionada


                    Button(
                        onClick = {

                            categoriaSeleccionada =
                                categoria
                        },

                        colors =
                            ButtonDefaults.buttonColors(

                                containerColor =
                                    if (
                                        seleccionada
                                    ) {

                                        MaterialTheme.colorScheme.primary

                                    } else {

                                        MaterialTheme.colorScheme.surface
                                    },

                                contentColor =
                                    if (
                                        seleccionada
                                    ) {

                                        MaterialTheme.colorScheme.onPrimary

                                    } else {

                                        MaterialTheme.colorScheme.onSurface
                                    }
                            )
                    ) {

                        Text(
                            text =
                                categoria
                        )
                    }
                }
            }
        }


        // =================================================
        // ORDENAMIENTO
        // =================================================

        item {

            Text(
                text =
                    "Ordenar por",

                style =
                    MaterialTheme.typography.titleLarge,

                fontWeight =
                    FontWeight.Bold,

                color =
                    MaterialTheme.colorScheme.onBackground
            )
        }


        item {

            val opcionesOrden = listOf(
                "Normal",
                "Precio ↑",
                "Precio ↓",
                "Nombre"
            )


            LazyRow(
                horizontalArrangement =
                    Arrangement.spacedBy(8.dp)
            ) {

                items(
                    opcionesOrden
                ) { opcion ->

                    val seleccionada =
                        ordenSeleccionado ==
                                opcion


                    Button(
                        onClick = {

                            ordenSeleccionado =
                                opcion
                        },

                        colors =
                            ButtonDefaults.buttonColors(

                                containerColor =
                                    if (
                                        seleccionada
                                    ) {

                                        MaterialTheme.colorScheme.primary

                                    } else {

                                        MaterialTheme.colorScheme.surface
                                    },

                                contentColor =
                                    if (
                                        seleccionada
                                    ) {

                                        MaterialTheme.colorScheme.onPrimary

                                    } else {

                                        MaterialTheme.colorScheme.onSurface
                                    }
                            )
                    ) {

                        Text(
                            text =
                                opcion
                        )
                    }
                }
            }
        }


        // =================================================
        // HABITACIONES
        // =================================================

        items(
            habitacionesMostradas
        ) { habitacion ->

            val reservada =
                habitacion.nombre in habitacionesReservadas


            HabitacionCard(
                habitacion =
                    habitacion,

                noches =
                    noches,

                reservada =
                    reservada,

                onCambiarReserva = {

                    onCambiarReserva(
                        habitacion
                    )
                }
            )
        }
    }
}


// =====================================================
// PANTALLA 3 - RESERVAS
// =====================================================

@Composable
fun ReservasScreen(
    habitaciones: List<Habitacion>,
    habitacionesReservadas: Set<String>,
    reservasGuardadas: List<ReservaEntity>,
    noches: Int,
    onCancelarReserva: (Habitacion) -> Unit,
    onEditarReserva: (ReservaEntity) -> Unit,
    onEliminarReserva: (ReservaEntity) -> Unit,
    onIrHabitaciones: () -> Unit
) {


    // =================================================
    // HABITACIONES RESERVADAS
    // =================================================

    val reservas =
        habitaciones.filter {

            it.nombre in habitacionesReservadas
        }


    // =================================================
    // TOTAL GENERAL
    // =================================================


    val reservasActivas = reservasGuardadas.filter {
        it.estado == "Activa"
    }

    val reservasCanceladas = reservasGuardadas.filter {
        it.estado == "Cancelada"
    }

    var mostrarHistorial by remember {
        mutableStateOf(false)
    }

    val reservasMostradas = if (mostrarHistorial) {
        reservasCanceladas
    } else {
        reservasActivas
    }

    val cantidadMostrada = reservasMostradas.size

    val nochesMostradas = reservasMostradas.sumOf { reserva ->
        calcularNochesReserva(
            reserva.fechaEntrada,
            reserva.fechaSalida
        ).toInt()
    }

    val totalMostrado = reservasMostradas.sumOf { reserva ->
        val precioHabitacion = habitaciones.find { habitacion ->
            val id = when (habitacion.nombre) {
                "Habitación Individual" -> 1
                "Habitación Doble" -> 2
                "Habitación Matrimonial" -> 3
                "Habitación Familiar" -> 4
                "Suite Junior" -> 5
                "Suite Presidencial" -> 6
                else -> 0
            }
            id == reserva.habitacionId
        }?.precio ?: 0

        precioHabitacion * calcularNochesReserva(
            reserva.fechaEntrada,
            reserva.fechaSalida
        ).toInt()
    }

    val totalNoches: Int = reservasActivas.sumOf { reserva ->
        calcularNochesReserva(
            reserva.fechaEntrada,
            reserva.fechaSalida
        ).toInt()
    }

    val totalGeneral: Int = reservasActivas.sumOf { reserva ->

        val habitacion = habitaciones.find { habitacion ->
            val habitacionId = when (habitacion.nombre) {
                "Habitación Individual" -> 1
                "Habitación Doble" -> 2
                "Habitación Matrimonial" -> 3
                "Habitación Familiar" -> 4
                "Suite Junior" -> 5
                "Suite Presidencial" -> 6
                else -> 0
            }

            habitacionId == reserva.habitacionId
        }

        val nochesReserva = calcularNochesReserva(
            reserva.fechaEntrada,
            reserva.fechaSalida
        ).toInt()

        (habitacion?.precio ?: 0) * nochesReserva
    }

    // =================================================
    // INTERFAZ
    // =================================================

    LazyColumn(
        modifier =
            Modifier.fillMaxSize(),

        contentPadding =
            PaddingValues(16.dp),

        verticalArrangement =
            Arrangement.spacedBy(18.dp)
    ) {


        // =================================================
        // ENCABEZADO
        // =================================================

        item {

            Text(
                text =
                    "Mis reservas",

                style =
                    MaterialTheme.typography.headlineSmall,

                fontWeight =
                    FontWeight.Bold,

                color =
                    MaterialTheme.colorScheme.onBackground
            )


            Spacer(
                modifier =
                    Modifier.height(6.dp)
            )


            Text(
                text =
                    "Consulta las habitaciones seleccionadas.",

                style =
                    MaterialTheme.typography.bodyMedium,

                color =
                    MaterialTheme.colorScheme.onBackground
            )
        }


        // =================================================
        // SIN RESERVAS
        // =================================================

        if (
            reservasGuardadas.isEmpty()
        ) {

            item {

                Card(
                    modifier =
                        Modifier.fillMaxWidth(),

                    colors =
                        CardDefaults.cardColors(

                            containerColor =
                                MaterialTheme.colorScheme.surface
                        ),

                    shape =
                        RoundedCornerShape(16.dp)
                ) {

                    Column(
                        modifier =
                            Modifier.padding(20.dp)
                    ) {

                        Text(
                            text =
                                "Aún no tienes habitaciones reservadas.",

                            style =
                                MaterialTheme.typography.titleMedium,

                            fontWeight =
                                FontWeight.Bold,

                            color =
                                MaterialTheme.colorScheme.onSurface
                        )


                        Spacer(
                            modifier =
                                Modifier.height(12.dp)
                        )


                        Button(
                            onClick =
                                onIrHabitaciones,

                            colors =
                                ButtonDefaults.buttonColors(

                                    containerColor =
                                        MaterialTheme.colorScheme.primary,

                                    contentColor =
                                        MaterialTheme.colorScheme.onPrimary
                                )
                        ) {

                            Text(
                                text =
                                    "Explorar habitaciones"
                            )
                        }
                    }
                }
            }

        } else {


            // =================================================
            // RESUMEN
            // =================================================

            item {

                Card(
                    modifier =
                        Modifier.fillMaxWidth(),

                    colors =
                        CardDefaults.cardColors(

                            containerColor =
                                MaterialTheme.colorScheme.surface
                        ),

                    shape =
                        RoundedCornerShape(16.dp)
                ) {

                    Column(
                        modifier =
                            Modifier.padding(16.dp),

                        verticalArrangement =
                            Arrangement.spacedBy(6.dp)
                    ) {

                        Text(
                            text =
                                if (mostrarHistorial) "Resumen del historial"
                                else "Resumen de reservas activas",

                            style =
                                MaterialTheme.typography.titleLarge,

                            fontWeight =
                                FontWeight.Bold,

                            color =
                                MaterialTheme.colorScheme.onSurface
                        )


                        Text(
                            text =
                                "Reservas: $cantidadMostrada",

                            color =
                                MaterialTheme.colorScheme.onSurface
                        )



                        Text(
                            text = "Noches registradas: $nochesMostradas",
                            color = MaterialTheme.colorScheme.onSurface
                        )



                        Text(
                            text =
                                "Valor total: ${formatearPrecio(totalMostrado)} COP",

                            style =
                                MaterialTheme.typography.titleMedium,

                            fontWeight =
                                FontWeight.Bold,

                            color =
                                MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = {
                            mostrarHistorial = false
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (!mostrarHistorial)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.surfaceVariant,
                            contentColor = if (!mostrarHistorial)
                                MaterialTheme.colorScheme.onPrimary
                            else
                                MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    ) {
                        Text("Activas")
                    }

                    Button(
                        onClick = {
                            mostrarHistorial = true
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (mostrarHistorial)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.surfaceVariant,
                            contentColor = if (mostrarHistorial)
                                MaterialTheme.colorScheme.onPrimary
                            else
                                MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    ) {
                        Text("Historial")
                    }
                }
            }


            // =================================================
            // CARDS DE RESERVAS
            // =================================================

            if (reservasMostradas.isEmpty()) {
                item {
                    Text(
                        text = if (mostrarHistorial)
                            "No tienes reservas canceladas."
                        else
                            "No tienes reservas activas.",

                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }

            items(reservasMostradas) { reserva ->

                val habitacion = habitaciones.find { habitacion ->
                    val habitacionId = when (habitacion.nombre) {
                        "Habitación Individual" -> 1
                        "Habitación Doble" -> 2
                        "Habitación Matrimonial" -> 3
                        "Habitación Familiar" -> 4
                        "Suite Junior" -> 5
                        "Suite Presidencial" -> 6
                        else -> 0
                    }

                    habitacionId == reserva.habitacionId
                }

                if (habitacion != null) {

                    val nochesReserva = calcularNochesReserva(
                        reserva.fechaEntrada,
                        reserva.fechaSalida
                    )

                    HabitacionCard(
                        habitacion = habitacion,
                        noches = nochesReserva.toInt(),
                        reservada = !mostrarHistorial,
                        onCambiarReserva = {
                            if (!mostrarHistorial) {
                                onCancelarReserva(habitacion)
                            }
                        },
                        reserva = reserva,
                        onEditarReserva = if (!mostrarHistorial) {
                            { onEditarReserva(reserva) }
                        } else {
                            null
                        },
                        onEliminarReserva = {
                            onEliminarReserva(reserva)
                        },
                        mostrarBotonReserva = !mostrarHistorial
                    )
                }
            }
        }
    }
}


// =====================================================
// CARD REUTILIZABLE
// =====================================================

@Composable
fun HabitacionCard(
    habitacion: Habitacion,
    noches: Int,
    reservada: Boolean,
    onCambiarReserva: () -> Unit,
    reserva: ReservaEntity? = null,
    onEditarReserva: (() -> Unit)? = null,
    onEliminarReserva: (() -> Unit)? = null,
    mostrarBotonReserva: Boolean = true
) {


    val total =
        habitacion.precio *
                noches


    Card(
        modifier =
            Modifier.fillMaxWidth(),

        shape =
            RoundedCornerShape(20.dp),

        colors =
            CardDefaults.cardColors(

                containerColor =
                    MaterialTheme.colorScheme.surfaceVariant
            ),

        elevation =
            CardDefaults.cardElevation(
                defaultElevation = 8.dp
            )
    ) {

        Column {


            // =================================================
            // IMAGEN
            // =================================================

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {

                Image(
                    painter =
                        painterResource(
                            id =
                                habitacion.imagen
                        ),

                    contentDescription =
                        habitacion.nombre,

                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                        .clip(
                            RoundedCornerShape(
                                16.dp
                            )
                        ),

                    contentScale =
                        ContentScale.Crop
                )


                // =================================================
                // DESTACADA
                // =================================================

                if (
                    habitacion.destacada
                ) {

                    Surface(
                        modifier = Modifier
                            .align(
                                Alignment.TopEnd
                            )
                            .padding(10.dp),

                        shape =
                            RoundedCornerShape(
                                20.dp
                            ),

                        color =
                            HotelGold,

                        shadowElevation =
                            4.dp
                    ) {

                        Text(
                            text =
                                "DESTACADA",

                            modifier =
                                Modifier.padding(
                                    horizontal = 12.dp,
                                    vertical = 6.dp
                                ),

                            fontWeight =
                                FontWeight.Bold,

                            color =
                                MaterialTheme.colorScheme.onTertiary
                        )
                    }
                }


                // =================================================
                // RESERVADA
                // =================================================

                if (
                    reservada
                ) {

                    Surface(
                        modifier = Modifier
                            .align(
                                Alignment.BottomStart
                            )
                            .padding(10.dp),

                        shape =
                            RoundedCornerShape(
                                20.dp
                            ),

                        color =
                            HotelReserved,

                        shadowElevation =
                            4.dp
                    ) {

                        Text(
                            text =
                                "EN RESERVA",

                            modifier =
                                Modifier.padding(
                                    horizontal = 12.dp,
                                    vertical = 6.dp
                                ),

                            fontWeight =
                                FontWeight.Bold,

                            color =
                                HotelWhite
                        )
                    }
                }
            }


            // =================================================
            // INFORMACIÓN
            // =================================================

            Column(
                modifier =
                    Modifier.padding(
                        start = 18.dp,
                        end = 18.dp,
                        bottom = 18.dp
                    ),

                verticalArrangement =
                    Arrangement.spacedBy(8.dp)
            ) {

                Text(
                    text =
                        habitacion.nombre,

                    style =
                        MaterialTheme.typography.titleLarge,

                    fontWeight =
                        FontWeight.Bold,

                    color =
                        MaterialTheme.colorScheme.onSurfaceVariant
                )


                if (reserva != null) {

                    Text(
                        text = "Huésped: ${reserva.nombreHuesped}",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Text(
                        text = "Entrada: ${reserva.fechaEntrada}",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Text(
                        text = "Salida: ${reserva.fechaSalida}",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Text(
                        text = "Duración: $noches noche(s)",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }



                Text(
                    text =
                        habitacion.descripcion,

                    style =
                        MaterialTheme.typography.bodyMedium,

                    color =
                        MaterialTheme.colorScheme.onSurfaceVariant
                )


                Text(
                    text =
                        "Capacidad: ${habitacion.capacidad}",

                    style =
                        MaterialTheme.typography.bodyMedium,

                    color =
                        MaterialTheme.colorScheme.onSurfaceVariant
                )


                // =================================================
                // PRECIO
                // =================================================

                Text(
                    text =
                        "${formatearPrecio(habitacion.precio)} COP / noche",

                    style =
                        MaterialTheme.typography.titleMedium,

                    fontWeight =
                        FontWeight.SemiBold,

                    color =
                        MaterialTheme.colorScheme.primary
                )


                // =================================================
                // TOTAL
                // =================================================

                Text(
                    text =
                        "Total por $noches noche(s): " +
                                "${formatearPrecio(total)} COP",

                    style =
                        MaterialTheme.typography.titleMedium,

                    fontWeight =
                        FontWeight.Bold,

                    color =
                        MaterialTheme.colorScheme.onSurfaceVariant
                )


                // =================================================
                // ESTADO
                // =================================================

                Text(
                    text = when {
                        reserva?.estado == "Cancelada" ->
                            "Cancelada"

                        reservada ->
                            "En reserva"

                        habitacion.disponible ->
                            "Disponible"

                        else ->
                            "No disponible"
                    },

                    fontWeight =
                        FontWeight.Bold,

                    color = when {
                        reserva?.estado == "Cancelada" ->
                            MaterialTheme.colorScheme.error

                        reservada ->
                            HotelReserved

                        habitacion.disponible ->
                            HotelAvailable

                        else ->
                            HotelUnavailable
                    }
                )


                // =================================================
                // BOTÓN RESERVAR
                // =================================================


                if (reserva != null && onEditarReserva != null) {
                    Button(
                        onClick = onEditarReserva,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondary,
                            contentColor = MaterialTheme.colorScheme.onSecondary
                        )
                    ) {
                        Text("✏ Editar reserva")
                    }
                }

                if (reserva != null && onEliminarReserva != null) {
                    Button(
                        onClick = onEliminarReserva,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error,
                            contentColor = MaterialTheme.colorScheme.onError
                        )
                    ) {
                        Text("Eliminar reserva")
                    }
                }

                if (mostrarBotonReserva) {

                Button(
                    onClick =
                        onCambiarReserva,

                    enabled =
                        habitacion.disponible ||
                                reservada,

                    shape =
                        RoundedCornerShape(
                            12.dp
                        ),

                    colors =
                        ButtonDefaults.buttonColors(

                            containerColor =
                                if (
                                    reservada
                                ) {

                                    HotelReserved

                                } else {

                                    MaterialTheme.colorScheme.primary
                                },

                            contentColor =
                                HotelWhite
                        )
                ) {

                    Text(
                        text =
                            when {

                                reservada ->

                                    "Cancelar reserva"

                                habitacion.disponible ->

                                    if (
                                        noches == 1
                                    ) {

                                        "Reservar 1 noche"

                                    } else {

                                        "Reservar $noches noches"
                                    }

                                else ->

                                    "No disponible"
                            }
                    )
                  }
                }
            }
        }
    }
}


// =====================================================
// BARRA DE NAVEGACIÓN
// =====================================================

@Composable
fun OpcionNavegacion(
    simbolo: String,
    texto: String,
    onClick: () -> Unit
) {

    Column(
        modifier = Modifier
            .clickable {

                onClick()
            }
            .padding(
                horizontal = 12.dp,
                vertical = 4.dp
            ),

        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {

        Text(
            text =
                simbolo,

            color =
                MaterialTheme.colorScheme.primary,

            fontWeight =
                FontWeight.Bold
        )


        Text(
            text =
                texto,

            style =
                MaterialTheme.typography.bodySmall,

            color =
                MaterialTheme.colorScheme.primary
        )
    }
}


// =====================================================
// FORMATEAR PRECIOS
// =====================================================

fun formatearPrecio(
    precio: Int
): String {

    return String
        .format(
            Locale.US,
            "%,d",
            precio
        )
        .replace(
            ",",
            "."
        )
}

@Composable
fun FormularioReservaDialog(
    nombreHabitacion: String,
    onConfirmar: (
        nombreHuesped: String,
        fechaEntrada: String,
        fechaSalida: String
    ) -> Unit,
    onCerrar: () -> Unit
) {

    var nombreHuesped by remember {
        mutableStateOf("")
    }

    var fechaEntrada by remember {
        mutableStateOf("")
    }

    var fechaSalida by remember {
        mutableStateOf("")
    }

    var errorFechas by remember {
        mutableStateOf<String?>(null)
    }

    AlertDialog(
        onDismissRequest = onCerrar,

        title = {
            Text("Nueva reserva")
        },

        text = {
            Column(
                verticalArrangement =
                    Arrangement.spacedBy(12.dp)
            ) {

                Text(
                    text = nombreHabitacion,
                    fontWeight = FontWeight.Bold
                )

                OutlinedTextField(
                    value = nombreHuesped,
                    onValueChange = {
                        nombreHuesped = it
                    },
                    label = {
                        Text("Nombre del huésped")
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = fechaEntrada,
                    onValueChange = {
                        fechaEntrada = it
                    },
                    label = {
                        Text("Fecha de entrada")
                    },
                    placeholder = {
                        Text("AAAA-MM-DD")
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = fechaSalida,
                    onValueChange = {
                        fechaSalida = it
                    },
                    label = {
                        Text("Fecha de salida")
                    },
                    placeholder = {
                        Text("AAAA-MM-DD")
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                // MENSAJE DE ERROR
                errorFechas?.let { mensaje ->
                    Text(
                        text = mensaje,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        },


        confirmButton = {
            TextButton(
                onClick = {

                    val error = validarFechasReserva(
                        fechaEntrada.trim(),
                        fechaSalida.trim()
                    )

                    if (error == null) {

                        onConfirmar(
                            nombreHuesped.trim(),
                            fechaEntrada.trim(),
                            fechaSalida.trim()
                        )

                    } else {

                        errorFechas = error

                    }
                },

                enabled =
                    nombreHuesped.isNotBlank() &&
                            fechaEntrada.isNotBlank() &&
                            fechaSalida.isNotBlank()

            ) {
                Text("Confirmar reserva")
            }
        },


        dismissButton = {
            TextButton(
                onClick = onCerrar
            ) {
                Text("Cancelar")
            }
        }
    )
}

fun validarFechasReserva(
    entrada: String,
    salida: String
): String? {

    return try {

        val fechaEntrada = LocalDate.parse(entrada)
        val fechaSalida = LocalDate.parse(salida)
        val fechaActual = LocalDate.now()

        when {
            fechaEntrada.isBefore(fechaActual) ->
                "La fecha de entrada no puede ser anterior a hoy."

            !fechaSalida.isAfter(fechaEntrada) ->
                "La fecha de salida debe ser posterior a la entrada."

            else -> null
        }

    } catch (e: DateTimeParseException) {
        "Ingresa fechas válidas en formato AAAA-MM-DD."
    }
}

fun calcularNochesReserva(
    entrada: String,
    salida: String
): Long {
    return try {
        val fechaEntrada = LocalDate.parse(entrada)
        val fechaSalida = LocalDate.parse(salida)

        ChronoUnit.DAYS.between(
            fechaEntrada,
            fechaSalida
        ).coerceAtLeast(0L)

    } catch (_: DateTimeParseException) {
        0L
    }
}

@Composable
fun FormularioEditarReservaDialog(
    reserva: ReservaEntity,
    onGuardar: (ReservaEntity) -> Unit,
    onCerrar: () -> Unit
) {
    var nombreHuesped by remember(reserva.id) {
        mutableStateOf(reserva.nombreHuesped)
    }

    var fechaEntrada by remember(reserva.id) {
        mutableStateOf(reserva.fechaEntrada)
    }

    var fechaSalida by remember(reserva.id) {
        mutableStateOf(reserva.fechaSalida)
    }

    var errorFechas by remember(reserva.id) {
        mutableStateOf<String?>(null)
    }

    AlertDialog(
        onDismissRequest = onCerrar,

        title = {
            Text("Editar reserva")
        },

        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = nombreHuesped,
                    onValueChange = {
                        nombreHuesped = it
                    },
                    label = {
                        Text("Nombre del huésped")
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = fechaEntrada,
                    onValueChange = {
                        fechaEntrada = it
                        errorFechas = null
                    },
                    label = {
                        Text("Fecha de entrada")
                    },
                    placeholder = {
                        Text("AAAA-MM-DD")
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = fechaSalida,
                    onValueChange = {
                        fechaSalida = it
                        errorFechas = null
                    },
                    label = {
                        Text("Fecha de salida")
                    },
                    placeholder = {
                        Text("AAAA-MM-DD")
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                errorFechas?.let { mensaje ->
                    Text(
                        text = mensaje,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        },

        confirmButton = {
            TextButton(
                onClick = {
                    val error = validarFechasReserva(
                        fechaEntrada.trim(),
                        fechaSalida.trim()
                    )

                    if (error == null) {
                        onGuardar(
                            reserva.copy(
                                nombreHuesped = nombreHuesped.trim(),
                                fechaEntrada = fechaEntrada.trim(),
                                fechaSalida = fechaSalida.trim()
                            )
                        )
                    } else {
                        errorFechas = error
                    }
                },
                enabled =
                    nombreHuesped.isNotBlank() &&
                            fechaEntrada.isNotBlank() &&
                            fechaSalida.isNotBlank()
            ) {
                Text("Guardar cambios")
            }
        },

        dismissButton = {
            TextButton(
                onClick = onCerrar
            ) {
                Text("Cancelar")
            }
        }
    )
}
