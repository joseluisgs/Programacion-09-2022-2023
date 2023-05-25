import java.awt.Button

fun main() {
    // Creamos un botón
    val button = Button()

    // Asignamos un listener al botón
    button.addActionListener {
        println("Button clicked!")
    }
}