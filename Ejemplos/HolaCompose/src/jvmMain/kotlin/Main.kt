import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

// La función App() es la función que se ejecuta en la aplicación.
// Composable que se renderiza en la pantalla.
@Composable
@Preview
fun App() {
    // Creamos un estado mutable para el texto.
    var textoInput by remember { mutableStateOf("") }
    var textSaludo by remember { mutableStateOf("") }

    // Usamos el tema de Material Design.
    MaterialTheme {
        // usamos una columna para colocar los elementos uno debajo de otro.
        Column {
            // usamos una Fila para colocar los elementos.
            // y la centramos en la pantalla.
            Row(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth(),
                // .background(Color.Gray),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Creamos un Texto que muestra el texto del estado.
                Text(text = "Introduce tu nombre: ")
                // Creamos un InputText que modifica el texto del estado.
                TextField(
                    value = textoInput, // valor inicial
                    onValueChange = { textoInput = it }, // función que se ejecuta cuando cambia el valor
                    //label = { Text("Nombre") } // texto de la etiqueta
                )
                // Creamos un botón que modifica el texto del estado. al ser pulsado
                Button(onClick = { textSaludo = "Hola Compose: $textoInput" }) {
                    Text(text = "Pulsame") // texto del botón
                }
            }
            // añadimos un separador
            Row(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth(),
                // .background(Color.Gray),
                horizontalArrangement = Arrangement.Center,
            ) {
                // Creamos un Texto que muestra el texto del estado.
                Text(text = textSaludo)
            }
        }

    }
}

// La función main() es la función de entrada de la aplicación.
fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        // Llamamos a la función App() que hemos definido arriba.
        App()
    }
}
