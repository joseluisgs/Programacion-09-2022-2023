package dev.joseluisgs.routesmanagerfx.controllers

import dev.joseluisgs.routesmanagerfx.data.parameters.DemoParameterState
import dev.joseluisgs.routesmanagerfx.data.viewmodels.DemoViewModel
import dev.joseluisgs.routesmanagerfx.routes.RoutesManager
import javafx.application.Platform
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.control.*
import java.util.*


class HelloController {
    @FXML
    private lateinit var welcomeText: Label

    @FXML
    private lateinit var botonIr: Button

    @FXML
    private lateinit var botonCambiarTema: Button

    @FXML
    private lateinit var textViewModel: TextField

    @FXML
    private fun initialize() {
        // Lo ponemos en español
        Locale.setDefault(Locale("es", "ES"))
        welcomeText.text = "Hola Enrutador :)"

        // evento propios no definidos en el FXML
        botonIr.setOnAction { onBotonIrClick() }
        // Esta vez le voy a pasar el evento it = e -> ActionEvent
        botonCambiarTema.setOnAction { onBotonCambiarTemaClick(it) }

        // Enlazamos el modelo de datos a la vista, Si cambia el modelo cambia la vista y viceversa
        textViewModel.textProperty().bindBidirectional(DemoViewModel.mensaje)
    }

    // Evento definido en el FXML
    @FXML
    private fun onHelloButtonClick() {
        welcomeText.text = "Esto es JavaFX y has pulsado el botón"
    }


    private fun onBotonIrClick() {
        println("Boton Ir pulsado")
        // antes de abrir relleno los datos a compartir
        DemoParameterState.data = "Te mando esto desde Stage Prueba"
        DemoParameterState.number = 10
        RoutesManager.initPruebaEscenasStage()
    }

    private fun onBotonCambiarTemaClick(event: ActionEvent) {
        // voy a sacar los datos del evento por curiosidad
        println("Boton Cambiar pulsado cambiando de tema")
        println(event.toString())
        // Para cambiar el rema debemos hacer lo mismo que con la escena
        // es decir, detectar el stage y cambiar el tema
        RoutesManager.changeStyle(style = RoutesManager.Style.DAM)
    }

    fun onOnCloseAction() {
        val alert = Alert(Alert.AlertType.CONFIRMATION)
        alert.title = "Salir"
        alert.contentText = "¿Salir de Prueba?"
        val result = alert.showAndWait()
        if (result.get() == ButtonType.OK) {
            Platform.exit() // O System.exit(0)
        } else {
            alert.close()
        }
    }
}