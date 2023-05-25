package dev.joseluisgs.pokedexfx.dto.json


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PokemonJsonDto(
    @Json(name = "avg_spawns")
    val avgSpawns: Double,
    @Json(name = "candy")
    val candy: String,
    @Json(name = "candy_count")
    val candyCount: Int?,
    @Json(name = "egg")
    val egg: String,
    @Json(name = "height")
    val height: String,
    @Json(name = "id")
    val id: Int,
    @Json(name = "img")
    val img: String,
    @Json(name = "multipliers")
    val multipliers: List<Double>?,
    @Json(name = "name")
    val name: String,
    @Json(name = "next_evolution")
    val nextEvolutionJsonDto: List<NextEvolutionJsonDto>?,
    @Json(name = "num")
    val num: String,
    @Json(name = "prev_evolution")
    val prevEvolutionJsonDto: List<PrevEvolutionJsonDto>?,
    @Json(name = "spawn_chance")
    val spawnChance: Double,
    @Json(name = "spawn_time")
    val spawnTime: String,
    @Json(name = "type")
    val type: List<String>,
    @Json(name = "weaknesses")
    val weaknesses: List<String>,
    @Json(name = "weight")
    val weight: String
)