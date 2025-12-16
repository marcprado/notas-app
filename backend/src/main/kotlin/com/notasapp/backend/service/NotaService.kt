package com.notasapp.backend.service

import com.notasapp.backend.entity.Nota
import com.notasapp.backend.repository.NotaRepository
import org.springframework.stereotype.Service

@Service
class NotaService(private val repository: NotaRepository) {

    fun getAll(): List<Nota> = repository.findAll()

    fun getById(id: Long): Nota? = repository.findById(id).orElse(null)

    fun getByUsuario(usuarioId: Long): List<Nota> = repository.findByUsuarioId(usuarioId)

    fun create(nota: Nota): Nota = repository.save(nota)

    fun update(id: Long, nota: Nota): Nota? {
        return if (repository.existsById(id)) {
            repository.save(nota.copy(id = id))
        } else null
    }

    fun delete(id: Long): Boolean {
        return if (repository.existsById(id)) {
            repository.deleteById(id)
            true
        } else false
    }

    fun marcarCompletada(id: Long): Nota? {
        val nota = repository.findById(id).orElse(null) ?: return null
        return repository.save(nota.copy(completada = true))
    }
}
