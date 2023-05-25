package dev.joseluisgs.holajavafxdam

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.stage.Stage

class DamFxApplication : Application() {
    override fun start(stage: Stage) {
        val fxmlLoader = FXMLLoader(DamFxApplication::class.java.getResource("views/saludo-view.fxml"))
        val scene = Scene(fxmlLoader.load(), 360.0, 220.0)
        stage.title = "Hola 1º DAM"
        stage.scene = scene
        stage.show()
    }
}

fun main() {
    Application.launch(DamFxApplication::class.java)
}