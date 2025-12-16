package com.notasapp

import com.notasapp.utils.ValidationException
import com.notasapp.utils.ValidationUtils
import org.junit.Assert.*
import org.junit.Test

class ValidationUtilsTest {

    // ---------------------------
    // TITULO
    // ---------------------------

    @Test fun tituloVacioInvalido() {
        assertFalse(ValidationUtils.isValidTitulo(""))
    }

    @Test fun tituloEspaciosInvalido() {
        assertFalse(ValidationUtils.isValidTitulo("   "))
    }

    @Test fun tituloDosCaracteresInvalido() {
        assertFalse(ValidationUtils.isValidTitulo("AB"))
    }

    @Test fun tituloTresCaracteresValido() {
        assertTrue(ValidationUtils.isValidTitulo("ABC"))
    }

    @Test fun tituloValidoNormal() {
        assertTrue(ValidationUtils.isValidTitulo("Nota de prueba"))
    }

    @Test fun tituloExactamente100Valido() {
        assertTrue(ValidationUtils.isValidTitulo("A".repeat(100)))
    }

    @Test fun titulo101Invalido() {
        assertFalse(ValidationUtils.isValidTitulo("A".repeat(101)))
    }

    @Test fun tituloConSaltosLineaInvalidoPorBlank() {
        assertFalse(ValidationUtils.isValidTitulo("\n\n"))
    }

    @Test fun tituloConCaracteresEspecialesValido() {
        assertTrue(ValidationUtils.isValidTitulo("Nota #1 - Importante!"))
    }

    @Test fun tituloErrorMensajeObligatorio() {
        assertEquals("El titulo es obligatorio", ValidationUtils.getTituloErrorMessage(""))
    }

    @Test fun tituloErrorMensajeCorto() {
        assertEquals("El titulo debe tener al menos 3 caracteres", ValidationUtils.getTituloErrorMessage("AB"))
    }

    @Test fun tituloErrorMensajeLargo() {
        assertEquals("El titulo no puede exceder 100 caracteres", ValidationUtils.getTituloErrorMessage("A".repeat(101)))
    }

    @Test fun tituloErrorMensajeValidoNull() {
        assertNull(ValidationUtils.getTituloErrorMessage("Nota valida"))
    }

    // ---------------------------
    // CONTENIDO
    // ---------------------------

    @Test fun contenidoVacioInvalido() {
        assertFalse(ValidationUtils.isValidContenido(""))
    }

    @Test fun contenidoEspaciosInvalido() {
        assertFalse(ValidationUtils.isValidContenido("     "))
    }

    @Test fun contenido9Invalido() {
        assertFalse(ValidationUtils.isValidContenido("123456789"))
    }

    @Test fun contenido10Valido() {
        assertTrue(ValidationUtils.isValidContenido("1234567890"))
    }

    @Test fun contenidoValidoNormal() {
        assertTrue(ValidationUtils.isValidContenido("Este es un contenido valido para la nota"))
    }

    @Test fun contenidoExactamente500Valido() {
        assertTrue(ValidationUtils.isValidContenido("A".repeat(500)))
    }

    @Test fun contenido501Invalido() {
        assertFalse(ValidationUtils.isValidContenido("A".repeat(501)))
    }

    @Test fun contenidoConSaltosLineaValido() {
        val c = "Linea 1\nLinea 2\nLinea 3 con suficiente texto"
        assertTrue(ValidationUtils.isValidContenido(c))
    }

    @Test fun contenidoErrorMensajeObligatorio() {
        assertEquals("El contenido es obligatorio", ValidationUtils.getContenidoErrorMessage(""))
    }

    @Test fun contenidoErrorMensajeCorto() {
        assertEquals("El contenido debe tener al menos 10 caracteres", ValidationUtils.getContenidoErrorMessage("Corto"))
    }

    @Test fun contenidoErrorMensajeLargo() {
        assertEquals("El contenido no puede exceder 500 caracteres", ValidationUtils.getContenidoErrorMessage("A".repeat(501)))
    }

    @Test fun contenidoErrorMensajeValidoNull() {
        assertNull(ValidationUtils.getContenidoErrorMessage("Contenido valido y suficiente"))
    }

    // ---------------------------
    // CATEGORIA
    // ---------------------------

    @Test fun categoriaVaciaInvalida() {
        assertFalse(ValidationUtils.isValidCategoria(""))
    }

    @Test fun categoriaEspaciosInvalida() {
        assertFalse(ValidationUtils.isValidCategoria("   "))
    }

    @Test fun categoriaValida() {
        assertTrue(ValidationUtils.isValidCategoria("TRABAJO"))
    }

    // ---------------------------
    // VALIDACION COMPLETA (EXCEPCIONES)
    // ---------------------------

    @Test(expected = ValidationException::class)
    fun validacionCompletaTituloInvalidoLanza() {
        ValidationUtils.validateNotaCompleta(
            titulo = "",
            contenido = "Contenido valido con mas de 10 caracteres",
            categoria = "TRABAJO"
        )
    }

    @Test(expected = ValidationException::class)
    fun validacionCompletaContenidoInvalidoLanza() {
        ValidationUtils.validateNotaCompleta(
            titulo = "Titulo valido",
            contenido = "Corto",
            categoria = "TRABAJO"
        )
    }

    @Test(expected = ValidationException::class)
    fun validacionCompletaCategoriaInvalidaLanza() {
        ValidationUtils.validateNotaCompleta(
            titulo = "Titulo valido",
            contenido = "Contenido valido con mas de 10 caracteres",
            categoria = ""
        )
    }

    @Test
    fun validacionCompletaDatosValidosNoLanza() {
        try {
            ValidationUtils.validateNotaCompleta(
                titulo = "Titulo valido",
                contenido = "Contenido valido con mas de 10 caracteres",
                categoria = "TRABAJO"
            )
            assertTrue(true)
        } catch (e: ValidationException) {
            fail("No deberia lanzar excepcion: ${e.message}")
        }
    }

    // ---------------------------
    // CASOS EXTRA (para llegar a 30+)
    // ---------------------------

    @Test fun tituloConEspaciosAlFinalValido() {
        assertTrue(ValidationUtils.isValidTitulo("Nota valida   "))
    }

    @Test fun contenidoConMuchosEspaciosPeroTextoValido() {
        assertTrue(ValidationUtils.isValidContenido("Texto valido          "))
    }

    @Test fun contenidoSoloSaltosLineaInvalido() {
        assertFalse(ValidationUtils.isValidContenido("\n\n\n"))
    }

    @Test fun tituloConTabInvalidoPorBlank() {
        assertFalse(ValidationUtils.isValidTitulo("\t\t"))
    }

    @Test fun contenidoConTabYTextoValido() {
        assertTrue(ValidationUtils.isValidContenido("\tTexto valido con mas de diez"))
    }
}
