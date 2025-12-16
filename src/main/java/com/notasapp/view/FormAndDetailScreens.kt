package com.notasapp.view

import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.notasapp.model.Categoria
import com.notasapp.model.Prioridad
import com.notasapp.navigation.Screen
import com.notasapp.utils.CameraHelper
import com.notasapp.utils.DateUtils
import com.notasapp.utils.ShareHelper
import com.notasapp.viewmodel.NotaViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormScreen(
    navController: NavController,
    viewModel: NotaViewModel,
    notaId: Int? = null
) {
    val scrollState = rememberScrollState()
    val titulo by viewModel.titulo.collectAsState()
    val contenido by viewModel.contenido.collectAsState()
    val categoria by viewModel.categoria.collectAsState()
    val prioridad by viewModel.prioridad.collectAsState()
    val fotoUri by viewModel.fotoUri.collectAsState()
    val isSaving by viewModel.isSaving.collectAsState()

    val tituloError by viewModel.tituloError.collectAsState()
    val contenidoError by viewModel.contenidoError.collectAsState()
    val categoriaError by viewModel.categoriaError.collectAsState()

    var mostrarCategorias by remember { mutableStateOf(false) }
    var mostrarPrioridades by remember { mutableStateOf(false) }
    var currentPhotoPath by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var snackbarMessage by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current

    val cameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            viewModel.updateFotoUri(currentPhotoPath)
            snackbarMessage = "Foto capturada"
        }
    }

    LaunchedEffect(notaId) {
        if (notaId != null && notaId != -1) {
            viewModel.loadNota(notaId)
        } else {
            viewModel.clearForm()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (notaId != null) "EDITAR NOTA" else "NUEVA NOTA", fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = {
                        viewModel.clearForm()
                        navController.navigateUp()
                    }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            Column(
                modifier = Modifier.fillMaxSize().verticalScroll(scrollState).padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)) {
                    Row(modifier = Modifier.padding(12.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Icon(Icons.Default.Info, contentDescription = null)
                        Text(text = "Completa todos los campos. Validacion en tiempo real.", fontSize = 12.sp)
                    }
                }

                OutlinedTextField(
                    value = titulo,
                    onValueChange = { viewModel.updateTitulo(it) },
                    label = { Text("TITULO *") },
                    isError = tituloError != null,
                    supportingText = { tituloError?.let { Text(it) } },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    leadingIcon = { Icon(Icons.Default.Description, contentDescription = null) }
                )

                OutlinedTextField(
                    value = contenido,
                    onValueChange = { viewModel.updateContenido(it) },
                    label = { Text("CONTENIDO *") },
                    isError = contenidoError != null,
                    supportingText = { contenidoError?.let { Text(it) } },
                    modifier = Modifier.fillMaxWidth().height(150.dp),
                    maxLines = 6,
                    leadingIcon = { Icon(Icons.Default.Description, contentDescription = null) }
                )

                OutlinedButton(onClick = { mostrarCategorias = true }, modifier = Modifier.fillMaxWidth()) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Icon(Icons.Default.Folder, contentDescription = null)
                            Text(if (categoria.isEmpty()) "SELECCIONAR CATEGORIA *" else Categoria.fromString(categoria).displayName.uppercase())
                        }
                        Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                    }
                }

                if (categoriaError != null) {
                    Text(text = categoriaError ?: "", color = MaterialTheme.colorScheme.error, fontSize = 12.sp, modifier = Modifier.padding(start = 16.dp))
                }

                OutlinedButton(onClick = { mostrarPrioridades = true }, modifier = Modifier.fillMaxWidth()) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Icon(Icons.Default.Flag, contentDescription = null)
                            Text("PRIORIDAD: ${Prioridad.fromString(prioridad).displayName.uppercase()}")
                        }
                        Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                    }
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text(text = "FOTO ADJUNTA", fontWeight = FontWeight.Bold)

                        if (fotoUri.isNotEmpty()) {
                            var imageBitmap by remember { mutableStateOf<ImageBitmap?>(null) }
                            var isLoading by remember { mutableStateOf(true) }

                            LaunchedEffect(fotoUri) {
                                isLoading = true
                                withContext(Dispatchers.IO) {
                                    val bitmap = BitmapFactory.decodeFile(fotoUri)
                                    withContext(Dispatchers.Main) {
                                        imageBitmap = bitmap?.asImageBitmap()
                                        isLoading = false
                                    }
                                }
                            }

                            Box(
                                modifier = Modifier.fillMaxWidth().height(200.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                if (isLoading) {
                                    CircularProgressIndicator()
                                } else if (imageBitmap != null) {
                                    Image(
                                        bitmap = imageBitmap!!,
                                        contentDescription = "Foto adjunta",
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.Crop
                                    )
                                } else {
                                    Text("No se pudo cargar la imagen")
                                }
                            }
                            Spacer(Modifier.height(8.dp))
                            IconButton(onClick = {
                                CameraHelper.deleteImageFile(fotoUri)
                                viewModel.updateFotoUri("")
                            }, modifier = Modifier.align(Alignment.End)) {
                                Icon(Icons.Default.Delete, contentDescription = "Eliminar Foto", tint = MaterialTheme.colorScheme.error)
                            }
                        }

                        Button(
                            onClick = {
                                try {
                                    val photoFile = CameraHelper.createImageFile(context)
                                    currentPhotoPath = photoFile.absolutePath
                                    imageUri = CameraHelper.getImageUri(context, photoFile)
                                    imageUri?.let { cameraLauncher.launch(it) }
                                } catch (e: Exception) {
                                    snackbarMessage = "Error: ${e.message}"
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Default.CameraAlt, contentDescription = null)
                            Spacer(Modifier.width(8.dp))
                            Text(if (fotoUri.isEmpty()) "TOMAR FOTO" else "CAMBIAR FOTO")
                        }
                    }
                }
                Spacer(Modifier.height(80.dp))
            }

            Button(
                onClick = {
                    viewModel.saveNota(
                        onSuccess = {
                            snackbarMessage = if (notaId != null) "Nota actualizada" else "Nota guardada"
                            navController.navigateUp()
                        },
                        onError = { snackbarMessage = it }
                    )
                },
                modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth().padding(16.dp),
                enabled = !isSaving
            ) {
                if (isSaving) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                } else {
                    Icon(Icons.Default.Save, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text(if (notaId != null) "ACTUALIZAR" else "GUARDAR")
                }
            }
        }
    }

    if (mostrarCategorias) {
        AlertDialog(onDismissRequest = { mostrarCategorias = false }, title = { Text("SELECCIONAR CATEGORIA") }, text = { CategoriaSelection(viewModel, onSelect = { mostrarCategorias = false }, current = categoria) }, confirmButton = {}) 
    }

    if (mostrarPrioridades) {
        AlertDialog(onDismissRequest = { mostrarPrioridades = false }, title = { Text("SELECCIONAR PRIORIDAD") }, text = { PrioridadSelection(viewModel, onSelect = { mostrarPrioridades = false }, current = prioridad) }, confirmButton = {}) 
    }

    snackbarMessage?.let { mensaje ->
        LaunchedEffect(mensaje) {
            delay(2000)
            snackbarMessage = null
        }
        Box(modifier = Modifier.fillMaxSize().padding(bottom = 80.dp), contentAlignment = Alignment.BottomCenter) {
             Snackbar(modifier = Modifier.padding(16.dp)) { Text(mensaje) }
        }
    }
}

@Composable
fun CategoriaSelection(viewModel: NotaViewModel, onSelect: () -> Unit, current: String) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Categoria.entries.forEach { cat ->
            Card(modifier = Modifier.fillMaxWidth().clickable { viewModel.updateCategoria(cat.name); onSelect() }, colors = CardDefaults.cardColors(containerColor = if (current == cat.name) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface)) {
                Text(text = cat.displayName, modifier = Modifier.padding(16.dp), fontWeight = if (current == cat.name) FontWeight.Bold else FontWeight.Normal)
            }
        }
    }
}

@Composable
fun PrioridadSelection(viewModel: NotaViewModel, onSelect: () -> Unit, current: String) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Prioridad.entries.forEach { prio ->
            Card(modifier = Modifier.fillMaxWidth().clickable { viewModel.updatePrioridad(prio.name); onSelect() }, colors = CardDefaults.cardColors(containerColor = if (current == prio.name) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface)) {
                Row(modifier = Modifier.padding(16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Flag, contentDescription = null)
                    Text(prio.displayName)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    navController: NavController,
    viewModel: NotaViewModel,
    notaId: Int
) {
    val context = LocalContext.current
    var nota by remember { mutableStateOf<com.notasapp.data.Nota?>(null) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    LaunchedEffect(notaId) {
        nota = viewModel.getNotaById(notaId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("DETALLE NOTA", fontWeight = FontWeight.Bold) },
                navigationIcon = { IconButton(onClick = { navController.navigateUp() }) { Icon(Icons.Default.ArrowBack, contentDescription = "Volver") } },
                actions = {
                    nota?.let { n ->
                        IconButton(onClick = { ShareHelper.compartirNota(context, n) }) { Icon(Icons.Default.Share, contentDescription = "Compartir") }
                        IconButton(onClick = { navController.navigate(Screen.Form.createRoute(n.id)) }) { Icon(Icons.Default.Edit, contentDescription = "Editar") }
                        IconButton(onClick = { showDeleteDialog = true }) { Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = MaterialTheme.colorScheme.error) }
                    }
                }
            )
        }
    ) { paddingValues ->
        nota?.let { n ->
            Column(
                modifier = Modifier.fillMaxSize().padding(paddingValues).verticalScroll(rememberScrollState()).padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) {
                    Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text(text = n.titulo, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                        Text(text = DateUtils.formatDateTime(n.fechaCreacion), fontSize = 12.sp, color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f))
                    }
                }

                Card {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        InfoRow(Icons.Default.Folder, "Categoria", Categoria.fromString(n.categoria).displayName)
                        HorizontalDivider()
                        InfoRow(Icons.Default.Flag, "Prioridad", Prioridad.fromString(n.prioridad).displayName)
                    }
                }

                if (n.fotoUri.isNotEmpty()) {
                    Card(modifier = Modifier.fillMaxWidth()) {
                         Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            Text("FOTO ADJUNTA", fontWeight = FontWeight.Bold)
                            var imageBitmap by remember { mutableStateOf<ImageBitmap?>(null) }
                            var isLoading by remember { mutableStateOf(true) }

                            LaunchedEffect(n.fotoUri) {
                                isLoading = true
                                withContext(Dispatchers.IO) {
                                    val bitmap = BitmapFactory.decodeFile(n.fotoUri)
                                    withContext(Dispatchers.Main) {
                                        imageBitmap = bitmap?.asImageBitmap()
                                        isLoading = false
                                    }
                                }
                            }

                            Box(
                                modifier = Modifier.fillMaxWidth().height(200.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                if (isLoading) {
                                    CircularProgressIndicator()
                                } else if (imageBitmap != null) {
                                    Image(
                                        bitmap = imageBitmap!!,
                                        contentDescription = "Foto adjunta",
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.Crop
                                    )
                                } else {
                                    Text("No se pudo cargar la imagen")
                                }
                            }
                        }
                    }
                }

                Card {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("CONTENIDO", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Spacer(Modifier.height(8.dp))
                        Text(n.contenido, fontSize = 15.sp, lineHeight = 22.sp)
                    }
                }
            }
        } ?: Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }

    if (showDeleteDialog && nota != null) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Eliminar Nota") },
            text = { Text("Estas seguro de eliminar '${nota!!.titulo}'?") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.deleteNota(nota!!)
                    showDeleteDialog = false
                    navController.navigateUp()
                }) { Text("ELIMINAR", color = MaterialTheme.colorScheme.error) }
            },
            dismissButton = { TextButton(onClick = { showDeleteDialog = false }) { Text("CANCELAR") } }
        )
    }
}

@Composable
fun InfoRow(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(20.dp))
            Text(label)
        }
        Text(value, fontWeight = FontWeight.Bold)
    }
}