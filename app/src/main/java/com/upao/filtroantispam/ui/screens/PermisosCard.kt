package com.upao.filtroantispam.ui.screens

import android.Manifest
import android.app.role.RoleManager
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.upao.filtroantispam.service.FeedbackLoopObserver

private val PERMISOS_NECESARIOS = buildList {
    add(Manifest.permission.READ_PHONE_STATE)
    add(Manifest.permission.READ_CALL_LOG)
    add(Manifest.permission.READ_CONTACTS)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        add(Manifest.permission.POST_NOTIFICATIONS)
    }
}

/** Expone si el filtro está realmente activo, para el estado del dashboard. */
class EstadoProteccion(
    val permisosOtorgados: Boolean,
    val rolOtorgado: Boolean,
    val otorgarPermisos: () -> Unit,
    val activarRol: () -> Unit
) {
    val protegido: Boolean get() = permisosOtorgados && rolOtorgado
}

/**
 * Sin estos permisos y sin el rol de "app de identificador de llamadas y spam",
 * Android nunca invoca a FiltroLlamadasService: esto es lo que hace que el
 * filtrado funcione de verdad, no solo compile.
 */
@Composable
fun rememberEstadoProteccion(): EstadoProteccion {
    val context = LocalContext.current

    fun tienePermisos() = PERMISOS_NECESARIOS.all {
        ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
    }

    fun tieneRol(): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) return false
        val roleManager = context.getSystemService(RoleManager::class.java) ?: return false
        return roleManager.isRoleHeld(RoleManager.ROLE_CALL_SCREENING)
    }

    var permisosOtorgados by remember { mutableStateOf(tienePermisos()) }
    var rolOtorgado by remember { mutableStateOf(tieneRol()) }

    val lanzadorPermisos = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) {
        permisosOtorgados = tienePermisos()
        // Si al arrancar la app el permiso todavía no existía, el observador
        // del feedback loop (RF-05) no se activó: reintentar ahora que se acaba de conceder.
        FeedbackLoopObserver.registrar(context)
    }

    val lanzadorRol = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { rolOtorgado = tieneRol() }

    return EstadoProteccion(
        permisosOtorgados = permisosOtorgados,
        rolOtorgado = rolOtorgado,
        otorgarPermisos = { lanzadorPermisos.launch(PERMISOS_NECESARIOS.toTypedArray()) },
        activarRol = {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val roleManager = context.getSystemService(RoleManager::class.java)
                val intent = roleManager?.createRequestRoleIntent(RoleManager.ROLE_CALL_SCREENING)
                if (intent != null) lanzadorRol.launch(intent)
            } else {
                // Antes de Android 10 no existe RoleManager: se activa manualmente
                // desde Ajustes > Apps > Apps predeterminadas.
                lanzadorRol.launch(Intent(Settings.ACTION_MANAGE_DEFAULT_APPS_SETTINGS))
            }
        }
    )
}

@Composable
fun PermisosYRolCard(estado: EstadoProteccion) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Termina de activar la protección", style = MaterialTheme.typography.titleMedium)

            Text(
                if (estado.permisosOtorgados) "Permisos: concedidos" else "Permisos: faltan",
                modifier = Modifier.padding(top = 8.dp)
            )
            if (!estado.permisosOtorgados) {
                Button(onClick = estado.otorgarPermisos, modifier = Modifier.padding(top = 4.dp)) {
                    Text("Otorgar permisos")
                }
            }

            Text(
                if (estado.rolOtorgado) "App de filtrado de llamadas: activa" else "App de filtrado de llamadas: inactiva",
                modifier = Modifier.padding(top = 8.dp)
            )
            if (!estado.rolOtorgado) {
                Button(onClick = estado.activarRol, modifier = Modifier.padding(top = 4.dp)) {
                    Text("Activar como filtro de llamadas")
                }
            }
        }
    }
}
