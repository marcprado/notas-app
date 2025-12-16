package com.notasapp.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.PendingActions
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.notasapp.data.Nota
import com.notasapp.model.Categoria
import com.notasapp.navigation.Screen
import com.notasapp.utils.DateUtils
import com.notasapp.utils.ShareHelper
import com.notasapp.viewmodel.NotaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: NotaViewModel
) {
    val context = LocalContext.current
    val notas by viewModel.allNotas.collectAsState()
    val notasPendientes by viewModel.notasPendientes.collectAsState()

    var mostrarFiltros by remember { mutableStateOf(false) }
    var filtroActivo by remember { mutableStateOf<String?>(null) }
    var notaAEliminar by remember { mutableStateOf<Nota?>(null) }
    var mostrarEstadisticas by remember { mutableStateOf(false) }

    val notasFiltradas = if (filtroActivo != null) {
        notas.filter { it.categoria == filtroActivo || it.prioridad == filtroActivo }
    } else notas

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(text = "MIS NOTAS", fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                    }
                },
                actions = {
                    IconButton(onClick = { mostrarEstadisticas = !mostrarEstadisticas }) {
                        Icon(Icons.Default.BarChart, contentDescription = "Estadisticas")
                    }
                    IconButton(onClick = { mostrarFiltros = !mostrarFiltros }) {
                        Icon(Icons.Default.FilterList, contentDescription = "Filtros")
                    }
                    if (notasFiltradas.isNotEmpty()) {
                        IconButton(onClick = { ShareHelper.compartirListaNotas(context, notasFiltradas) }) {
                            Icon(Icons.Default.Share, contentDescription = "Compartir")
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(Screen.Form.createRoute()) },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Nueva nota")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize().padding(paddingValues)
        ) {
            AnimatedVisibility(
                visible = mostrarFiltros,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                FiltrosPanel(
                    filtroSeleccionado = filtroActivo,
                    onFiltroSeleccionado = { filtroActivo = it }
                )
            }

            if (mostrarEstadisticas) {
                EstadisticasCard(
                    total = notas.size,
                    pendientes = notasPendientes.size,
                    completadas = notas.count { it.completada }
                )
            }

            if (notasFiltradas.isEmpty()) {
                EmptyState(mensaje = if (filtroActivo != null) "No hay notas con este filtro" else "No tienes notas aun")
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(items = notasFiltradas, key = { it.id }) { nota ->
                        NotaCard(
                            nota = nota,
                            onClick = { navController.navigate(Screen.Detail.createRoute(nota.id)) },
                            onEdit = { navController.navigate(Screen.Form.createRoute(nota.id)) },
                            onDelete = { notaAEliminar = nota },
                            onShare = { ShareHelper.compartirNota(context, nota) },
                            onToggleCompletada = { viewModel.toggleCompletada(nota.id, !nota.completada) }
                        )
                    }
                }
            }
        }
    }

    notaAEliminar?.let { nota ->
        AlertDialog(
            onDismissRequest = { notaAEliminar = null },
            title = { Text(text = "Eliminar Nota", fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold) },
            text = { Text(text = "Estas seguro de eliminar '${nota.titulo}'?", fontFamily = FontFamily.Monospace) },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.deleteNota(nota)
                    notaAEliminar = null
                }) {
                    Text("ELIMINAR", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = { TextButton(onClick = { notaAEliminar = null }) { Text("CANCELAR") } }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FiltrosPanel(
    filtroSeleccionado: String?,
    onFiltroSeleccionado: (String?) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surfaceVariant).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(text = "FILTRAR POR:", fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold, fontSize = 12.sp)

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FilterChip(
                selected = filtroSeleccionado == null,
                onClick = { onFiltroSeleccionado(null) },
                label = { Text("TODAS") }
            )

            Categoria.entries.take(3).forEach { categoria ->
                FilterChip(
                    selected = filtroSeleccionado == categoria.name,
                    onClick = { onFiltroSeleccionado(categoria.name) },
                    label = { Text(categoria.displayName.uppercase()) }
                )
            }
        }
    }
}

@Composable
fun EstadisticasCard(total: Int, pendientes: Int, completadas: Int) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(16.dp)
            .background(MaterialTheme.colorScheme.secondaryContainer, RoundedCornerShape(12.dp))
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        StatItem(Icons.Default.Description, total.toString(), "TOTAL")
        StatItem(Icons.Default.PendingActions, pendientes.toString(), "PENDIENTES")
        StatItem(Icons.Default.CheckCircle, completadas.toString(), "COMPLETADAS")
    }
}

@Composable
fun StatItem(icon: androidx.compose.ui.graphics.vector.ImageVector, value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Icon(icon, contentDescription = null, modifier = Modifier.size(28.dp))
        Text(text = value, fontWeight = FontWeight.Bold, fontSize = 24.sp)
        Text(text = label, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
    }
}

@Composable
fun NotaCard(
    nota: Nota,
    onClick: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onShare: () -> Unit,
    onToggleCompletada: () -> Unit
) {
    val prioridadColor = when (nota.prioridad) {
        "ALTA" -> Color(0xFFFF5252)
        "MEDIA" -> Color(0xFFFFB300)
        else -> Color(0xFF00E676)
    }

    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (nota.completada) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.surface
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(modifier = Modifier.size(12.dp).clip(CircleShape).background(prioridadColor))

                    Text(
                        text = nota.titulo,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        textDecoration = if (nota.completada) TextDecoration.LineThrough else TextDecoration.None
                    )

                    if (nota.tieneFoto) {
                        Icon(Icons.Default.Image, contentDescription = null, modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.primary)
                    }
                }

                Checkbox(checked = nota.completada, onCheckedChange = { onToggleCompletada() })
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = nota.contenido,
                fontSize = 14.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    AssistChip(
                        onClick = {},
                        label = { Text(nota.categoria, fontSize = 11.sp) },
                        leadingIcon = { Icon(Icons.Default.Folder, contentDescription = null, modifier = Modifier.size(14.dp)) }
                    )
                    Text(
                        text = DateUtils.getRelativeTime(nota.fechaCreacion),
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                }

                Row {
                    IconButton(onClick = onShare, modifier = Modifier.size(36.dp)) {
                        Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(18.dp))
                    }
                    IconButton(onClick = onEdit, modifier = Modifier.size(36.dp)) {
                        Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(18.dp))
                    }
                    IconButton(onClick = onDelete, modifier = Modifier.size(36.dp)) {
                        Icon(Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(18.dp), tint = MaterialTheme.colorScheme.error)
                    }
                }
            }
        }
    }
}

@Composable
fun EmptyState(mensaje: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Icon(Icons.Default.Description, contentDescription = null, modifier = Modifier.size(80.dp), tint = MaterialTheme.colorScheme.outline)
            Text(text = mensaje, fontFamily = FontFamily.Monospace, fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
