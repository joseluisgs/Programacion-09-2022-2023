package dev.joseluisgs.holajavafxdam.repositories

import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import dev.joseluisgs.holajavafxdam.error.AlumnoError
import dev.joseluisgs.holajavafxdam.models.Alumno
import mu.KotlinLogging

private val logger = KotlinLogging.logger {}

class AlumnosRepository {
    private val cache = mutableListOf<Alumno>()

    fun save(alumno: Alumno): Result<Alumno, AlumnoError> {
        logger.info { "Saving alumno $alumno" }
        cache.add(alumno)
        return Ok(alumno)
    }
}