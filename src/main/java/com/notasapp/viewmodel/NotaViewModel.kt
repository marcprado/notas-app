package com.notasapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.notasapp.data.AppDatabase
import com.notasapp.data.Nota
import com.notasapp.model.NotaRepository
import com.notasapp.utils.ValidationException
import com.notasapp.utils.ValidationUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class NotaViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: NotaRepository

    val allNotas: StateFlow<List<Nota>>
    val notasPendientes: StateFlow<List<Nota>>

    private val _titulo = MutableStateFlow("")
    val titulo = _titulo.asStateFlow()

    private val _contenido = MutableStateFlow("")
    val contenido = _contenido.asStateFlow()

    private val _categoria = MutableStateFlow("")
    val categoria = _categoria.asStateFlow()

    private val _prioridad = MutableStateFlow("MEDIA")
    val prioridad = _prioridad.asStateFlow()

    private val _fotoUri = MutableStateFlow("")
    val fotoUri = _fotoUri.asStateFlow()

    private val _completada = MutableStateFlow(false)
    val completada = _completada.asStateFlow()

    private val _tituloError = MutableStateFlow<String?>(null)
    val tituloError = _tituloError.asStateFlow()

    private val _contenidoError = MutableStateFlow<String?>(null)
    val contenidoError = _contenidoError.asStateFlow()

    private val _categoriaError = MutableStateFlow<String?>(null)
    val categoriaError = _categoriaError.asStateFlow()

    private val _isSaving = MutableStateFlow(false)
    val isSaving = _isSaving.asStateFlow()

    private var notaEnEdicion: Nota? = null

    private val _filtroCategoria = MutableStateFlow<String?>(null)
    val filtroCategoria = _filtroCategoria.asStateFlow()

    private val _filtroPrioridad = MutableStateFlow<String?>(null)
    val filtroPrioridad = _filtroPrioridad.asStateFlow()

    init {
        val notaDao = AppDatabase.getDatabase(application).notaDao()
        repository = NotaRepository(notaDao)

        allNotas = repository.allNotas.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
        notasPendientes = repository.notasPendientes.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    }

    fun updateTitulo(value: String) {
        _titulo.value = value
        _tituloError.value = ValidationUtils.getTituloErrorMessage(value)
    }

    fun updateContenido(value: String) {
        _contenido.value = value
        _contenidoError.value = ValidationUtils.getContenidoErrorMessage(value)
    }

    fun updateCategoria(value: String) {
        _categoria.value = value
        _categoriaError.value = if (value.isBlank()) "Seleccione una categoria" else null
    }

    fun updatePrioridad(value: String) {
        _prioridad.value = value
    }

    fun updateFotoUri(value: String) {
        _fotoUri.value = value
    }

    fun updateCompletada(value: Boolean) {
        _completada.value = value
    }

    private fun validateForm(): Boolean {
        return try {
            ValidationUtils.validateNotaCompleta(
                _titulo.value,
                _contenido.value,
                _categoria.value
            )
            _tituloError.value = null
            _contenidoError.value = null
            _categoriaError.value = null
            true
        } catch (_: ValidationException) {
            _tituloError.value = ValidationUtils.getTituloErrorMessage(_titulo.value)
            _contenidoError.value = ValidationUtils.getContenidoErrorMessage(_contenido.value)
            _categoriaError.value = if (_categoria.value.isBlank()) "Seleccione una categoria" else null
            false
        }
    }

    fun saveNota(onSuccess: () -> Unit, onError: (String) -> Unit) {
        if (!validateForm()) {
            onError("Corrige los errores del formulario")
            return
        }

        viewModelScope.launch {
            try {
                _isSaving.value = true

                val nota = Nota(
                    id = notaEnEdicion?.id ?: 0,
                    titulo = _titulo.value.trim(),
                    contenido = _contenido.value.trim(),
                    categoria = _categoria.value,
                    prioridad = _prioridad.value,
                    completada = _completada.value,
                    tieneFoto = _fotoUri.value.isNotEmpty(),
                    fotoUri = _fotoUri.value,
                    fechaCreacion = notaEnEdicion?.fechaCreacion ?: System.currentTimeMillis()
                )

                if (notaEnEdicion != null) {
                    repository.updateNota(nota)
                } else {
                    repository.insertNota(nota)
                }

                clearForm()
                onSuccess()
            } catch (e: Exception) {
                onError("Error al guardar: ${e.message}")
            } finally {
                _isSaving.value = false
            }
        }
    }

    fun loadNota(notaId: Int) {
        viewModelScope.launch {
            val nota = repository.getNotaById(notaId) ?: return@launch
            notaEnEdicion = nota
            _titulo.value = nota.titulo
            _contenido.value = nota.contenido
            _categoria.value = nota.categoria
            _prioridad.value = nota.prioridad
            _fotoUri.value = nota.fotoUri
            _completada.value = nota.completada
        }
    }

    suspend fun getNotaById(notaId: Int): Nota? = repository.getNotaById(notaId)

    fun deleteNota(nota: Nota) {
        viewModelScope.launch { repository.deleteNota(nota) }
    }

    fun toggleCompletada(notaId: Int, completada: Boolean) {
        viewModelScope.launch { repository.marcarCompletada(notaId, completada) }
    }

    fun deleteCompletadas() {
        viewModelScope.launch { repository.deleteCompletadas() }
    }

    fun clearForm() {
        notaEnEdicion = null
        _titulo.value = ""
        _contenido.value = ""
        _categoria.value = ""
        _prioridad.value = "MEDIA"
        _fotoUri.value = ""
        _completada.value = false

        _tituloError.value = null
        _contenidoError.value = null
        _categoriaError.value = null
    }

    fun setFiltroCategoria(categoria: String?) {
        _filtroCategoria.value = categoria
    }

    fun setFiltroPrioridad(prioridad: String?) {
        _filtroPrioridad.value = prioridad
    }

    fun getNotasFiltradas(): StateFlow<List<Nota>> {
        return combine(allNotas, filtroCategoria, filtroPrioridad) { notas, cat, prio ->
            var out = notas
            if (cat != null) out = out.filter { it.categoria == cat }
            if (prio != null) out = out.filter { it.prioridad == prio }
            out
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    }

    suspend fun getEstadisticas(): Map<String, Int> {
        return try {
            mapOf(
                "total" to repository.getCount(),
                "alta" to repository.getCountByPrioridad("ALTA"),
                "media" to repository.getCountByPrioridad("MEDIA"),
                "baja" to repository.getCountByPrioridad("BAJA")
            )
        } catch (_: Exception) {
            emptyMap()
        }
    }
}
