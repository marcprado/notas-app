package com.notasapp.utils

import android.content.Context
import android.content.Intent
import com.notasapp.data.Nota
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object ShareHelper {

    fun compartirNota(context: Context, nota: Nota) {
        val fecha = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
            .format(Date(nota.fechaCreacion))

        val textoCompartir = buildString {
            appendLine("NOTA")
            appendLine("----------------------")
            appendLine()
            appendLine(nota.titulo)
            appendLine()
            appendLine(nota.contenido)
            appendLine()
            appendLine("----------------------")
            appendLine("Categoria: ${nota.categoria}")
            appendLine("Prioridad: ${nota.prioridad}")
            appendLine("Fecha: $fecha")
            if (nota.completada) appendLine("Estado: COMPLETADA")
            appendLine()
            appendLine("Enviado desde NotasApp")
        }

        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, textoCompartir)
            type = "text/plain"
        }

        context.startActivity(Intent.createChooser(sendIntent, "Compartir nota"))
    }

    fun compartirListaNotas(context: Context, notas: List<Nota>) {
        if (notas.isEmpty()) return

        val textoCompartir = buildString {
            appendLine("MIS NOTAS")
            appendLine("----------------------")
            appendLine()
            notas.forEach { nota ->
                val status = if (nota.completada) "[OK]" else "[PEND]"
                appendLine("$status ${nota.titulo} - ${nota.categoria} | ${nota.prioridad}")
            }
            appendLine()
            appendLine("----------------------")
            appendLine("Total: ${notas.size}")
        }

        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, textoCompartir)
            type = "text/plain"
        }

        context.startActivity(Intent.createChooser(sendIntent, "Compartir lista"))
    }
}
