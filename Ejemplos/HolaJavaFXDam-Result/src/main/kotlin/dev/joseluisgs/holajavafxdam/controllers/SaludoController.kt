package dev.joseluisgs.holajavafxdam.controllers

import com.github.michaelbull.result.andThen
import com.github.michaelbull.result.mapBoth
import dev.joseluisgs.holajavafxdam.models.Alumno
import dev.joseluisgs.holajavafxdam.repositories.AlumnosRepository
import dev.joseluisgs.holajavafxdam.validators.edadValidator
import dev.joseluisgs.holajavafxdam.validators.emailValidator
import dev.joseluisgs.holajavafxdam.validators.nombreValidator
import javafx.fxml.FXML
import javafx.scene.control.Alert
import javafx.scene.control.Button
import javafx.scene.control.TextField


private val logger = mu.KotlinLogging.logger { }

class SaludoController {
    private val alumnosRepository = AlumnosRepository()

    @FXML
    private lateinit var textNombre: TextField

    @FXML
    private lateinit var textEdad: TextField

    @FXML
    private lateinit var textEmail: TextField

    @FXML
    lateinit var botonLimpiar: Button

    @FXML
    lateinit var botonSaludar: Button


    init {
        // No funciona, no se ha inicializado la vista
        // botonSaludo.setOnAction { onHelloButtonClick() }
        logger.info { "Inicializando controlador..." }
    }

    @FXML
    fun initialize() {
        logger.info { "Inicializando vista..." }

        // Configurar valores por defecto
        limpiarCamposFormulario()

        // configurar los eventos
        botonSaludar.setOnAction { onBotonSaludarClick() }
        botonLimpiar.setOnAction { onBotonLimpiarClick() }
    }

    private fun limpiarCamposFormulario() {
        textNombre.text = ""
        textEdad.text = ""
        textEmail.text = ""
    }

    private fun onBotonLimpiarClick() {
        logger.info { "Limpiando..." }
        limpiarCamposFormulario()
    }

    private fun onBotonSaludarClick() {
        logger.info { "Saludando..." }
        logger.info { "Hola ${textNombre.text}, tienes ${textEdad.text} años" }

        // Validamos con cadenas de Result!!!
        nombreValidator(textNombre.text).andThen { nombre ->
            edadValidator(textEdad.text).andThen { edad ->
                emailValidator(textEmail.text).andThen { email ->
                    alumnosRepository.save(Alumno(nombre, edad, email))
                }
            }
        }.mapBoth(
            success = {
                logger.info { "Alumno guardado correctamente" }
                showAlertDialog(
                    Alert.AlertType.INFORMATION,
                    "Alumno guardado en almacenamiento",
                    "Alumno guardado",
                    "Se ha guardado el alumno ${it.nombre} correctamente"
                )
            },
            failure = {
                logger.error(it.mensaje)
                showAlertDialog(
                    Alert.AlertType.ERROR,
                    "Error al guardar el alumno en almacenamiento",
                    "Error al guardar",
                    it.mensaje
                )
            }
        )
    }

    private fun showAlertDialog(
        type: Alert.AlertType = Alert.AlertType.CONFIRMATION,
        titleAlert: String,
        headerAlert: String?,
        contentAlert: String
    ) {
        Alert(type).apply {
            headerText = headerAlert
            title = titleAlert
            contentText = contentAlert
        }.run {
            showAndWait()
        }
    }
}