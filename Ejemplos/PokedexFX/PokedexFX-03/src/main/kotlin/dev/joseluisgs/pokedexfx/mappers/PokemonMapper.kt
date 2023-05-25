package dev.joseluisgs.pokedexfx.mappers

import dev.joseluisgs.pokedexfx.dto.json.PokemonJsonDto
import dev.joseluisgs.pokedexfx.models.Pokemon

fun PokemonJsonDto.toPokemon(): Pokemon {
    return Pokemon(
        id = this.id,
        num = this.num,
        name = this.name,
        height = this.height.removeSuffix("m").toDouble(),
        weight = this.weight.removeSuffix("kg").toDouble(),
        img = this.img,
        type = this.type,
        nextEvolution = this.nextEvolutionJsonDto?.map { it.name } ?: emptyList(),
        weaknesses = this.weaknesses
    )
}