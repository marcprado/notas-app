package com.notasapp.model

enum class Categoria(val displayName: String) {
    TRABAJO("Trabajo"),
    PERSONAL("Personal"),
    ESTUDIO("Estudio"),
    OTROS("Otros");

    companion object {
        fun fromString(value: String): Categoria {
            return entries.find { it.name == value } ?: OTROS
        }
    }
}

enum class Prioridad(val displayName: String, val color: String) {
    ALTA("Alta", "#FF5252"),
    MEDIA("Media", "#FFB300"),
    BAJA("Baja", "#00E676");

    companion object {
        fun fromString(value: String): Prioridad {
            return entries.find { it.name == value } ?: MEDIA
        }
    }
}
