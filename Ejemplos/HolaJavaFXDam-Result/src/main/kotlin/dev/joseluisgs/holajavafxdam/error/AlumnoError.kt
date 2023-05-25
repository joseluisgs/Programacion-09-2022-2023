package dev.joseluisgs.holajavafxdam.error

sealed class AlumnoError(val mensaje: String) {
    class NotSaved(mensaje: String) : AlumnoError(mensaje)
    class BadRequest(mensaje: String) : AlumnoError(mensaje)
}