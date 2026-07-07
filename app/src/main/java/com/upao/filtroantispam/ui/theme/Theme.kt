package com.upao.filtroantispam.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val EsquemaClaro = lightColorScheme(
    primary = IndigoPrimario,
    onPrimary = Color.White,
    primaryContainer = IndigoContenedor,
    onPrimaryContainer = IndigoPrimario,
    tertiary = VerdeProtegido,
    tertiaryContainer = VerdeProtegidoContenedor,
    onTertiaryContainer = VerdeProtegido,
    error = RojoSpam,
    errorContainer = RojoSpamContenedor,
    onErrorContainer = RojoSpam,
    background = FondoClaro,
    surface = SuperficieClara,
    onSurfaceVariant = TextoSecundarioClaro
)

private val EsquemaOscuro = darkColorScheme(
    primary = IndigoPrimarioOscuro,
    onPrimary = Color(0xFF1E1B4B),
    primaryContainer = Color(0xFF3730A3),
    onPrimaryContainer = IndigoContenedor,
    tertiary = VerdeProtegido,
    tertiaryContainer = Color(0xFF065F46),
    onTertiaryContainer = VerdeProtegidoContenedor,
    error = RojoSpam,
    errorContainer = Color(0xFF7F1D1D),
    onErrorContainer = RojoSpamContenedor,
    background = FondoOscuro,
    surface = SuperficieOscura,
    onSurfaceVariant = TextoSecundarioOscuro
)

/**
 * Paleta de marca fija a propósito (no dynamic color): la app debe verse igual
 * en cualquier equipo, como Truecaller/CallApp, en vez de heredar el color del
 * fondo de pantalla del usuario.
 */
@Composable
fun FiltroAntispamTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) EsquemaOscuro else EsquemaClaro,
        typography = Typography,
        content = content
    )
}
