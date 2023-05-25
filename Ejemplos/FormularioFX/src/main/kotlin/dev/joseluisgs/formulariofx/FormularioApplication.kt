package dev.joseluisgs.formulariofx

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.image.Image
import javafx.stage.Stage

class HelloApplication : Application() {
    // Sobre escribimos el método start
    // Este método es obligatorio y lanza la aplicación: stage inicial
    override fun start(stage: Stage) {
        val fxmlLoader = FXMLLoader(
            // Ojo al path siempre relativo a la carpeta resources desde donde se ejecuta
            // Cuidad con los paquetes
            HelloApplication::class.java.getResource("views/formulario-view.fxml")
        )

        val scene = Scene(fxmlLoader.load(), 500.0, 240.0)
        stage.title = "Registro"
        stage.scene = scene
        // Le ponemos un icono
        stage.icons.add(
            Image(
                HelloApplication::class.java.getResourceAsStream("icons/java.png")
            )
        )
        stage.show()
    }

    // Opcionalmente podemos sobreescribir el método stop e init, si queremos hacer algo antes o después
    override fun stop() {
        println("Stop")
    }

    override fun init() {
        println("Init")
    }
}

fun main() {
    // Lanzamos la aplicación
    // Usamos el método launch de la clase Application, que llama al método start
    Application.launch(HelloApplication::class.java)
}