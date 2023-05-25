package dev.joseluisgs.moscafx

import dev.joseluisgs.moscafx.di.AppDIModule
import dev.joseluisgs.moscafx.routes.RoutesManager
import javafx.application.Application
import javafx.stage.Stage
import mu.KotlinLogging
import org.koin.core.component.KoinComponent
import org.koin.core.context.startKoin
import org.koin.logger.slf4jLogger

private val logger = KotlinLogging.logger {}

class MoscaApplication : Application(), KoinComponent {
    override fun start(stage: Stage) {
        logger.info { "Iniciando PokedexFX App" }

        // Cargo el contexto de Koin de DI
        startKoin {
            slf4jLogger() // Logger de Koin para ver lo que hace opcional
            modules(AppDIModule)
        }


        // Le pasamos la aplicación a la clase RoutesManager
        RoutesManager.apply {
            app = this@MoscaApplication
        }
        // Inicializamos la escena principal
        RoutesManager.initMainStage(stage)
    }
}

fun main() {

    Application.launch(MoscaApplication::class.java)
}