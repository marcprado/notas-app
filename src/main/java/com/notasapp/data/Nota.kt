package com.notasapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notas")
data class Nota(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val titulo: String,
    val contenido: String,
    val categoria: String,
    val prioridad: String,
    val completada: Boolean = false,
    val tieneFoto: Boolean = false,
    val fotoUri: String = "",
    val fechaCreacion: Long = System.currentTimeMillis(),

    val sincronizado: Boolean = false,
    val idServidor: Long? = null
)
