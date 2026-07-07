package com.upao.filtroantispam.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.upao.filtroantispam.ui.screens.EstadisticasScreen
import com.upao.filtroantispam.ui.screens.HorarioScreen
import com.upao.filtroantispam.ui.screens.ListaNegraScreen
import com.upao.filtroantispam.ui.screens.RegistroScreen

sealed class Pantalla(val ruta: String, val etiqueta: String) {
    data object Registro : Pantalla("registro", "Registro")
    data object ListaNegra : Pantalla("lista_negra", "Lista Negra")
    data object Horario : Pantalla("horario", "Horario")
    data object Estadisticas : Pantalla("estadisticas", "Estadísticas")
}

private val pantallas = listOf(Pantalla.Registro, Pantalla.ListaNegra, Pantalla.Horario, Pantalla.Estadisticas)

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                pantallas.forEach { pantalla ->
                    val seleccionada = currentDestination?.hierarchy?.any { it.route == pantalla.ruta } == true
                    NavigationBarItem(
                        selected = seleccionada,
                        onClick = {
                            navController.navigate(pantalla.ruta) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            val icono = when (pantalla) {
                                Pantalla.Registro -> Icons.Filled.History
                                Pantalla.ListaNegra -> Icons.Filled.Block
                                Pantalla.Horario -> Icons.Filled.Schedule
                                Pantalla.Estadisticas -> Icons.Filled.BarChart
                            }
                            Icon(icono, contentDescription = pantalla.etiqueta)
                        },
                        label = { Text(pantalla.etiqueta) }
                    )
                }
            }
        }
    ) { paddingInterno ->
        NavHost(
            navController = navController,
            startDestination = Pantalla.Registro.ruta
        ) {
            composable(Pantalla.Registro.ruta) { RegistroScreen(paddingInterno) }
            composable(Pantalla.ListaNegra.ruta) { ListaNegraScreen(paddingInterno) }
            composable(Pantalla.Horario.ruta) { HorarioScreen(paddingInterno) }
            composable(Pantalla.Estadisticas.ruta) { EstadisticasScreen(paddingInterno) }
        }
    }
}
