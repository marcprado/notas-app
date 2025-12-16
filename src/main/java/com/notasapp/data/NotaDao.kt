package com.notasapp.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface NotaDao {

    @Query("SELECT * FROM notas ORDER BY fechaCreacion DESC")
    fun getAllNotas(): Flow<List<Nota>>

    @Query("SELECT * FROM notas WHERE categoria = :categoria ORDER BY fechaCreacion DESC")
    fun getNotasByCategoria(categoria: String): Flow<List<Nota>>

    @Query("SELECT * FROM notas WHERE prioridad = :prioridad ORDER BY fechaCreacion DESC")
    fun getNotasByPrioridad(prioridad: String): Flow<List<Nota>>

    @Query("SELECT * FROM notas WHERE completada = 0 ORDER BY fechaCreacion DESC")
    fun getNotasPendientes(): Flow<List<Nota>>

    @Query("SELECT * FROM notas WHERE id = :id")
    suspend fun getNotaById(id: Int): Nota?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNota(nota: Nota): Long

    @Update
    suspend fun updateNota(nota: Nota)

    @Delete
    suspend fun deleteNota(nota: Nota)

    @Query("UPDATE notas SET completada = :completada WHERE id = :id")
    suspend fun marcarCompletada(id: Int, completada: Boolean)

    @Query("DELETE FROM notas WHERE completada = 1")
    suspend fun deleteCompletadas()

    @Query("SELECT COUNT(*) FROM notas")
    suspend fun getCount(): Int

    @Query("SELECT COUNT(*) FROM notas WHERE prioridad = :prioridad")
    suspend fun getCountByPrioridad(prioridad: String): Int
}
