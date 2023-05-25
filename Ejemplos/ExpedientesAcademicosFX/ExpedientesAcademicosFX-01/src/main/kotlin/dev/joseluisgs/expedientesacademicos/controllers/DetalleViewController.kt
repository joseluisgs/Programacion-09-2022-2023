package dev.joseluisgs.expedientesacademicos.controllers

import com.github.michaelbull.result.*
import dev.joseluisgs.expedientesacademicos.errors.AlumnoError
import dev.joseluisgs.expedientesacademicos.routes.RoutesManager
import dev.joseluisgs.expedientesacademicos.viewmodels.ExpedientesViewModel
import dev.joseluisgs.expedientesacademicos.viewmodels.ExpedientesViewModel.TipoOperacion.EDITAR
import dev.joseluisgs.expedientesacademicos.viewmodels.ExpedientesViewModel.TipoOperacion.NUEVO
import javafx.fxml.FXML
import javafx.scene.control.*
import javafx.scene.control.Alert.AlertType
import javafx.scene.image.ImageView
import javafx.stage.FileChooser
import mu.KotlinLogging
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.time.LocalDate


private val logger = KotlinLogging.logger {}

class DetalleViewController : KoinComponent {
    // Inyectamos nuestro ViewModel
    val viewModel: ExpedientesViewModel by inject()

    // Botones
    @FXML
    private lateinit var btnGuardar: Button

    @FXML
    private lateinit var btnLimpiar: Button

    @FXML
    private lateinit var btnCancelar: Button

    // Formulario del alumno
    @FXML
    private lateinit var textAlumnoNumero: TextField

    @FXML
    private lateinit var textAlumnoApellidos: TextField

    @FXML
    private lateinit var textAlumnoNombre: TextField

    @FXML
    private lateinit var textAlumnoEmail: TextField

    @FXML
    private lateinit var dateAlumnoFechaNacimiento: DatePicker

    @FXML
    private lateinit var textAlumnoCalificacion: TextField

    @FXML
    private lateinit var checkAlumnoRepetidor: CheckBox

    @FXML
    private lateinit var imageAlumno: ImageView

    @FXML
    fun initialize() {
        logger.debug { "Inicializando DetalleViewController FXML en Modo: ${viewModel.state.tipoOperacion}" }

        textAlumnoNumero.isEditable = false // No se puede editar el número

        // Iniciamos los bindings
        initBindings()

        // Iniciamos los eventos
        initEventos()
    }


    private fun initEventos() {
        // Botones
        btnGuardar.setOnAction {
            onGuardarAction()
        }
        btnLimpiar.setOnAction {
            onLimpiarAction()
        }
        btnCancelar.setOnAction {
            onCancelarAction()
        }

        imageAlumno.setOnMouseClicked {
            onImageAction()
        }
    }

    private fun onImageAction() {
        logger.debug { "onImageAction" }
        // Abrimos un diálogo para seleccionar una imagen, esta vez lo he hecho más compacto!!!
        // Comparalo con los de Json!!!
        FileChooser().run {
            title = "Selecciona una imagen"
            extensionFilters.addAll(FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg"))
            showOpenDialog(RoutesManager.activeStage)
        }?.let {
            viewModel.updateImageAlumnoOperacion(it)
        }

    }

    private fun initBindings() {
        // Formulario, ahora si es doble binding, porque va y viene
        textAlumnoNumero.textProperty().bindBidirectional(viewModel.state.alumnoOperacion.numero)
        textAlumnoApellidos.textProperty().bindBidirectional(viewModel.state.alumnoOperacion.apellidos)
        textAlumnoNombre.textProperty().bindBidirectional(viewModel.state.alumnoOperacion.nombre)
        textAlumnoEmail.textProperty().bindBidirectional(viewModel.state.alumnoOperacion.email)
        dateAlumnoFechaNacimiento.valueProperty().bindBidirectional(viewModel.state.alumnoOperacion.fechaNacimiento)
        textAlumnoCalificacion.textProperty().bindBidirectional(viewModel.state.alumnoOperacion.calificacion)
        checkAlumnoRepetidor.selectedProperty().bindBidirectional(viewModel.state.alumnoOperacion.repetidor)
        imageAlumno.imageProperty().bindBidirectional(viewModel.state.alumnoOperacion.imagen)
    }

    private fun onGuardarAction() {
        logger.debug { "onGuardarActio" }
        // Dependiendo del modo
        when (viewModel.state.tipoOperacion) {
            NUEVO -> {
                validateForm().andThen {
                    viewModel.crearAlumno()
                }.onSuccess {
                    logger.debug { "Alumno creado correctamente" }
                    showAlertOperacion(
                        AlertType.INFORMATION,
                        "Alumno creado",
                        "Alumno creado correctamente:\n${it.nombreCompleto}"
                    )
                    cerrarVentana()
                }.onFailure {
                    logger.error { "Error al salvar alumno/a: ${it.message}" }
                    showAlertOperacion(
                        AlertType.ERROR,
                        "Error al salvar alumno/a",
                        "Se ha producido un error al salvar el alumno/a:\n${it.message}"
                    )
                }
            }

            EDITAR -> {
                validateForm().andThen {
                    viewModel.editarAlumno()
                }.onSuccess {
                    logger.debug { "Alumno editado correctamente" }
                    showAlertOperacion(
                        AlertType.INFORMATION,
                        "Alumno editado",
                        "Alumno editado correctamente:\n${it.nombreCompleto}"
                    )
                    cerrarVentana()
                }.onFailure {
                    logger.error { "Error al actualizar alumno/a: ${it.message}" }
                    showAlertOperacion(
                        AlertType.ERROR,
                        "Error al actualizar alumno/a",
                        "Se ha producido un error al actualizar el alumno/a:\n${it.message}"
                    )
                }
            }
        }

    }

    private fun cerrarVentana() {
        // truco coger el stage asociado a un componente
        btnCancelar.scene.window.hide()
    }

    private fun onCancelarAction() {
        logger.debug { "onCancelarAction" }
        viewModel.state.alumnoOperacion.limpiar()
        cerrarVentana()
    }

    private fun onLimpiarAction() {
        logger.debug { "onLimpiarAction" }
        // Limpiamos el estado actual
        viewModel.state.alumnoOperacion.limpiar()
    }

    // Lo puedo hacer aquí o en mi validador en el viewModel
    private fun validateForm(): Result<Unit, AlumnoError> {
        logger.debug { "validateForm" }

        // Validacion del formulario
        if (textAlumnoApellidos.text.isNullOrEmpty()) {
            return Err(AlumnoError.ValidationProblem("Apellidos no puede estar vacío"))
        }
        if (textAlumnoNombre.text.isNullOrEmpty()) {
            return Err(AlumnoError.ValidationProblem("Nombre no puede estar vacío"))
        }
        // Validamos el email, expresión regular
        if (textAlumnoEmail.text.isNullOrEmpty() || !textAlumnoEmail.text.matches(Regex("[a-zA-Z0-9._-]+@[a-zA-Z0-9._-]+\\.[a-zA-Z0-9_-]+"))) {
            return Err(AlumnoError.ValidationProblem("Email no puede estar vacío o no tiene el formato correcto"))
        }
        if (dateAlumnoFechaNacimiento.value == null || dateAlumnoFechaNacimiento.value.isAfter(LocalDate.now())) {
            return Err(AlumnoError.ValidationProblem("Fecha de nacimiento no puede estar vacía y debe ser anterior a hoy"))
        }
        if (textAlumnoCalificacion.text.isNullOrEmpty() || textAlumnoCalificacion.text.replace(",", ".")
                .toDoubleOrNull() == null || textAlumnoCalificacion.text.replace(",", ".")
                .toDouble() < 0 || textAlumnoCalificacion.text.replace(",", ".").toDouble() > 10
        ) {
            return Err(AlumnoError.ValidationProblem("Calificación no puede estar vacía y debe ser un número entre 0 y 10"))
        }
        return Ok(Unit)
    }

    private fun showAlertOperacion(
        alerta: AlertType = AlertType.CONFIRMATION,
        title: String = "",
        mensaje: String = ""
    ) {
        val alert = Alert(alerta)
        alert.title = title
        alert.contentText = mensaje
        alert.showAndWait()
    }


}



