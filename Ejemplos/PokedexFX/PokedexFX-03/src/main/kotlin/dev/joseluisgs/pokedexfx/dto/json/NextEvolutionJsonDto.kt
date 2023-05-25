package dev.joseluisgs.pokedexfx.dto.json


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NextEvolutionJsonDto(
    @Json(name = "name")
    val name: String,
    @Json(name = "num")
    val num: String
)