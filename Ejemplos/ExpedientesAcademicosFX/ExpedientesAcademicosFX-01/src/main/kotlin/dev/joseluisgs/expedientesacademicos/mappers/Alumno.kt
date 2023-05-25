package dev.joseluisgs.expedientesacademicos.mappers

import database.AlumnoEntity
import dev.joseluisgs.expedientesacademicos.dto.json.AlumnoDto
import dev.joseluisgs.expedientesacademicos.models.Alumno
import dev.joseluisgs.expedientesacademicos.viewmodels.ExpedientesViewModel.AlumnoState
import java.time.LocalDate

fun AlumnoDto.toModel(): Alumno {
    return Alumno(
        id,
        apellidos,
        nombre,
        email,
        LocalDate.parse(fechaNacimiento),
        calificacion,
        repetidor,
        imagen
    )
}

fun List<AlumnoDto>.toModel(): List<Alumno> {
    return map { it.toModel() }
}

fun Alumno.toDto(): AlumnoDto {
    return AlumnoDto(
        id,
        apellidos,
        nombre,
        email,
        fechaNacimiento.toString(),
        calificacion,
        repetidor,
        imagen
    )
}

fun List<Alumno>.toDto(): List<AlumnoDto> {
    return map { it.toDto() }
}

fun AlumnoEntity.toModel(): Alumno {
    return Alumno(
        id,
        apellidos,
        nombre,
        email,
        LocalDate.parse(fechaNacimiento),
        calificacion,
        repetidor == 1L,
        imagen
    )
}

fun AlumnoState.toModel(): Alumno {
    return Alumno(
        id = if (numero.value.trim().isBlank()) Alumno.NEW_ALUMNO else numero.value.toLong(),
        apellidos = apellidos.value.trim(),
        nombre = nombre.value.trim(),
        email = email.value.trim(),
        fechaNacimiento = fechaNacimiento.value,
        calificacion = calificacion.value.trim().replace(",", ".").toDouble(),
        repetidor = repetidor.value,
        imagen = imagen.value.url ?: "sin-imagen.png"
    )
}

