package dev.joseluisgs.pokedexfx.models

data class Pokemon(
    val id: Int,
    val num: String,
    val name: String,
    val height: Double,
    val weight: Double,
    val img: String,
    val type: List<String>,
    val nextEvolution: List<String>,
    val weaknesses: List<String>
)