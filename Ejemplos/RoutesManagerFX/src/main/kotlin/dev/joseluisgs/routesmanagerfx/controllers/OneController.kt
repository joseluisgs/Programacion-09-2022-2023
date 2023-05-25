package dev.joseluisgs.routesmanagerfx.controllers

import dev.joseluisgs.routesmanagerfx.data.parameters.DemoParameterState
import dev.joseluisgs.routesmanagerfx.data.viewmodels.DemoViewModel
import dev.joseluisgs.routesmanagerfx.routes.RoutesManager
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.stage.Stage

class OneController {
    @FXML
    private lateinit var botonCambiar: Button

    @FXML
    private lateinit var datosLabel: Label

    @FXML
    private lateinit var textViewModel: TextField

    @FXML
    private fun initialize() {
        // evento del boton
        botonCambiar.setOnAction { onBotonCambiarClick() }

        // Recogemos los datos que nos pasan
        datosLabel.text = DemoParameterState.data + " " + DemoParameterState.number

        // Enlazamos el modelo de datos a la vista, Si cambia el modelo cambia la vista y viceversa
        textViewModel.textProperty().bindBidirectional(DemoViewModel.mensaje)
    }

    private fun onBotonCambiarClick() {
        println("Boton Cambiar pulsado cambiando de 1 -> 2")
        // Sobre que Stage?
        // Somo estamos fuera de la ventana principal, no es MainStage
        // Debemos saber que NodoRaiz o parent en el que estamos
        // un truco es usar un elemento que tengamos
        val stage = botonCambiar.scene.window as Stage
        // RoutesManager.changeScene(stage, RoutesManager.Views.TWO)
        // o podemos usar el activeStage
        //RoutesManager.changeScene(RoutesManager.activeStage, RoutesManager.Views.TWO)
        // O el active stage que ya esta definido como parámetro por defecto
        // Le vamos a pasar un parámetro
        DemoParameterState.data = "Te mando esto desde la escena 1"
        RoutesManager.changeScene(view = RoutesManager.View.TWO)
    }
}
