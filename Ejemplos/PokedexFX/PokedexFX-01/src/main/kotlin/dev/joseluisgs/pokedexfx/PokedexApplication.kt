package dev.joseluisgs.pokedexfx

import dev.joseluisgs.pokedexfx.routes.RoutesManager
import javafx.application.Application
import javafx.stage.Stage
import mu.KotlinLogging

private val logger = KotlinLogging.logger {}

class PokedexApplication : Application() {
    override fun start(stage: Stage) {
        logger.info { "Iniciando PokedexFX App" }

        // Le pasamos la aplicación a la clase RoutesManager
        RoutesManager.apply {
            app = this@PokedexApplication
        }
        // Inicializamos la escena principal
        RoutesManager.initMainStage(stage)
    }
}

fun main() {
    Application.launch(PokedexApplication::class.java)
}