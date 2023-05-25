package dev.joseluisgs.pokedexfx.errors

sealed class PokedexErrors(val mensaje: String) {
    class LoadJsonError(mensaje: String) : PokedexErrors(mensaje)
}
