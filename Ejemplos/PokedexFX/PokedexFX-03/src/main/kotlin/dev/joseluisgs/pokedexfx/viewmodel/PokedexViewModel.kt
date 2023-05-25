package dev.joseluisgs.pokedexfx.viewmodel

import com.github.michaelbull.result.Result
import com.github.michaelbull.result.onSuccess
import dev.joseluisgs.pokedexfx.errors.PokedexErrors
import dev.joseluisgs.pokedexfx.locale.toLocalNumber
import dev.joseluisgs.pokedexfx.models.Pokemon
import dev.joseluisgs.pokedexfx.routes.RoutesManager
import dev.joseluisgs.pokedexfx.services.PokedexStorageJson
import javafx.beans.property.SimpleObjectProperty
import javafx.scene.image.Image
import mu.KotlinLogging
import java.io.File

private val logger = KotlinLogging.logger {}


class PokedexViewModel(
    private val pokedexStorageJson: PokedexStorageJson
) {

    // Creo el estado con la imagen inicial
    val state: SimpleObjectProperty<PokemonState> = SimpleObjectProperty(PokemonState())

    init {
        logger.info { "PokedexViewModel cargando datos" }
        // Cargamos los datos
        pokedexStorageJson.loadPokedex(RoutesManager.getResourceAsStream("data/pokedex.json"))
            .onSuccess {
                logger.info { "PokedexViewModel datos cargados" }
                // Si todo va bien, los cargamos en la lista
                initState(it) // Inicializamos los observables
                logger.debug { "PokedexViewModel datos cargados pokedex: ${state.value.pokemons.size}" }
            }
    }

    fun loadPokedexFromJson(fichero: File): Result<List<Pokemon>, PokedexErrors> {
        println("Cargando pokedex desde $fichero")
        return pokedexStorageJson.loadPokedex(fichero.inputStream())
            .onSuccess {
                initState(it) // Inicializamos los observables
                logger.debug { "PokedexViewModel datos cargados pokedex: ${state.value.pokemons.size}" }
            }
    }

    private fun initState(pokemonsList: List<Pokemon>) {
        // Inicializamos la lista de todos. Esto simplemente hago apply para no escribir distintas
        state.value = state.value.copy(
            pokemons = pokemonsList,
            types = listOf("") + pokemonsList.map { it.type }.flatten().distinct().sorted()
        )
    }

    fun updateState(pokemon: Pokemon) {
        logger.debug { "Actualizando estado de Pokemon: $pokemon" }

        state.value = state.value.copy(
            num = pokemon.num,
            name = pokemon.name,
            height = pokemon.height.toLocalNumber() + " m",
            weight = pokemon.weight.toLocalNumber() + " kg",
            nextEvolution = pokemon.nextEvolution.joinToString { it },
            weaknesses = pokemon.weaknesses.joinToString { it },
            img = try {
                logger.debug { "Cargando imagen: ${pokemon.img}" }
                Image(pokemon.img.replace("http", "https"))
            } catch (e: Exception) {
                logger.error { "Error al cargar la imagen: ${e.message}, usando imagen por defecto" }
                Image(RoutesManager.getResourceAsStream("images/sin-image.png"))
            }
        )

    }

    fun pokemonFilteredList(tipo: String, texto: String): List<Pokemon> {
        logger.debug { "Filtrando lista de Pokemon: $tipo, $texto" }

        return state.value.pokemons
            .filter { pokemon ->
                if (tipo == "") true else pokemon.type.contains(tipo)
            }.filter { pokemon ->
                pokemon.name.contains(texto, true)
            }

    }

    // Clase que representa el estado de la vista
    data class PokemonState(
        val num: String = "",
        val name: String = "",
        val height: String = "",
        val weight: String = "",
        val nextEvolution: String = "",
        val weaknesses: String = "",
        val img: Image = Image(RoutesManager.getResourceAsStream("images/sin-image.png")),
        val types: List<String> = listOf(),
        val pokemons: List<Pokemon> = listOf(),
    )
}
