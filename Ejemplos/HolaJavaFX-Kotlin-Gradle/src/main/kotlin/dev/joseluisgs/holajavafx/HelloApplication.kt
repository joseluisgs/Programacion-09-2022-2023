package dev.joseluisgs.holajavafx

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.stage.Stage

class HelloApplication : Application() {
    override fun start(stage: Stage) {
        val fxmlLoader = FXMLLoader(HelloApplication::class.java.getResource("hello-view.fxml"))
        val scene = Scene(fxmlLoader.load(), 320.0, 240.0)
        stage.title = "Hello!"
        stage.scene = scene
        stage.show()
    }
}

// No pulses el play, si no
// desde el terminal ./gradlew run.
// Desde Gradle -> Tasks -> application -> run
fun main() {
    Application.launch(HelloApplication::class.java)
}