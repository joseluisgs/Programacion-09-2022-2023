class MyButtonDirecto {
    private var onClickListener: ((MyButtonDirecto) -> Unit)? = null

    fun setOnClickListener(listener: (MyButtonDirecto) -> Unit) {
        this.onClickListener = listener
        this.onClickListener?.invoke(this)
    }
}

fun main() {
    // Creamos un botón
    val button = MyButtonDirecto()

    // Asignamos un listener al botón
    button.setOnClickListener {
        println("Button clicked!")
    }
}
