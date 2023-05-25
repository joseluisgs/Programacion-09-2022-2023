package dev.joseluisgs.pokedexfx.services


import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import dev.joseluisgs.pokedexfx.dto.json.PokedexJsonDto
import dev.joseluisgs.pokedexfx.errors.PokedexErrors
import dev.joseluisgs.pokedexfx.mappers.toPokemon
import dev.joseluisgs.pokedexfx.models.Pokemon
import mu.KotlinLogging
import java.io.InputStream


private val logger = KotlinLogging.logger {}

object PokedexStorageJson {

    fun loadPokedex(jsonFileStream: InputStream): Result<List<Pokemon>, PokedexErrors> {
        logger.debug { "Cargando Pokedex desde JSON: $jsonFileStream" }

        return try {
            val gson = GsonBuilder().create()
            // El tipo de dato que vamos a leer
            val importType = object : TypeToken<PokedexJsonDto>() {}.type
            // Cargamos un reader con el contenido del fichero
            val jsonReader = JsonReader(jsonFileStream.reader())
            // otra forma de leer el fichero
            //val json = jsonFileStream.bufferedReader().use { it.readText() }
            //logger.debug { "Contenido del fichero" }
            //println(json)
            // Leemos el JSON y lo convertimos a un objeto
            //val pokedex = gson.fromJson<PokedexJsonDto>(json, importType)
            val pokedex = gson.fromJson<PokedexJsonDto>(jsonReader, importType)
            Ok(pokedex.pokemonJsonDto.map { it.toPokemon() })
        } catch (e: Exception) {
            println(e.stackTraceToString())
            logger.error { "Error al cargar la Pokedex desde JSON: $jsonFileStream" }
            Err(PokedexErrors.LoadJsonError("Error al cargar la Pokedex desde JSON o fichero no es un Json valido\n ${e.message}"))
        }
    }
}