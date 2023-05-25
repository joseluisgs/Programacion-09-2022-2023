package dev.joseluisgs.pokedexfx.di

import dev.joseluisgs.pokedexfx.services.PokedexStorageJson
import dev.joseluisgs.pokedexfx.viewmodel.PokedexViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val AppDIModule = module {
    // Lo voy a definir todo como Singleton
    // https://insert-koin.io/docs/reference/koin-core/dsl
    singleOf(::PokedexStorageJson) // A
    singleOf(::PokedexViewModel) // B (A) --> Lo hace automáticamente
}
