package dev.joseluisgs.moscafx.controllers

import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import dev.joseluisgs.moscafx.routes.RoutesManager
import dev.joseluisgs.moscafx.viewmodel.Acertado
import dev.joseluisgs.moscafx.viewmodel.MoscaError
import dev.joseluisgs.moscafx.viewmodel.MoscaViewModel
import javafx.application.Platform
import javafx.fxml.FXML
import javafx.scene.control.*
import javafx.scene.control.Alert.AlertType
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import mu.KotlinLogging
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


private val logger = KotlinLogging.logger {}

class MoscaViewConroller : KoinComponent {
    // Mi ViewModel, debemos inyectarlo bien :) o hacer el get() de Koin si no lo queremos lazy
    val viewModel: MoscaViewModel by inject()

    // Menus
    @FXML
    private lateinit var menuSalir: MenuItem

    @FXML
    private lateinit var menuAcercaDe: MenuItem


    @FXML
    private lateinit var spinnerDimension: Spinner<Int>

    @FXML
    private lateinit var spinnerIntentos: Spinner<Int>

    @FXML
    private lateinit var spinnerFila: Spinner<Int>

    @FXML
    private lateinit var spinnerColumna: Spinner<Int>

    @FXML
    private lateinit var textCuadrante: TextArea

    @FXML
    private lateinit var textGolpes: TextField

    // Botones
    @FXML
    lateinit var botonComenzar: Button

    @FXML
    lateinit var botonGolpear: Button

    // Paneles
    @FXML
    lateinit var panelJugar: HBox

    @FXML
    lateinit var panelConfiguracion: VBox

    @FXML
    fun initialize() {
        logger.info { "Inicializando Mosca Controller FXML" }


        // Eventos de los menus
        menuSalir.setOnAction { onOnCloseAction() }
        menuAcercaDe.setOnAction { onAcercaDeAction() }

        // Eventos de los botones
        botonComenzar.setOnAction { onComenzarAction() }
        botonGolpear.setOnAction { onGolpearAction() }

        // Iniciamos los spinners
        spinnerDimension.valueFactory = SpinnerValueFactory.IntegerSpinnerValueFactory(3, 10, 3)
        spinnerIntentos.valueFactory = SpinnerValueFactory.IntegerSpinnerValueFactory(1, 20, 3)

        // Osculto el panel de jugar
        panelJugar.isVisible = false

        // Binding del cuadrante
        textCuadrante.textProperty().bind(viewModel.state.cuadrante)

        // Binding de los golpes
        textGolpes.textProperty().bind(viewModel.state.golpes.asString())
    }

    private fun onComenzarAction() {
        // Cerramos el panel de comenzar
        panelConfiguracion.isVisible = false
        // Mostramos el panel de jugar
        panelJugar.isVisible = true
        // Cogemos las dimensiones
        val dimension = spinnerDimension.value.toInt()
        val intentos = spinnerIntentos.value.toInt()

        // Ponemos filas y columnas como dimensiones
        spinnerFila.valueFactory = SpinnerValueFactory.IntegerSpinnerValueFactory(1, dimension, 1)
        spinnerColumna.valueFactory = SpinnerValueFactory.IntegerSpinnerValueFactory(1, dimension, 1)

        // Iniciamos el juego
        viewModel.iniciarJuego(dimension, intentos)
    }

    private fun onGolpearAction() {
        logger.info { "Golpeando" }
        // Cogemos las fila y columna
        val fila = spinnerFila.value.toInt() - 1
        val columna = spinnerColumna.value.toInt() - 1
        // Golpeamos
        viewModel.golpear(fila, columna).onSuccess {
            accionAcertar(it)
        }.onFailure {
            accionFallar(it)
        }
    }

    private fun accionFallar(error: MoscaError) {
        logger.info { "Acción de fallo: $error" }
        when (error) {
            is MoscaError.FinIntentos -> finIntentosAction()
            is MoscaError.NoAcertado -> noAcertadoAction()
            is MoscaError.Casi -> casiAction(error)
        }
    }

    private fun casiAction(error: MoscaError.Casi) {
        logger.info { "Casi: $error" }
        Alert(AlertType.WARNING).apply {
            title = "Casi lo has logrado"
            contentText =
                "¡Casi lo has logrado!\nLa mosca estaba cerca de la posición:\nFila: ${error.fila + 1}\nColumna: ${error.columna + 1}.\nLlevas ${error.intentos} intentos.\nLa mosca se ha movido a una nueva posición"
            showAndWait()
        }
    }

    private fun noAcertadoAction() {
        logger.info { "No acertado" }
        Alert(AlertType.WARNING).apply {
            title = "No acertado"
            contentText = "¡Maldita Mosca!. No has acertado, sigue intentándolo"
            showAndWait()
        }
    }

    private fun finIntentosAction() {
        logger.info { "Fin de intentos" }
        Alert(AlertType.ERROR).apply {
            title = "Fin de intentos"
            contentText =
                "¡Vaya!\nSe han acabado los intentos, la mosca estaba en la posición\nFila: ${viewModel.state.moscaPosition.fila + 1}\nColumna: ${viewModel.state.moscaPosition.columna + 1}"
            showAndWait()
        }.also {
            // Cerramos el panel de jugar
            panelJugar.isVisible = false
            // Mostramos el panel de comenzar
            panelConfiguracion.isVisible = true
        }
    }

    private fun accionAcertar(acierto: Acertado) {
        logger.info { "Acción de acerto: $acierto" }
        Alert(AlertType.INFORMATION).apply {
            title = "Acertado"
            contentText =
                "¡Has acertado!\nLa mosca estaba en la posición\nFila: ${acierto.mosca.fila + 1}\nColumna: ${acierto.mosca.columna + 1}.\nHas necesitado ${acierto.intentos} intentos"
            showAndWait()
        }.also {
            // Cerramos el panel de jugar
            panelJugar.isVisible = false
            // Mostramos el panel de comenzar
            panelConfiguracion.isVisible = true
        }
    }


    private fun onAcercaDeAction() {

        RoutesManager.initAcercaDeStage()
    }


    // Método para salir de la aplicación
    fun onOnCloseAction() {
        logger.debug { "onOnCloseAction" }

        val alert = Alert(AlertType.CONFIRMATION)
        alert.title = "Salir"
        alert.contentText = "¿Desea salir de Mosca DAM?"
        val result = alert.showAndWait()
        if (result.get() == ButtonType.OK) {
            Platform.exit() // O System.exit(0)
        } else {
            alert.close()
        }
    }
}
