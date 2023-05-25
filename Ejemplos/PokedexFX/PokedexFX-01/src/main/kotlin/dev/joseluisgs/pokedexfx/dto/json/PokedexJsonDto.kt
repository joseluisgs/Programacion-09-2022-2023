package dev.joseluisgs.pokedexfx.dto.json


import com.google.gson.annotations.SerializedName

data class PokedexJsonDto(
    @SerializedName("pokemon")
    val pokemonJsonDto: List<PokemonJsonDto>
)