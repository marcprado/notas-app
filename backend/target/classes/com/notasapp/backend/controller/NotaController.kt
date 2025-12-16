package com.notasapp.backend.controller

import com.notasapp.backend.entity.Nota
import com.notasapp.backend.service.NotaService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/notas")
@CrossOrigin(origins = ["*"])
class NotaController(private val service: NotaService) {

    @GetMapping
    fun getAllNotas(): ResponseEntity<List<Nota>> =
        ResponseEntity.ok(service.getAll())

    @GetMapping("/{id}")
    fun getNotaById(@PathVariable id: Long): ResponseEntity<Nota> {
        val nota = service.getById(id)
        return if (nota != null) ResponseEntity.ok(nota) else ResponseEntity.notFound().build()
    }

    @GetMapping("/usuario/{usuarioId}")
    fun getNotasByUsuario(@PathVariable usuarioId: Long): ResponseEntity<List<Nota>> =
        ResponseEntity.ok(service.getByUsuario(usuarioId))

    @PostMapping
    fun createNota(@RequestBody nota: Nota): ResponseEntity<Nota> =
        ResponseEntity.status(HttpStatus.CREATED).body(service.create(nota))

    @PutMapping("/{id}")
    fun updateNota(@PathVariable id: Long, @RequestBody nota: Nota): ResponseEntity<Nota> {
        val updated = service.update(id, nota)
        return if (updated != null) ResponseEntity.ok(updated) else ResponseEntity.notFound().build()
    }

    @DeleteMapping("/{id}")
    fun deleteNota(@PathVariable id: Long): ResponseEntity<Void> =
        if (service.delete(id)) ResponseEntity.noContent().build() else ResponseEntity.notFound().build()

    @PatchMapping("/{id}/completar")
    fun marcarCompletada(@PathVariable id: Long): ResponseEntity<Nota> {
        val nota = service.marcarCompletada(id)
        return if (nota != null) ResponseEntity.ok(nota) else ResponseEntity.notFound().build()
    }
}
