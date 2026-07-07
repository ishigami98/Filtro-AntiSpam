# Filtro Antispam en Tiempo Real

Proyecto académico (Aprendizaje Estadístico, UPAO) que detecta y bloquea llamadas
spam antes de que suene el teléfono, usando un Random Forest reimplementado como
reglas en Kotlin.

## Cómo abrir el proyecto

1. Abrir Android Studio (Hedgehog o superior) → **Open** → seleccionar la carpeta
   `ProyectoSpamApp`.
2. Dejar que Android Studio genere el Gradle Wrapper (`gradlew`/`gradlew.bat`) y
   sincronice el proyecto — no está incluido en el repo porque requiere el binario
   `gradle-wrapper.jar`.
3. Ejecutar en un emulador o dispositivo con Android 8.0 (API 26) o superior.

## Estructura

```
app/src/main/java/com/upao/filtroantispam/
├── MainActivity.kt
├── service/          # CallScreeningService (Fase 2)
├── data/             # Room: historial y lista negra (Fase 3)
└── ui/
    ├── theme/        # Tema Material 3
    ├── navigation/   # NavHost + barra inferior
    └── screens/      # Registro, Lista Negra, Horario (contenido real: Fase 4)
```

## Fases del proyecto

- **Fase 0** ✅ — Notebook con el modelo (Random Forest, 6 variables conocidas
  antes de contestar la llamada). Ver `Fase0_ModeloAntispam.ipynb`.
- **Fase 1** ✅ — Estructura del proyecto Android (este scaffold).
- **Fase 2** — `CallScreeningService` + lógica de decisión (reglas del RF) en Kotlin.
- **Fase 3** — Base de datos local (Room) y lista negra.
- **Fase 4** — UI real de Registro, Lista Negra y Horario.
- **Fase 5** — Feedback loop (RF-05) y estadísticas (RF-06).
- **Fase 6** — Compilar APK desde Android Studio.

## Variables del modelo (conocidas antes de contestar)

1. `en_contactos` — si está guardado, no pasa por el modelo.
2. `prefijo_riesgo` — prefijo de país infrecuente para el usuario.
3. `frecuencia_llamadas_numero` — llamadas del mismo número en días recientes.
4. `hora_del_dia`.
5. `es_numero_corto_o_extrano` — formato atípico (VoIP/spoofing).
6. `reportes_previos` — reportes de la comunidad.

`duracion` no se usa para predecir en tiempo real; se reutiliza en el feedback
loop (RF-05) después de colgar.
