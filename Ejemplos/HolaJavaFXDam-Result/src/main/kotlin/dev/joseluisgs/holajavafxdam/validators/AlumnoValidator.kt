package dev.joseluisgs.holajavafxdam.validators

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import dev.joseluisgs.holajavafxdam.error.AlumnoError
import dev.joseluisgs.holajavafxdam.models.Alumno

fun Alumno.validate(): Result<Alumno, AlumnoError> {
    if (this.nombre.isEmpty()) {
        return Err(AlumnoError.BadRequest("El nombre no puede estar vacío"))
    }
    if (this.edad < 0) {
        return Err(AlumnoError.BadRequest("La edad no puede ser negativa"))
    }
    // Expressión regular para validar el email
    if (this.email.isEmpty() || !this.email.contains(Regex("^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})"))) {
        return Err(AlumnoError.BadRequest("El email no puede estar vacío o no es válido"))
    }
    return Ok(this)
}

// esto es del formulario por separador
fun edadValidator(edad: String): Result<Int, AlumnoError> {
    if (edad.isEmpty() || edad.toIntOrNull() == null) {
        return Err(AlumnoError.BadRequest("La edad no puede estar vacía o no es válida"))
    }
    return Ok(edad.toInt())
}

fun nombreValidator(nombre: String): Result<String, AlumnoError> {
    if (nombre.isEmpty()) {
        return Err(AlumnoError.BadRequest("El nombre no puede estar vacío"))
    }
    return Ok(nombre)
}

fun emailValidator(email: String): Result<String, AlumnoError> {
    if (email.isEmpty() || !email.contains(Regex("^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})"))) {
        return Err(AlumnoError.BadRequest("El email no puede estar vacío o no es válido"))
    }
    return Ok(email)
}