package dev.joseluisgs.pokedexfx.viewmodel

import com.github.michaelbull.result.Result
import com.github.michaelbull.result.onSuccess
import dev.joseluisgs.pokedexfx.errors.PokedexErrors
import dev.joseluisgs.pokedexfx.locale.toLocalNumber
import dev.joseluisgs.pokedexfx.models.Pokemon
import dev.joseluisgs.pokedexfx.routes.RoutesManager
import dev.joseluisgs.pokedexfx.services.PokedexStorageJson
import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.collections.transformation.FilteredList
import javafx.scene.image.Image
import mu.KotlinLogging
import java.io.File

private val logger = KotlinLogging.logger {}


class PokedexViewModel(
    private val pokedexStorageJson: PokedexStorageJson
) {

    // Creo el estado con la imagen inicial
    val state = PokemonState()

    init {
        logger.info { "PokedexViewModel cargando datos" }
        // Cargamos los datos
        pokedexStorageJson.loadPokedex(RoutesManager.getResourceAsStream("data/pokedex.json"))
            .onSuccess {
                logger.info { "PokedexViewModel datos cargados" }
                // Si todo va bien, los cargamos en la lista
                initObservableList(it) // Inicializamos los observables
                logger.debug { "PokedexViewModel datos cargados pokedex: ${state.pokedex.size}" }
            }
    }

    fun loadPokedexFromJson(fichero: File): Result<List<Pokemon>, PokedexErrors> {
        println("Cargando pokedex desde $fichero")
        return pokedexStorageJson.loadPokedex(fichero.inputStream())
            .onSuccess {
                initObservableList(it) // Inicializamos los observables
                logger.debug { "PokedexViewModel datos cargados pokedex: ${state.pokedex.size}" }
            }
    }

    private fun initObservableList(pokemons: List<Pokemon>) {
        // Inicializamos la lista de todos. Esto simplemente hago apply para no escribir distintas
        // veces el nombre de la variable
        state.pokedex.apply {
            clear()
            addAll(pokemons)
        }

        // Inicializamos los tipos de pokemon
        state.typesPokemon.apply {
            clear()
            // esto es para añadir un elemento vacio al principio
            addAll(listOf("") + pokemons.map { it.type }.flatten().distinct().sorted())
        }
    }

    fun updateState(pokemon: Pokemon) {
        logger.debug { "Actualizando estado de Pokemon: $pokemon" }

        state.apply {
            num.value = pokemon.num
            name.value = pokemon.name
            height.value = pokemon.height.toLocalNumber() + " m"
            weight.value = pokemon.weight.toLocalNumber() + " kg"
            nextEvolution.value = pokemon.nextEvolution.joinToString { it }
            weaknesses.value = pokemon.weaknesses.joinToString { it }
            img.value = try {
                logger.debug { "Cargando imagen: ${pokemon.img}" }
                Image(pokemon.img.replace("http", "https"))
            } catch (e: Exception) {
                logger.error { "Error al cargar la imagen: ${e.message}, usando imagen por defecto" }
                Image(RoutesManager.getResourceAsStream("images/sin-image.png"))
            }
        }
    }

    fun pokemonFilteredList(tipo: String, texto: String): FilteredList<Pokemon> {
        logger.debug { "Filtrando lista de Pokemon: $tipo, $texto" }

        return state.pokedex
            .filtered { pokemon ->
                if (tipo == "") true else pokemon.type.contains(tipo)
            }.filtered { pokemon ->
                pokemon.name.contains(texto, true)
            }

    }

    // Clase que representa el estado de la vista
    data class PokemonState(
        val num: SimpleStringProperty = SimpleStringProperty(),
        val name: SimpleStringProperty = SimpleStringProperty(),
        val height: SimpleStringProperty = SimpleStringProperty(),
        val weight: SimpleStringProperty = SimpleStringProperty(),
        val nextEvolution: SimpleStringProperty = SimpleStringProperty(),
        val weaknesses: SimpleStringProperty = SimpleStringProperty(),
        val img: ObjectProperty<Image> = SimpleObjectProperty(Image(RoutesManager.getResourceAsStream("images/sin-image.png"))),
        val typesPokemon: ObservableList<String> = FXCollections.observableArrayList(),
        val pokedex: ObservableList<Pokemon> = FXCollections.observableArrayList()
    )
}
