package dev.joseluisgs.starwarsfx.viewmodel

import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import dev.joseluisgs.starwarsfx.factories.DroideGenerator
import dev.joseluisgs.starwarsfx.models.Defensa
import dev.joseluisgs.starwarsfx.models.Droide
import dev.joseluisgs.starwarsfx.models.DroideW447
import dev.joseluisgs.starwarsfx.repositories.matrix.Matrix
import dev.joseluisgs.starwarsfx.repositories.pila.Pila
import javafx.beans.property.SimpleObjectProperty
import mu.KotlinLogging
import java.io.File


private val logger = KotlinLogging.logger {}


private const val efectoDisparoNormal = 25
private const val efectoDisparoCritical = 50

data class StarWarsError(val message: String)

sealed class StarWarsResultado {
    data class StarWarsFin(val message: String) : StarWarsResultado()
    object StarWarsLoading : StarWarsResultado()
}

// Moraleja, si vas a trabajar en tareas backgroud que se lanzan con un Hilo
// Si dentro de este hilo vas a tocar variables reactivas de la interfaz o algo que se debe
// Reflejar en la interfaz debes usar Platform.runLater

class StarWarsViewModel(
    private val droideGenerator: DroideGenerator
) {
    // Creo el estado con la imagen inicial
    val state: SimpleObjectProperty<StarWarsState> = SimpleObjectProperty(StarWarsState())

    private var enemigos = Pila<Droide>()
    private var espacio: Matrix<Droide> = Matrix(0, 0)

    init {
        logger.info { "StarWarsViewModel cargando datos" }
    }

    fun comenzarSimulacion(dimensionEspacio: Int, tiempo: Int, droidesTotal: Int) {
        logger.info { "Comenzando simulación" }
        state.value = StarWarsState().copy(
            dimensionEspacio = dimensionEspacio,
            tiempoTotal = tiempo,
            droidesTotal = droidesTotal,
            resultado = Ok(StarWarsResultado.StarWarsLoading)
        )

        printOperacion("Comenzando simulación")

        initDroidesEnemigos()
        colocarDroidesEnemigos()

        simulacion()

        estadoResultadoFinal()
    }

    private fun colocarDroidesEnemigos() {
        logger.debug { "Colocando droides enemigos" }

        printOperacion("Colocando droides enemigos")

        // creamos la matriz de droides con la dimensión del espacio
        espacio = Matrix(state.value.dimensionEspacio, state.value.dimensionEspacio)
        // La imprimimos
        logger.debug { "Espacio generado: \n$espacio" }
        // Colocamos los droides de manera aleatoria siempre y cuando no haya otro droide
        for (i in 0 until enemigos.size()) {
            var fil: Int
            var col: Int
            // si esta vivo
            if (enemigos[i].isAlive) {
                println("Droide ${enemigos[i]} vivo")
                do {
                    println("DColocando droide ${enemigos[i]}")
                    fil = (0 until state.value.dimensionEspacio).random()
                    col = (0 until state.value.dimensionEspacio).random()
                } while (espacio[fil, col] != null)
                espacio[fil, col] = enemigos[i]
            }
        }

        logger.debug { "Droides enemigos colocados: ${enemigos.size()}" }

        // imprimir la matriz
        printCuadrante()
    }

    private fun initDroidesEnemigos() {
        logger.debug { "Generando droides enemigos" }
        printOperacion("Generando droides enemigos")

        enemigos = Pila<Droide>()
        for (i in 1..state.value.droidesTotal) {
            enemigos.push(droideGenerator.randomDroide())
        }
        logger.debug { "Droides enemigos generados: ${enemigos.size()}" }
    }

    private fun matrixToArea(): String {
        val sb = StringBuilder()
        for (i in 0 until espacio.rows) {
            for (j in 0 until espacio.cols) {
                if (espacio[i, j] != null) {
                    sb.append("[ ").append(espacio[i, j]?.toArea() ?: " ").append(" ]")
                } else {
                    sb.append("[   ]")
                }
            }
            sb.append("\n")
        }
        return sb.toString()
    }

    // Este métdo va a estar en background, no olvides que si tocas algo de la interfaz
    // Debes usar Platform.runLater y ademas debes lanzar este método con un hilo
    fun simulacion() {
        logger.debug { "Simulando" }
        var tiempo = 0
        var salida = false

        setTiempo(tiempo)
        do {
            printOperacion("Tiempo: ${tiempo / 1000}s")
            setTiempo(tiempo / 1000)
            printOperacion("Escaneado y Apuntando")
            // Obtengo la posicion a disparar
            val fil = (0 until state.value.dimensionEspacio).random()
            val col = (0 until state.value.dimensionEspacio).random()
            printOperacion("Disparando en: [$fil][$col]")

            // Disparo
            actualizarDisparos()
            // Acierto si es != null
            if (espacio[fil, col] != null) {
                printOperacion("Has acertado en la posicion: [${fil + 1}] [${col + 1}]")

                var efecto: Int = getEfecto()

                printOperacion("La energia del droide es: ${espacio[fil, col]?.energia}\nEfecto de disparo: $efecto")

                val d = espacio[fil, col]!! // Sabemos que no es nulo!!!

                // Si el androide es del tipo SW447, tiene una defensa que lo delimita de los disparos
                if (d is DroideW447) {
                    printOperacion("El droide SW447 tiene una defensa que lo delimita de los disparos")
                    efecto -= d.defensa
                    efecto = if (efecto < 0) 0 else efecto

                    // Si implementa una interfaz de defensa
                } else if (d is Defensa) {
                    printOperacion("El droide implementa una interfaz de Escudo de Defensa")
                    efecto = (d as Defensa).defender(efecto)
                    efecto = if (efecto < 0) 0 else efecto
                }
                d.energia -= efecto
                printOperacion("Has acertado con un daño de: $efecto a: $d")
                actualizarAciertos()

                // Ha muerto
                if (!d.isAlive) {
                    printOperacion("El droide ha muerto  pues su energia es: ${d.energia}")
                    actualizarMuertos()
                } else {
                    printOperacion("El droide sigue vivo con una energia de: ${d.energia}")
                }

            } else {
                printOperacion("Has fallado en la posicion: [${fil + 1}] [${col + 1}]")
            }
            tiempo += 1000
            setTiempo(tiempo / 1000)
            // Pauso!! ---> Esto es lo que te va a dar problemas!!!!
            Thread.sleep(1000)
            // Cambian de posicion los droides
            if (tiempo % 3000 == 0) {
                colocarDroidesEnemigos() // Cambiamos de posicion los droides
            }
            // Imprimo el espacio
            printCuadrante()
            val finTiempo: Boolean = tiempo >= state.value.tiempoTotal * 1000
            val todosMuertos = state.value.droidesTotal == state.value.muertos
            salida = finTiempo || todosMuertos
        } while (!salida)
        printOperacion("Fin de la simulacion")
    }

    private fun actualizarMuertos() {
        state.value = state.value.copy(muertos = state.value.muertos + 1)
    }

    private fun actualizarAciertos() {
        state.value = state.value.copy(aciertos = state.value.aciertos + 1)
    }

    private fun actualizarDisparos() {
        state.value = state.value.copy(disparos = state.value.disparos + 1)
    }

    private fun getEfecto(): Int {
        val efecto = (Math.random() * 100).toInt()
        return if (efecto <= 15) {
            efectoDisparoCritical
        } else {
            efectoDisparoNormal
        }
    }

    private fun setTiempo(tiempo: Int) {
        state.value = state.value.copy(tiempoActual = tiempo)
    }

    private fun printOperacion(menssage: String) {
        // De nuevo Run Latter para que se actualice en el hilo IU
        state.value = state.value.copy(operacion = state.value.operacion + "\n$menssage")
        println(menssage)
    }

    private fun printCuadrante() {
        state.value = state.value.copy(cuadrante = matrixToArea())
        println(espacio)
    }

    private fun estadoResultadoFinal() {
        state.value = state.value.copy(resultado = Ok(StarWarsResultado.StarWarsFin("Simulación terminada")))
    }

    fun guardarInforme(file: File) {
        file.writeText(report())
    }

    private fun report(): String {
        val sb = StringBuilder()
        sb.append("Informe de la simulacion\n")
        sb.append("Tiempo total de la simulacion: ${state.value.tiempoTotal} s\n")
        sb.append("Droides enemigos totales: ${state.value.droidesTotal}\n")
        sb.append("Droides enemigos muertos: ${state.value.muertos}\n")
        sb.append("Droides enemigos vivos: ${state.value.droidesTotal - state.value.muertos}\n")
        sb.append("Disparos realizados: ${state.value.disparos}\n")
        sb.append("Aciertos: ${state.value.aciertos}\n")
        // Porcetaje de aciertos sobre disparos
        sb.append("Procentaje de aciertos: ${(state.value.aciertos * 100) / state.value.disparos} %\n")
        // enemigos ordenados por energia
        sb.append("Enemigos por energia\n")
        sb.append("Enemigos ordenados por energia: ${enemigos.toList().sortedBy { it.energia }}\n")
        // enemigos ordenados por modelo
        sb.append("Enemigos por modelo\n")
        sb.append("Enemigos ordenados por modelo: ${enemigos.toList().sortedBy { it.model }}\n")
        // Enemigos vivos
        sb.append("Enemigos vivos: ${enemigos.toList().count { it.isAlive }}\n")
        // Enemigos muertos
        sb.append("Enemigos muertos: ${enemigos.toList().count { !it.isAlive }}\n")
        return sb.toString()
    }


    // Clase que representa el estado de la vista
    data class StarWarsState(
        val dimensionEspacio: Int = 0,
        val droidesTotal: Int = 0,
        val tiempoTotal: Int = 0,
        val tiempoActual: Int = 0,
        val operacion: String = "",
        val cuadrante: String = "",
        val disparos: Int = 0,
        val aciertos: Int = 0,
        val muertos: Int = 0,
        val resultado: Result<StarWarsResultado, StarWarsError> = Ok(StarWarsResultado.StarWarsLoading)
    )

}
