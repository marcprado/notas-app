package com.notasapp.backend.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "notas")
data class Nota(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false, length = 100)
    val titulo: String,

    @Column(nullable = false, length = 500)
    val contenido: String,

    @Column(nullable = false)
    val categoria: String,

    @Column(nullable = false)
    val prioridad: String,

    @Column(name = "usuario_id", nullable = false)
    val usuarioId: Long,

    @Column(name = "fecha_creacion")
    val fechaCreacion: LocalDateTime = LocalDateTime.now(),

    @Column(name = "completada")
    val completada: Boolean = false,

    @Column(name = "tiene_foto")
    val tieneFoto: Boolean = false
)
