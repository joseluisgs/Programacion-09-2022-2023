package dev.joseluisgs.expedientesacademicos.controllers

import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import dev.joseluisgs.expedientesacademicos.models.Alumno
import dev.joseluisgs.expedientesacademicos.routes.RoutesManager
import dev.joseluisgs.expedientesacademicos.viewmodels.ExpedientesViewModel
import dev.joseluisgs.expedientesacademicos.viewmodels.ExpedientesViewModel.TipoOperacion
import javafx.application.Platform
import javafx.fxml.FXML
import javafx.scene.Cursor.DEFAULT
import javafx.scene.Cursor.WAIT
import javafx.scene.control.*
import javafx.scene.control.Alert.AlertType
import javafx.scene.control.cell.PropertyValueFactory
import javafx.scene.image.ImageView
import javafx.stage.FileChooser
import mu.KotlinLogging
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


private val logger = KotlinLogging.logger {}

class ExpedientesViewConroller : KoinComponent {
    // Inyectamos nuestro ViewModel
    val viewModel: ExpedientesViewModel by inject()

    // Menus
    @FXML
    private lateinit var menuImportar: MenuItem

    @FXML
    private lateinit var menuExportar: MenuItem

    @FXML
    private lateinit var menuZip: MenuItem

    @FXML
    private lateinit var menuUnzip: MenuItem

    @FXML
    private lateinit var menuSalir: MenuItem

    @FXML
    private lateinit var menuAcercaDe: MenuItem

    // Botones
    @FXML
    private lateinit var btnNuevo: Button

    @FXML
    private lateinit var btnEditar: Button

    @FXML
    private lateinit var btnEliminar: Button

    //Combo
    @FXML
    private lateinit var comboTipo: ComboBox<String>

    // Tabla
    @FXML
    private lateinit var tableAlumnos: TableView<Alumno>

    @FXML
    private lateinit var tableColumnNumero: TableColumn<Alumno, Long>

    @FXML
    private lateinit var tableColumNombre: TableColumn<Alumno, String>

    @FXML
    private lateinit var tableColumnCalificacion: TableColumn<Alumno, Double>

    // Buscador
    @FXML
    private lateinit var textBuscador: TextField

    // Estadisticas
    @FXML
    private lateinit var textNumAprobados: TextField

    @FXML
    private lateinit var textNotaMedia: TextField

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

    // Metodo para inicializar
    @FXML
    fun initialize() {
        logger.debug { "Inicializando ExpedientesDeViewController FXML" }

        // Iniciamos los bindings
        initBindings()

        // Iniciamos los eventos
        initEventos()
    }

    private fun initBindings() {
        logger.debug { "Inicializando bindings" }

        // comboBox
        comboTipo.items = viewModel.state.typesRepetidor
        comboTipo.selectionModel.selectFirst()

        // Tabla
        tableAlumnos.items = viewModel.state.alumnos
        // columnas, con el nombre de la propiedad del objeto hará binding
        tableColumnNumero.cellValueFactory = PropertyValueFactory("id")
        tableColumNombre.cellValueFactory = PropertyValueFactory("nombreCompleto")
        tableColumnCalificacion.cellValueFactory = PropertyValueFactory("calificacion")

        // Estadisticas
        textNumAprobados.textProperty().bind(viewModel.state.numAprobados)
        textNotaMedia.textProperty().bind(viewModel.state.notaMedia)

        // Formulario, solo queremos enlazar el alumno, no tocar el estado, por eso es bind
        textAlumnoNumero.textProperty().bind(viewModel.state.alumnoSeleccionado.numero)
        textAlumnoApellidos.textProperty().bind(viewModel.state.alumnoSeleccionado.apellidos)
        textAlumnoNombre.textProperty().bind(viewModel.state.alumnoSeleccionado.nombre)
        textAlumnoEmail.textProperty().bind(viewModel.state.alumnoSeleccionado.email)
        dateAlumnoFechaNacimiento.valueProperty().bind(viewModel.state.alumnoSeleccionado.fechaNacimiento)
        textAlumnoCalificacion.textProperty().bind(viewModel.state.alumnoSeleccionado.calificacion)
        checkAlumnoRepetidor.selectedProperty().bind(viewModel.state.alumnoSeleccionado.repetidor)
        imageAlumno.imageProperty().bind(viewModel.state.alumnoSeleccionado.imagen)

        // Para que no se vea desactivado mucho, que queda feo!!
        dateAlumnoFechaNacimiento.style = "-fx-opacity: 1"
        dateAlumnoFechaNacimiento.editor.style = "-fx-opacity: 1"
        checkAlumnoRepetidor.style = "-fx-opacity: 1"
    }

    private fun initEventos() {
        logger.debug { "Inicializando eventos" }

        // menús
        menuImportar.setOnAction {
            onImportarAction()
        }

        menuExportar.setOnAction {
            onExportarAction()
        }

        menuZip.setOnAction {
            onZipAction()
        }

        menuUnzip.setOnAction {
            onUnzipAction()
        }

        menuSalir.setOnAction {
            onOnCloseAction()
        }

        menuAcercaDe.setOnAction {
            onAcercaDeAction()
        }

        // Botones
        btnNuevo.setOnAction {
            onNuevoAction()
        }

        btnEditar.setOnAction {
            onEditarAction()
        }

        btnEliminar.setOnAction {
            onEliminarAction()
        }

        // Combo
        comboTipo.selectionModel.selectedItemProperty().addListener { _, _, newValue ->
            newValue?.let { onComboSelected(it) }
        }

        // Tabla
        tableAlumnos.selectionModel.selectedItemProperty().addListener { _, _, newValue ->
            newValue?.let { onTablaSelected(it) }
        }

        // Buscador
        // Evento del buscador key press
        // Funciona con el intro
        // textBuscador.setOnAction {
        // con cualquer letra al levantarse, ya ha cambiado el valor
        textBuscador.setOnKeyReleased { newValue ->
            newValue?.let { onKeyReleasedAction() }
        }
    }

    private fun onKeyReleasedAction() {
        logger.debug { "onKeyReleasedAction" }
        filterDataTable()
    }

    private fun filterDataTable() {
        logger.debug { "filterDataTable" }
        // filtramos por el tipo seleccionado en la tabla
        tableAlumnos.items = viewModel.alumnosFilteredList(comboTipo.value, textBuscador.text.trim())
    }

    private fun onTablaSelected(newValue: Alumno) {
        logger.debug { "onTablaSelected: $newValue" }
        viewModel.updateAlumnoSeleccionado(newValue)
    }

    private fun onComboSelected(newValue: String) {
        logger.debug { "onComboSelected: $newValue" }
        filterDataTable()
    }

    fun onEliminarAction() {
        logger.debug { "onEliminarAction" }
        // Comprbar que se ha seleccionado antes!!
        if (tableAlumnos.selectionModel.selectedItem == null) {
            return
        }
        val alert = Alert(AlertType.CONFIRMATION)
        alert.title = "¿Eliminar Alumno/?"
        alert.contentText =
            "¿Desea eliminar este alumno?\nEsta acción no se puede deshacer y se eliminarán todos los datos asociados al alumno."
        val result = alert.showAndWait()
        if (result.get() == ButtonType.OK) {
            viewModel.eliminarAlumno().onSuccess {
                logger.debug { "Alumno/a eliminado correctamente" }
                showAlertOperacion(
                    alerta = AlertType.INFORMATION,
                    "Alumno/a eliminado/a",
                    "Se ha eliminado el/la alumno/a correctamente del sistema"
                )
                tableAlumnos.selectionModel.clearSelection()
            }.onFailure {
                logger.error { "Error al eliminar el alumno: ${it.message}" }
                showAlertOperacion(alerta = AlertType.ERROR, "Error al eliminar el/la alumno/a", it.message)
            }
        } else {
            alert.close()
        }

    }

    private fun onEditarAction() {
        logger.debug { "onEditarAction" }
        if (tableAlumnos.selectionModel.selectedItem == null) {
            return
        }
        viewModel.state.tipoOperacion = TipoOperacion.EDITAR
        RoutesManager.initDetalle()

    }

    private fun onNuevoAction() {
        logger.debug { "onNuevoAction" }
        // Poner el modo nuevo antes!!
        viewModel.state.tipoOperacion = TipoOperacion.NUEVO
        RoutesManager.initDetalle()
    }

    private fun onAcercaDeAction() {
        logger.debug { "onAcercaDeAction" }
        RoutesManager.initAcercaDeStage()
    }

    private fun onExportarAction() {
        logger.debug { "onExportarAction" }
        // Forma larga, muy Java
        //val fileChooser = FileChooser()
        //fileChooser.title = "Exportar expedientes"
        //fileChooser.extensionFilters.add(FileChooser.ExtensionFilter("JSON", "*.json"))
        //val file = fileChooser.showSaveDialog(RoutesManager.activeStage)

        // Forma Kotlin con run y let (scope functions)
        FileChooser().run {
            title = "Exportar expedientes"
            extensionFilters.add(FileChooser.ExtensionFilter("JSON", "*.json"))
            showSaveDialog(RoutesManager.activeStage)
        }?.let {
            logger.debug { "onSaveAction: $it" }
            RoutesManager.activeStage.scene.cursor = WAIT
            viewModel.saveAlumnadoToJson(it)
                .onSuccess {
                    showAlertOperacion(
                        title = "Datos exportados",
                        mensaje = "Se ha exportado tus Expedientes.\nAlumnos exportados: ${viewModel.state.alumnos.size}"
                    )
                }.onFailure { error ->
                    showAlertOperacion(alerta = AlertType.ERROR, title = "Error al exportar", mensaje = error.message)
                }
            RoutesManager.activeStage.scene.cursor = DEFAULT
        }
    }

    private fun onImportarAction() {
        logger.debug { "onImportarAction" }
        // Forma larga, muy Java
        //val fileChooser = FileChooser()
        //fileChooser.title = "Importar expedientes"
        //fileChooser.extensionFilters.add(FileChooser.ExtensionFilter("JSON", "*.json"))
        //val file = fileChooser.showOpenDialog(RoutesManager.activeStage)

        // Forma Kotlin con run y let (scope functions)
        FileChooser().run {
            title = "Importar expedientes"
            extensionFilters.add(FileChooser.ExtensionFilter("JSON", "*.json"))
            showOpenDialog(RoutesManager.activeStage)
        }?.let {
            logger.debug { "onAbrirAction: $it" }
            showAlertOperacion(
                AlertType.INFORMATION,
                "Importando datos",
                "Importando datos Se sustituye la imagen por una imagen por defecto."
            )
            // Cambiar el cursor a espera
            RoutesManager.activeStage.scene.cursor = WAIT
            viewModel.loadAlumnadoFromJson(it)
                .onSuccess {
                    showAlertOperacion(
                        title = "Datos importados",
                        mensaje = "Se ha importado tus Expedientes.\nAlumnos importados: ${viewModel.state.alumnos.size}"
                    )
                }.onFailure { error ->
                    showAlertOperacion(alerta = AlertType.ERROR, title = "Error al importar", mensaje = error.message)
                }
            RoutesManager.activeStage.scene.cursor = DEFAULT
        }
    }

    // Método para salir de la aplicación
    fun onOnCloseAction() {
        logger.debug { "onOnCloseAction" }

        val alert = Alert(AlertType.CONFIRMATION)
        alert.title = "Salir"
        alert.contentText = "¿Desea salir de Expedientes DAM?"
        val result = alert.showAndWait()
        if (result.get() == ButtonType.OK) {
            Platform.exit() // O System.exit(0)
        } else {
            alert.close()
        }
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

    private fun onUnzipAction() {
        logger.debug { "onUnzipAction" }
        FileChooser().run {
            title = "Importar desde Zip"
            extensionFilters.add(FileChooser.ExtensionFilter("ZIP", "*.zip"))
            showOpenDialog(RoutesManager.activeStage)
        }?.let {
            logger.debug { "onAbrirAction: $it" }
            showAlertOperacion(
                AlertType.INFORMATION,
                "Importando datos",
                "Importando datos. Se sustituye la imagen por la imagen encontrada en el zip."
            )
            // Cambiar el cursor a espera
            RoutesManager.activeStage.scene.cursor = WAIT
            viewModel.loadAlumnadoFromZip(it)
                .onSuccess {
                    showAlertOperacion(
                        title = "Datos importados",
                        mensaje = "Se ha importado tus Expedientes.\nAlumnos importados: ${viewModel.state.alumnos.size}"
                    )
                }.onFailure { error ->
                    showAlertOperacion(alerta = AlertType.ERROR, title = "Error al importar", mensaje = error.message)
                }
            RoutesManager.activeStage.scene.cursor = DEFAULT
        }

    }

    private fun onZipAction() {
        logger.debug { "onZipAction" }
        FileChooser().run {
            title = "Exportar a Zip"
            extensionFilters.add(FileChooser.ExtensionFilter("ZIP", "*.zip"))
            showSaveDialog(RoutesManager.activeStage)
        }?.let {
            logger.debug { "onAbrirAction: $it" }
            // Cambiar el cursor a espera
            RoutesManager.activeStage.scene.cursor = WAIT
            viewModel.exportToZip(it)
                .onSuccess {
                    showAlertOperacion(
                        title = "Datos exportados",
                        mensaje = "Se ha exportado tus Expedientes completos con imágenes.\nAlumnos exportados: ${viewModel.state.alumnos.size}"
                    )
                }.onFailure { error ->
                    showAlertOperacion(alerta = AlertType.ERROR, title = "Error al exportar", mensaje = error.message)
                }
            RoutesManager.activeStage.scene.cursor = DEFAULT
        }
    }

}
