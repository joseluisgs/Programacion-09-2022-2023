package dev.joseluisgs.routesmanagerfx

import dev.joseluisgs.routesmanagerfx.routes.RoutesManager
import javafx.application.Application
import javafx.stage.Stage

class HelloApplication : Application() {
    override fun start(stage: Stage) {
        // Le pasamos la aplicación a la clase RoutesManager
        RoutesManager.apply {
            app = this@HelloApplication
        }
        // Inicializamos la scena principal
        RoutesManager.initMainStage(stage)
    }
}

fun main() {
    Application.launch(HelloApplication::class.java)
}