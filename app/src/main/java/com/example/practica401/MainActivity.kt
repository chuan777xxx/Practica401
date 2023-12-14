package com.example.practica401

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.practica401.ui.theme.Practica401Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Practica401Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    App()
                }
            }
        }
    }
}

class Alumno(var notas: DoubleArray = DoubleArray(0)) {

}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun App() {
    var alumno by remember { mutableStateOf(Alumno()) }
    var notasString by remember { mutableStateOf("") }
    var media by remember { mutableStateOf(0.0) }
    var mostrarNotasMasAltas by remember { mutableStateOf(false) }
    var mostrarMedia by remember { mutableStateOf(false) }
    var notasMasAltasText by remember { mutableStateOf("") }
    var mostrarSeccionBorrarNotas by remember { mutableStateOf(false) }
    var mensajeError: String by remember { mutableStateOf("") }
    var mensajeErrorExcepcion: String by remember { mutableStateOf("") }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Agregar notas
        TextField(
            value = notasString,
            onValueChange = {
                notasString = it
            },
            label = { Text("Introduce las notas separadas por comas") },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    val notasArray =
                        notasString.split(",").map { it.trim().toDouble() }.toDoubleArray()
                    alumno = Alumno(notasArray)
                }
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        Button(
            onClick = {
                val notasArray = notasString.split(",").map {
                    it.trim().toDoubleOrNull()
                }

                if (notasArray.all { it != null }) {
                    // Todas las notas son números válidos
                    alumno = Alumno(notasArray.requireNoNulls().toDoubleArray())

                    // Vaciar el contenido del TextField después de enviar las notas
                    notasString = ""

                    // Reiniciar la visibilidad de las secciones
                    mostrarNotasMasAltas = false
                    mostrarMedia = false
                    mensajeErrorExcepcion = ""
                } else {
                    // Mostrar un mensaje de error si alguna nota no es un número válido
                    mensajeErrorExcepcion = "Ingrese solo números válidos separados por comas."
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        ) {
            Text("Enviar notas")
        }
        // Mostrar el array de notas introducido
        if (alumno.notas.isNotEmpty()) {
            Text(
                text = "Array de notas: ${alumno.notas.joinToString(", ")}",
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
        if (mensajeErrorExcepcion.isNotEmpty()) {
            Text(
                text = mensajeErrorExcepcion,
                color = Color.Red,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        // Botón para sacar la nota más alta y su posición
        Button(
            onClick = {
                if (alumno.notas.isNotEmpty()) {
                    var maxNota = alumno.notas[0]
                    var posicionMax = 0

                    for (i in 1 until alumno.notas.size) {
                        if (alumno.notas[i] > maxNota) {
                            maxNota = alumno.notas[i]
                            posicionMax = i
                        }
                    }

                    // Actualizar el estado con las notas más altas
                    notasMasAltasText = "Notas más altas: $maxNota"

                    // Mostrar la sección de notas más altas
                    mostrarNotasMasAltas = true
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        ) {
            Text("Obtener nota más alta y posición")
        }


        // Botón para calcular la media sin la nota más alta y más baja
        Button(
            onClick = {
                if (alumno.notas.isNotEmpty()) {
                    var maxNota = alumno.notas[0]
                    var minNota = alumno.notas[0]
                    var sumaNotas = 0.0
                    var cantidadNotas = 0

                    // Encontrar la nota más alta y más baja, y calcular la suma de las notas
                    for (nota in alumno.notas) {
                        if (nota > maxNota) {
                            maxNota = nota
                        }
                        if (nota < minNota) {
                            minNota = nota
                        }
                        sumaNotas += nota
                        cantidadNotas++
                    }

                    // Restar la nota más alta y más baja de la suma
                    sumaNotas -= maxNota
                    sumaNotas -= minNota
                    cantidadNotas -= 2 // Restar dos porque eliminamos dos notas (más alta y más baja)

                    // Calcular la media sin la nota más alta y más baja
                    val promedio = if (cantidadNotas > 0) {
                        sumaNotas / cantidadNotas.toDouble()
                    } else {
                        // Manejar el caso cuando hay menos de dos notas
                        0.0
                    }

                    // Mostrar la sección de la media
                    mostrarMedia = true
                    media = promedio
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        ) {
            Text("Calcular media sin la nota más alta y más baja")
        }


        // Texto para mostrar las notas más altas
        if (mostrarNotasMasAltas) {
            Text(notasMasAltasText)
        }

        // Texto para mostrar la media
        if (mostrarMedia) {
            Text("Media: $media")
        }

        // Botón para mostrar la sección de borrar notas
        Button(
            onClick = {
                mostrarSeccionBorrarNotas = true
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        ) {
            Text("Mostrar sección de borrar notas")
        }

// Sección de borrar notas condicionalmente visible
        if (mostrarSeccionBorrarNotas) {
            // Campo de texto para introducir la posición de la nota a borrar
            var posicionBorrar by remember { mutableStateOf("") }

            TextField(
                value = posicionBorrar,
                onValueChange = {
                    posicionBorrar = it
                },
                label = { Text("Introduce la posición de la nota a borrar") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )
            // Botón para borrar la nota de la posición especificada
            Button(
                onClick = {
                    // Al hacer clic en el botón, intenta borrar la nota en la posición especificada
                    val posicion = posicionBorrar.toIntOrNull()
                    if (posicion != null && posicion >= 0 && posicion < alumno.notas.size) {
                        alumno.notas = alumno.notas.filterIndexed { index, _ -> index != posicion }.toDoubleArray()
                        // Restablecer el campo de texto después de borrar
                        posicionBorrar = ""
                    } else {
                        mensajeError = "Posición inválida"
                    }
                    posicionBorrar=""
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            ) {
                Text("Borrar nota en la posición especificada")
            }
            if (mensajeError.isNotEmpty()) {
                Text(
                    text = mensajeError,
                    color = Color.Red,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
            Button(
                onClick = {
                    alumno = Alumno()
                    mostrarMedia=false
                    mostrarNotasMasAltas=false
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            ) {
                Text("Borrar todas las notas")
            }
        }
    }
}
