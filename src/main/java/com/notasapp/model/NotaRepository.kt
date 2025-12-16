package com.notasapp.model

import com.notasapp.data.Nota
import com.notasapp.data.NotaDao
import kotlinx.coroutines.flow.Flow

class NotaRepository(private val notaDao: NotaDao) {

    val allNotas: Flow<List<Nota>> = notaDao.getAllNotas()
    val notasPendientes: Flow<List<Nota>> = notaDao.getNotasPendientes()

    suspend fun insertNota(nota: Nota): Long = notaDao.insertNota(nota)

    suspend fun updateNota(nota: Nota) {
        notaDao.updateNota(nota)
    }

    suspend fun deleteNota(nota: Nota) {
        notaDao.deleteNota(nota)
    }

    suspend fun getNotaById(id: Int): Nota? = notaDao.getNotaById(id)

    fun getNotasByCategoria(categoria: String): Flow<List<Nota>> = notaDao.getNotasByCategoria(categoria)

    fun getNotasByPrioridad(prioridad: String): Flow<List<Nota>> = notaDao.getNotasByPrioridad(prioridad)

    suspend fun marcarCompletada(id: Int, completada: Boolean) {
        notaDao.marcarCompletada(id, completada)
    }

    suspend fun deleteCompletadas() {
        notaDao.deleteCompletadas()
    }

    suspend fun getCount(): Int = notaDao.getCount()

    suspend fun getCountByPrioridad(prioridad: String): Int = notaDao.getCountByPrioridad(prioridad)
}
