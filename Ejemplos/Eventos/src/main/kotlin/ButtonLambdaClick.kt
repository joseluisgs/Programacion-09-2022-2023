// Definición de la interfaz del evento
interface ButtonClickListener {
    fun onButtonClicked()
}

// Clase que representa un botón
class MyButtonInterface {
    private var listener: ButtonClickListener? = null

    // Función para asignar un listener al botón
    fun setOnClickListener(listener: ButtonClickListener) {
        this.listener = listener
    }

    // Función para simular el click en el botón
    fun click() {
        listener?.onButtonClicked()
    }
}


fun main() {
    // Creamos un botón
    val button = MyButtonInterface()

    // Asignamos un listener al botón
    button.setOnClickListener(object : ButtonClickListener {
        override fun onButtonClicked() {
            println("Button clicked!")
        }
    })

    // Simulamos el click en el botón
    button.click()
}
