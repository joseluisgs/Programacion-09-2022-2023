package dev.joseluisgs.pokedexfx.viewmodel

import com.github.michaelbull.result.Result
import com.github.michaelbull.result.onSuccess
import dev.joseluisgs.pokedexfx.errors.PokedexErrors
import dev.joseluisgs.pokedexfx.models.Pokemon
import dev.joseluisgs.pokedexfx.routes.RoutesManager
import dev.joseluisgs.pokedexfx.services.PokedexStorageJson
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import mu.KotlinLogging
import java.io.File

private val logger = KotlinLogging.logger {}

class PokedexViewModel {

    val state = PokedexState()

    init {
        logger.info { "PokedexViewModel cargando datos" }
        // Cargamos los datos
        PokedexStorageJson.loadPokedex(RoutesManager.getResourceAsStream("data/pokedex.json"))
            .onSuccess {
                logger.info { "PokedexViewModel datos cargados" }
                // Si todo va bien, los cargamos en la lista
                initObservableList(it) // Inicializamos los observables
                logger.debug { "PokedexViewModel datos cargados pokedex: ${it.size}" }
            }
    }

    fun loadPokedexFromJson(fichero: File): Result<List<Pokemon>, PokedexErrors> {
        println("Cargando pokedex desde $fichero")
        return PokedexStorageJson.loadPokedex(fichero.inputStream())
            .onSuccess {
                initObservableList(it) // Inicializamos los observables
                logger.debug { "PokedexViewModel datos cargados pokedex: ${it.size}" }
            }
    }

    fun initObservableList(pokemons: List<Pokemon>) {
        // Inicializamos la lista de todos. Esto simplemente hago apply para no escribir distintas
        // veces el nombre de la variable
        state.filteredPokedex.apply {
            clear()
            addAll(pokemons)
        }

        // Inicializamos los tipos de pokemon
        state.typesPokemon.apply {
            clear()
            addAll(listOf("") + pokemons.map { it.type }.flatten().distinct())
        }
    }

    data class PokedexState(
        val typesPokemon: ObservableList<String> = FXCollections.observableArrayList(),
        val filteredPokedex: ObservableList<Pokemon> = FXCollections.observableArrayList()
    )
}