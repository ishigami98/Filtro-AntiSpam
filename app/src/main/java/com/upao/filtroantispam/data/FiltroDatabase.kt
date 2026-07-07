package com.upao.filtroantispam.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        RegistroLlamadaEntity::class,
        NumeroBloqueadoEntity::class,
        HorarioBloqueoEntity::class,
        ReporteLocalEntity::class,
        FeedbackUsuarioEntity::class
    ],
    version = 4,
    exportSchema = false
)
abstract class FiltroDatabase : RoomDatabase() {
    abstract fun registroLlamadaDao(): RegistroLlamadaDao
    abstract fun numeroBloqueadoDao(): NumeroBloqueadoDao
    abstract fun horarioBloqueoDao(): HorarioBloqueoDao
    abstract fun reporteLocalDao(): ReporteLocalDao
    abstract fun feedbackUsuarioDao(): FeedbackUsuarioDao

    companion object {
        @Volatile
        private var instancia: FiltroDatabase? = null

        fun obtenerInstancia(context: Context): FiltroDatabase =
            instancia ?: synchronized(this) {
                instancia ?: Room.databaseBuilder(
                    context.applicationContext,
                    FiltroDatabase::class.java,
                    "filtro_antispam.db"
                )
                    // FiltroLlamadasService necesita responder antes de que suene el
                    // teléfono (RNF-02): no hay tiempo para saltar a un hilo aparte.
                    .allowMainThreadQueries()
                    // Mientras el esquema siga cambiando en desarrollo, recrear las
                    // tablas es preferible a escribir migraciones para datos de prueba.
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { instancia = it }
            }
    }
}
