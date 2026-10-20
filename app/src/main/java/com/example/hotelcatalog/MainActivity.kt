package com.example.hotelcatalog

import android.os.Bundle
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
import com.example.hotelcatalog.ui.theme.HotelAvailable
import com.example.hotelcatalog.ui.theme.HotelCatalogTheme
import com.example.hotelcatalog.ui.theme.HotelGold
import com.example.hotelcatalog.ui.theme.HotelReserved
import com.example.hotelcatalog.ui.theme.HotelUnavailable
import com.example.hotelcatalog.ui.theme.HotelWhite
import java.util.Locale


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

        setContent {

            HotelCatalogApp()
        }
    }
}


// =====================================================
// CONTROL GENERAL DEL TEMA
// =====================================================

@Composable
fun HotelCatalogApp() {

    val temaSistemaOscuro = isSystemInDarkTheme()

    var temaOscuro by remember {
        mutableStateOf(temaSistemaOscuro)
    }


    HotelCatalogTheme(
        darkTheme = temaOscuro
    ) {

        BenveniuHotelApp(
            temaOscuro = temaOscuro,

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
    onCambiarTema: () -> Unit
) {

    val navController = rememberNavController()


    // =================================================
    // ESTADO COMPARTIDO
    // =================================================

    var noches by remember {
        mutableStateOf(1)
    }


    var habitacionesReservadas by remember {
        mutableStateOf(setOf<String>())
    }


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

                        if (
                            habitacion.nombre
                            in habitacionesReservadas
                        ) {

                            habitacionesReservadas =
                                habitacionesReservadas -
                                        habitacion.nombre

                        } else if (
                            habitacion.disponible
                        ) {

                            habitacionesReservadas =
                                habitacionesReservadas +
                                        habitacion.nombre
                        }
                    }
                )
            }


            // =================================================
            // RESERVAS
            // =================================================

            composable("reservas") {

                ReservasScreen(
                    habitaciones =
                        habitaciones,

                    habitacionesReservadas =
                        habitacionesReservadas,

                    noches =
                        noches,

                    onCancelarReserva = { habitacion ->

                        habitacionesReservadas =
                            habitacionesReservadas -
                                    habitacion.nombre
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
    noches: Int,
    onCancelarReserva: (Habitacion) -> Unit,
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

    val totalGeneral =
        reservas.sumOf {

            it.precio * noches
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
            reservas.isEmpty()
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
                                "Resumen de la reserva",

                            style =
                                MaterialTheme.typography.titleLarge,

                            fontWeight =
                                FontWeight.Bold,

                            color =
                                MaterialTheme.colorScheme.onSurface
                        )


                        Text(
                            text =
                                "Habitaciones: ${reservas.size}",

                            color =
                                MaterialTheme.colorScheme.onSurface
                        )


                        Text(
                            text =
                                if (
                                    noches == 1
                                ) {

                                    "Estadía: 1 noche"

                                } else {

                                    "Estadía: $noches noches"
                                },

                            color =
                                MaterialTheme.colorScheme.onSurface
                        )


                        Text(
                            text =
                                "Total: ${formatearPrecio(totalGeneral)} COP",

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


            // =================================================
            // CARDS DE RESERVAS
            // =================================================

            items(
                reservas
            ) { habitacion ->

                HabitacionCard(
                    habitacion =
                        habitacion,

                    noches =
                        noches,

                    reservada =
                        true,

                    onCambiarReserva = {

                        onCancelarReserva(
                            habitacion
                        )
                    }
                )
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
    onCambiarReserva: () -> Unit
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
                    text =
                        when {

                            reservada ->

                                "En reserva"

                            habitacion.disponible ->

                                "Disponible"

                            else ->

                                "No disponible"
                        },

                    fontWeight =
                        FontWeight.Bold,

                    color =
                        when {

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