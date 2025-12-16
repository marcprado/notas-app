package com.notasapp.utils

object ValidationUtils {

    @Throws(ValidationException::class)
    fun validateNotaCompleta(titulo: String, contenido: String, categoria: String) {
        if (!isValidTitulo(titulo)) {
            throw ValidationException(getTituloErrorMessage(titulo) ?: "Titulo invalido")
        }
        if (!isValidContenido(contenido)) {
            throw ValidationException(getContenidoErrorMessage(contenido) ?: "Contenido invalido")
        }
        if (!isValidCategoria(categoria)) {
            throw ValidationException("Debe seleccionar una categoria")
        }
    }

    fun isValidTitulo(titulo: String): Boolean {
        return titulo.isNotBlank() && titulo.length >= 3 && titulo.length <= 100
    }

    fun isValidContenido(contenido: String): Boolean {
        return contenido.isNotBlank() && contenido.length >= 10 && contenido.length <= 500
    }

    fun isValidCategoria(categoria: String): Boolean {
        return categoria.isNotBlank()
    }

    fun getTituloErrorMessage(titulo: String): String? {
        return when {
            titulo.isBlank() -> "El titulo es obligatorio"
            titulo.length < 3 -> "El titulo debe tener al menos 3 caracteres"
            titulo.length > 100 -> "El titulo no puede exceder 100 caracteres"
            else -> null
        }
    }

    fun getContenidoErrorMessage(contenido: String): String? {
        return when {
            contenido.isBlank() -> "El contenido es obligatorio"
            contenido.length < 10 -> "El contenido debe tener al menos 10 caracteres"
            contenido.length > 500 -> "El contenido no puede exceder 500 caracteres"
            else -> null
        }
    }
}

class ValidationException(message: String) : Exception(message)
