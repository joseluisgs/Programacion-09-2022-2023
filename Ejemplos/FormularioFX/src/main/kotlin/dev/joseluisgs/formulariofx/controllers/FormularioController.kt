package dev.joseluisgs.formulariofx.controllers

import javafx.fxml.FXML
import javafx.scene.control.*
import java.time.LocalDate
import java.util.*
import kotlin.system.exitProcess


class FormularioController {

    // Importamos los elementos de la vista
    @FXML
    private lateinit var textNombre: TextField

    @FXML
    private lateinit var textPassword: TextField

    @FXML
    private lateinit var radioRepetidor: RadioButton

    @FXML
    private lateinit var dateNacimiento: DatePicker

    @FXML
    private lateinit var botonAceptar: Button

    @FXML
    private lateinit var botonCerrar: Button


    // Aquí inicializamos los valores por defecto si queremos
    @FXML
    fun initialize() {
        // Inicializamos los valores por defecto
        // Lo ponemos todo en español
        Locale.setDefault(Locale("es", "ES"))
        textNombre.text = "José Luis"
        textPassword.text = "1234"
        dateNacimiento.value = LocalDate.now()
        radioRepetidor.isSelected = false

        // Definimos los eventos
        botonAceptar.setOnAction { aceptar() }
        botonCerrar.setOnAction { cerrar() }

    }

    private fun cerrar() {
        // Cerramos la aplicación
        exitProcess(0)
    }


    @FXML
    fun aceptar() {
        // Mostramos los datos por consola
        println("Nombre: ${textNombre.text}")
        println("Password: ${textPassword.text}")
        println("Fecha de nacimiento: ${dateNacimiento.value}")
        println("Repetidor: ${radioRepetidor.isSelected}")

        // Podemos sacar un Alert
        Alert(Alert.AlertType.INFORMATION)
            .apply {
                title = "Información"
                headerText = "Datos del formulario"
                contentText = "Nombre: ${textNombre.text} \n" +
                        "Password: ${textPassword.text} \n" +
                        "Fecha de nacimiento: ${dateNacimiento.value} \n" +
                        "Repetidor: ${if (radioRepetidor.isSelected) "Sí" else "No"}"
            }.showAndWait()
    }
}