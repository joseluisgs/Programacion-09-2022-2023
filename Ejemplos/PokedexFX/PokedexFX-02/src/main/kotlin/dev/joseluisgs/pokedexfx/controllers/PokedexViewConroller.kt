package dev.joseluisgs.pokedexfx.controllers

import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import dev.joseluisgs.pokedexfx.models.Pokemon
import dev.joseluisgs.pokedexfx.routes.RoutesManager
import dev.joseluisgs.pokedexfx.viewmodel.PokedexViewModel
import javafx.application.Platform
import javafx.fxml.FXML
import javafx.scene.control.*
import javafx.scene.control.Alert.AlertType
import javafx.scene.control.cell.PropertyValueFactory
import javafx.scene.image.ImageView
import javafx.stage.FileChooser
import mu.KotlinLogging
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


private val logger = KotlinLogging.logger {}

class PokedexViewConroller : KoinComponent {

    // Mi ViewModel, debemos inyectarlo bien :) o hacer el get() de Koin si no lo queremos lazy
    val viewModel: PokedexViewModel by inject()

    // Menus
    @FXML
    private lateinit var menuSalir: MenuItem

    @FXML
    private lateinit var menuAcercaDe: MenuItem

    @FXML
    private lateinit var menuAbrir: MenuItem

    // Tabla
    @FXML
    private lateinit var tablePokemon: TableView<Pokemon>

    @FXML
    private lateinit var tableColumnNumero: TableColumn<Pokemon, String>

    @FXML
    private lateinit var tableColumNombre: TableColumn<Pokemon, String>

    // Combo y buscador y slider
    @FXML
    private lateinit var comboTipo: ComboBox<String>

    @FXML
    private lateinit var textBuscador: TextField

    // Formualrio de detalles
    @FXML
    private lateinit var imgPokemon: ImageView

    @FXML
    private lateinit var sliderPokemons: Slider

    @FXML
    private lateinit var textPokemonNumero: TextField

    @FXML
    private lateinit var textPokemonEvoluciones: TextArea

    @FXML
    private lateinit var textPokemonDebilidades: TextArea

    @FXML
    private lateinit var textPokemonPeso: TextField

    @FXML
    private lateinit var textPokemonAltura: TextField

    @FXML
    private lateinit var textPokemonNombre: TextField

    // Metodo para inicializar
    @FXML
    fun initialize() {
        logger.info { "Inicializando AcercaDeViewController FXML" }

        // Tabla
        tablePokemon.items = viewModel.state.pokedex
        // columnas, con el nombre de la propiedad del objeto hará binding
        tableColumnNumero.cellValueFactory = PropertyValueFactory("num")
        tableColumNombre.cellValueFactory = PropertyValueFactory("name")

        // comboBoxes
        comboTipo.items = viewModel.state.typesPokemon
        comboTipo.value = "" // Selecciona ese valor


        // Eventos
        // Botones
        menuSalir.setOnAction { onOnCloseAction() }
        menuAcercaDe.setOnAction { onAcercaDeAction() }
        menuAbrir.setOnAction { onAbrirAction() }
        // Tabla
        tablePokemon.selectionModel.selectedItemProperty().addListener { _, _, newValue ->
            newValue?.let { onTableSelected(it) }
        }
        // Evento del combo
        comboTipo.selectionModel.selectedItemProperty().addListener { _, _, newValue ->
            newValue?.let { onComboSelected(it) }
        }
        // Evento del buscador key press
        // Funciona con el intro
        // textBuscador.setOnAction {
        // con cualquer letra al levantarse, ya ha cambiado el valor
        textBuscador.setOnKeyReleased { newValue ->
            newValue?.let { onTextAction() }
        }
        // Slider
        sliderPokemons.min = 0.0
        sliderPokemons.max = viewModel.state.pokedex.size.toDouble() - 1
        sliderPokemons.valueProperty().addListener { _, _, newValue ->
            newValue?.let { onSliderSelected(it.toInt()) }
        }

        // Reactividad de los campos, solo queremos una direccion
        textPokemonNumero.textProperty().bind(viewModel.state.num)
        textPokemonNombre.textProperty().bind(viewModel.state.name)
        textPokemonAltura.textProperty().bind(viewModel.state.height)
        textPokemonPeso.textProperty().bind(viewModel.state.weight)
        textPokemonEvoluciones.textProperty().bind(viewModel.state.nextEvolution)
        textPokemonDebilidades.textProperty().bind(viewModel.state.weaknesses)
        imgPokemon.imageProperty().bind(viewModel.state.img)


    }

    private fun onSliderSelected(newValue: Int) {
        // Ponemos el slider en la posición del pokemon seleccionado
        logger.debug { "onSliderSelected: $newValue" }
        tablePokemon.selectionModel.select(newValue)
        tablePokemon.scrollTo(newValue)

    }

    private fun onTextAction() {
        // podemos cambiarlo en el binding diretamente
        logger.debug { "onTextAction: ${textBuscador.text}" }
        filterDataTable()
    }

    private fun onComboSelected(newValue: String) {
        // filtramos por el tipo seleccionado en la tabla
        logger.debug { "onComboSelected: $newValue" }
        filterDataTable()
    }

    private fun filterDataTable() {
        logger.debug { "filterDataTable" }
        // filtramos por el tipo seleccionado en la tabla
        tablePokemon.items = viewModel.pokemonFilteredList(comboTipo.value, textBuscador.text.trim())

    }

    private fun onTableSelected(newValue: Pokemon) {
        logger.debug { "onTableSelected: $newValue" }
        viewModel.updateState(newValue)
    }

    private fun onAbrirAction() {
        // Abrimos el explorador de archivos
        val fileChooser = FileChooser()
        fileChooser.title = "Abrir Pokedex"
        fileChooser.extensionFilters.add(FileChooser.ExtensionFilter("JSON", "*.json"))
        val file = fileChooser.showOpenDialog(RoutesManager.activeStage)
        file?.let {
            logger.debug { "onAbrirAction: $it" }
            viewModel.loadPokedexFromJson(it)
                .onSuccess {
                    showAlertDatosCargados(mensaje = "Se ha importado tu Pokedex.\nPokemons cargados: ${viewModel.state.pokedex.size}")
                }.onFailure { error ->
                    showAlertDatosCargados(alerta = AlertType.ERROR, mensaje = error.mensaje)
                }
        }
    }

    private fun showAlertDatosCargados(alerta: AlertType = AlertType.CONFIRMATION, mensaje: String = "") {
        logger.debug { "showAlertDatosCargados: $mensaje" }
        when (alerta) {
            AlertType.ERROR -> {
                val alert = Alert(AlertType.ERROR)
                alert.title = "Error al cargar datos"
                alert.contentText = mensaje
                alert.showAndWait()
            }

            else -> {
                val alert = Alert(AlertType.CONFIRMATION)
                alert.title = "Carga de datos"
                alert.contentText = mensaje
                alert.showAndWait()
            }
        }
    }

    private fun onAcercaDeAction() {

        RoutesManager.initAcercaDeStage()
    }


    // Método para salir de la aplicación
    fun onOnCloseAction() {
        logger.debug { "onOnCloseAction" }

        Alert(AlertType.CONFIRMATION).apply {
            title = "Salir"
            contentText = "¿Desea salir de Pokedex?"
        }.showAndWait().ifPresent {
            if (it == ButtonType.OK) {
                Platform.exit() // O System.exit(0)
            }
        }
    }
}
