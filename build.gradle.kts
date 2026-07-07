// Build script raíz: solo declara los plugins usados por los módulos, sin aplicarlos aquí.
plugins {
    id("com.android.application") version "8.5.0" apply false
    id("org.jetbrains.kotlin.android") version "2.0.0" apply false
    id("com.google.devtools.ksp") version "2.0.0-1.0.21" apply false
    // AGREGA ESTA LÍNEA ABAJO:
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.0" apply false
}