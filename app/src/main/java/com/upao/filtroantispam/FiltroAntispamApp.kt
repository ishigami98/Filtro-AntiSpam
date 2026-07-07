package com.upao.filtroantispam

import android.app.Application
import com.upao.filtroantispam.service.FeedbackLoopObserver

class FiltroAntispamApp : Application() {
    override fun onCreate() {
        super.onCreate()
        // Si el permiso READ_CALL_LOG todavía no está otorgado en este punto,
        // PermisosYRolCard vuelve a intentarlo apenas el usuario lo conceda.
        FeedbackLoopObserver.registrar(this)
    }
}
