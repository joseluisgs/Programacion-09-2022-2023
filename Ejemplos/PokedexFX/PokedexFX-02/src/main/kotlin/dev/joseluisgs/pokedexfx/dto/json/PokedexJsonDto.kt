package dev.joseluisgs.pokedexfx.dto.json


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PokedexJsonDto(
    @Json(name = "pokemon")
    val pokemonJsonDto: List<PokemonJsonDto>
)