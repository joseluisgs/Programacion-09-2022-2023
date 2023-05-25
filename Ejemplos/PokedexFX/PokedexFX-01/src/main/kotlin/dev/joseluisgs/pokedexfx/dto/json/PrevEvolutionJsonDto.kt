package dev.joseluisgs.pokedexfx.dto.json


import com.google.gson.annotations.SerializedName

data class PrevEvolutionJsonDto(
    @SerializedName("name")
    val name: String,
    @SerializedName("num")
    val num: String
)