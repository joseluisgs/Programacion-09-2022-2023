package dev.joseluisgs.pokedexfx.services


import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.squareup.moshi.Moshi
import dev.joseluisgs.pokedexfx.dto.json.PokedexJsonDto
import dev.joseluisgs.pokedexfx.errors.PokedexErrors
import dev.joseluisgs.pokedexfx.mappers.toPokemon
import dev.joseluisgs.pokedexfx.models.Pokemon
import mu.KotlinLogging
import java.io.InputStream


private val logger = KotlinLogging.logger {}

class PokedexStorageJson {

    fun loadPokedex(jsonFileStream: InputStream): Result<List<Pokemon>, PokedexErrors> {
        logger.debug { "Cargando Pokedex desde JSON: $jsonFileStream" }

        return try {
            val moshi = Moshi.Builder().build()

            val adapter = moshi.adapter(PokedexJsonDto::class.java)
            val pokedex = adapter.fromJson(jsonFileStream.reader().buffered().readText())!!.pokemonJsonDto
            return Ok(pokedex.map { it.toPokemon() })


        } catch (e: Exception) {
            println(e.stackTraceToString())
            logger.error { "Error al cargar la Pokedex desde JSON: $jsonFileStream" }
            Err(PokedexErrors.LoadJsonError("Error al cargar la Pokedex desde JSON o fichero no es un Json valido\n ${e.message}"))
        }
    }
}