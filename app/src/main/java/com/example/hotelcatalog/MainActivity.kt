package com.example.hotelcatalog

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.hotelcatalog.ui.theme.HotelCatalogTheme


// =====================================================
// MODELO DE DATOS
// =====================================================

data class Habitacion(
    val nombre: String,
    val descripcion: String,
    val precio: String,
    val capacidad: String,
    val disponible: Boolean,
    val imagen: Int
)


// =====================================================
// MAIN ACTIVITY
// =====================================================

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            HotelCatalogTheme {

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = Color(0xFFF8F5F0)
                ) { innerPadding ->

                    CatalogoHotel(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}


// =====================================================
// CATÁLOGO DEL HOTEL
// =====================================================

@Composable
fun CatalogoHotel(modifier: Modifier = Modifier) {

    val habitaciones = listOf(

        Habitacion(
            nombre = "Habitación Individual",
            descripcion = "1 cama individual • WiFi • TV • Baño privado",
            precio = "120.000 COP / noche",
            capacidad = "1 persona",
            disponible = true,
            imagen = R.drawable.habitacion_individual
        ),

        Habitacion(
            nombre = "Habitación Doble",
            descripcion = "2 camas • WiFi • TV • Aire acondicionado",
            precio = "180.000 COP / noche",
            capacidad = "2 personas",
            disponible = true,
            imagen = R.drawable.habitacion_doble
        ),

        Habitacion(
            nombre = "Habitación Matrimonial",
            descripcion = "Cama doble • WiFi • TV • Minibar",
            precio = "210.000 COP / noche",
            capacidad = "2 personas",
            disponible = false,
            imagen = R.drawable.habitacion_matrimonial
        ),

        Habitacion(
            nombre = "Habitación Familiar",
            descripcion = "3 camas • WiFi • TV • Desayuno incluido",
            precio = "280.000 COP / noche",
            capacidad = "4 personas",
            disponible = true,
            imagen = R.drawable.habitacion_familiar
        ),

        Habitacion(
            nombre = "Suite Presidencial",
            descripcion = "Cama King • Jacuzzi • Balcón • Sala privada",
            precio = "450.000 COP / noche",
            capacidad = "2 personas",
            disponible = true,
            imagen = R.drawable.suite_presidencial
        )
    )


    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {

        // =====================================================
        // ENCABEZADO
        // =====================================================

        Text(
            text = "Benveniu Hotel",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF4E342E)
        )

        Spacer(
            modifier = Modifier.height(6.dp)
        )

        Text(
            text = "Catálogo de Habitaciones",
            style = MaterialTheme.typography.titleMedium,
            color = Color(0xFF8D6E63)
        )

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        Text(
            text = "Descubre espacios diseñados para el descanso, la elegancia y la comodidad.",
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFF5D4037)
        )

        Spacer(
            modifier = Modifier.height(24.dp)
        )


        // =====================================================
        // GENERACIÓN DE CARDS MEDIANTE FOR
        // =====================================================

        for (habitacion in habitaciones) {

            HabitacionCard(
                habitacion = habitacion
            )

            Spacer(
                modifier = Modifier.height(18.dp)
            )
        }
    }
}


// =====================================================
// CARD REUTILIZABLE
// =====================================================

@Composable
fun HabitacionCard(habitacion: Habitacion) {

    val colorDisponible = if (habitacion.disponible) {
        Color(0xFF2E7D32)
    } else {
        Color(0xFFC62828)
    }

    val textoDisponible = if (habitacion.disponible) {
        "Disponible"
    } else {
        "No disponible"
    }


    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),

        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFD7B899)
        ),

        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        )
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),

            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            // =====================================================
            // IMAGEN
            // =====================================================

            Image(
                painter = painterResource(
                    id = habitacion.imagen
                ),

                contentDescription = habitacion.nombre,

                modifier = Modifier
                    .fillMaxWidth()
                    .height(210.dp)
                    .clip(
                        RoundedCornerShape(16.dp)
                    ),

                contentScale = ContentScale.Crop
            )


            Spacer(
                modifier = Modifier.height(6.dp)
            )


            // =====================================================
            // NOMBRE
            // =====================================================

            Text(
                text = habitacion.nombre,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF3E2723)
            )


            // =====================================================
            // DESCRIPCIÓN
            // =====================================================

            Text(
                text = habitacion.descripcion,
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF4E342E)
            )


            // =====================================================
            // CAPACIDAD
            // =====================================================

            Text(
                text = "Capacidad: ${habitacion.capacidad}",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF5D4037)
            )


            // =====================================================
            // PRECIO
            // =====================================================

            Text(
                text = habitacion.precio,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF6D4C41)
            )


            // =====================================================
            // DISPONIBILIDAD
            // =====================================================

            Text(
                text = textoDisponible,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = colorDisponible
            )


            Spacer(
                modifier = Modifier.height(4.dp)
            )


            // =====================================================
            // BOTÓN
            // =====================================================

            Button(
                onClick = {
                    // Más adelante podemos agregar una acción.
                },

                shape = RoundedCornerShape(12.dp),

                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF6D4C41)
                )
            ) {

                Text(
                    text = "Ver detalles",
                    color = Color.White
                )
            }
        }
    }
}