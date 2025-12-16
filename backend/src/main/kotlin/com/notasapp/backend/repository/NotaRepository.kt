package com.notasapp.backend.repository

import com.notasapp.backend.entity.Nota
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface NotaRepository : JpaRepository<Nota, Long> {
    fun findByUsuarioId(usuarioId: Long): List<Nota>
    fun findByCategoria(categoria: String): List<Nota>
    fun findByPrioridad(prioridad: String): List<Nota>
}
