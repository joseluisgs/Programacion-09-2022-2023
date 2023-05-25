package dev.joseluisgs.moscafx.viewmodel

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import mu.KotlinLogging


private val logger = KotlinLogging.logger {}

// No me apetece crearme modelos y errores
// Tú no lo hagas así en la misma clase ;)

sealed class MoscaError {
    object NoAcertado : MoscaError()
    class FinIntentos(val mosca: MoscaPosition, val intentos: Int) : MoscaError()
    class Casi(val fila: Int, val columna: Int, val intentos: Int) : MoscaError()
}

class MoscaPosition(val fila: Int, val columna: Int)

class Acertado(val mosca: MoscaPosition, val intentos: Int)

private const val MOSCA = -1

class MoscaViewModel {
    // Creo el estado con la imagen inicial
    val state = MoscaState()

    var matrix = Array(3) { IntArray(3) }

    init {
        logger.info { "MoscaViewModel cargando datos" }
    }

    fun iniciarJuego(dimension: Int, intentos: Int) {
        logger.info { "Iniciando juego con dimension $dimension e intentos $intentos" }
        state.dimension = dimension
        state.intentos = intentos
        state.fila = 0
        state.columna = 0
        state.isTerminado = false
        state.cuadrante.set("")
        state.golpes.set(0)


        // Inicializamos la matriz
        matrix = Array(dimension) { IntArray(dimension) }
        initMatrix()
        situarMosca()
    }

    private fun situarMosca() {
        logger.info { "Situando mosca" }
        // vamos a situar una mosca en una posición aleatoria
        // Debemos repetir mientras donde queremos situar la mosca ya esté ocupado
        var fila: Int
        var columna: Int
        do {
            fila = (matrix.indices).random()
            columna = (matrix.indices).random()
        } while (matrix[fila][columna] == MOSCA)
        // Ya tenemos la posición, la situamos
        matrix[fila][columna] = MOSCA
        // Actualizamos el estado
        state.moscaPosition = MoscaPosition(fila, columna)
        println("La mosca está en la fila ${fila + 1} y columna ${columna + 1}")
    }

    private fun initMatrix() {
        logger.info { "Inicializando matriz" }
        // No es necesario, pero por que  ya lo hace!!!
        for (element in matrix) {
            for (j in matrix.indices) {
                element[j] = 0
            }
        }
    }

    fun golpear(fila: Int, columna: Int): Result<Acertado, MoscaError> {
        logger.info { "Golpear $fila, $columna" }
        // Subimos los golpes
        state.golpes.value++
        // Se ha acabado el juego
        if (state.golpes.value == state.intentos) {
            state.isTerminado = true
            state.cuadrante.value = matrixToAreaFin()
            return Err(MoscaError.FinIntentos(state.moscaPosition, state.golpes.value))
        }

        // La has golpeando??
        if (matrix[fila][columna] == MOSCA) {
            state.isTerminado = true
            state.cuadrante.value = matrixToAreaFin()
            return Ok(Acertado(MoscaPosition(fila, columna), state.golpes.value))
        }

        // Es casi??
        // Nos hemos acercado al menos a 1 de distancia en una de las ocho direcciones
        if (state.moscaPosition.fila - fila in -1..1 && state.moscaPosition.columna - columna in -1..1) {
            state.cuadrante.value = matrixToAreaJuego()
            initMatrix()
            situarMosca()
            return Err(MoscaError.Casi(fila, columna, state.golpes.value))
        }
        state.cuadrante.value = matrixToAreaJuego()
        return Err(MoscaError.NoAcertado)
    }

    private fun matrixToAreaJuego(): String {
        val sb = StringBuilder()
        for (i in matrix.indices) {
            for (j in matrix[i].indices) {
                sb.append("[   ]")
            }
            sb.append("\n")
        }
        return sb.toString()
    }

    private fun matrixToAreaFin(): String {
        val sb = StringBuilder()
        for (i in matrix.indices) {
            for (j in matrix[i].indices) {
                if (matrix[i][j] == MOSCA) {
                    sb.append("[ X ]")
                } else {
                    sb.append("[   ]")
                }
            }
            sb.append("\n")
        }
        return sb.toString()
    }


    // Clase que representa el estado de la vista
    data class MoscaState(
        var dimension: Int = 0,
        var intentos: Int = 0,
        var fila: Int = 0,
        var columna: Int = 0,
        var isTerminado: Boolean = false,
        val cuadrante: SimpleStringProperty = SimpleStringProperty(""), // binding en la IU
        val golpes: SimpleIntegerProperty = SimpleIntegerProperty(0), // Binding en la IU
        var moscaPosition: MoscaPosition = MoscaPosition(0, 0)
    )
}
