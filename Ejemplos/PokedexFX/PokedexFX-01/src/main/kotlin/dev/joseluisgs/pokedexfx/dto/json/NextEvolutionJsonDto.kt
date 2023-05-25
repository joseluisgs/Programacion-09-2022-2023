package dev.joseluisgs.pokedexfx.dto.json


import com.google.gson.annotations.SerializedName

data class NextEvolutionJsonDto(
    @SerializedName("name")
    val name: String,
    @SerializedName("num")
    val num: String
)