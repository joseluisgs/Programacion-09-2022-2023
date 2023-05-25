// Definición de la clase Button con lambda
class MyButtonLambda {
    private var onClickListener: ((MyButtonLambda) -> Unit)? = null

    // Función para asignar un listener al botón
    fun setOnClickListener(listener: (MyButtonLambda) -> Unit) {
        this.onClickListener = listener
    }

    // Función para simular el click en el botón
    fun click() {
        onClickListener?.invoke(this)
    }
}

fun main() {
    // Creamos un botón
    val button = MyButtonLambda()

    // Asignamos un listener al botón
    button.setOnClickListener {
        println("Button clicked!")
    }

    // Simulamos el click en el botón
    button.click()
}