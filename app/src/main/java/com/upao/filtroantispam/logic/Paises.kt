package com.upao.filtroantispam.logic

/**
 * [digitosNumero] es la longitud del número de **celular** (no fijo) sin el
 * código de país, en formato E.164 (sin el 0 de troncal si el país lo usa).
 * No es una validación E.164 exacta (para eso se usaría libphonenumber) — es
 * una aproximación razonable para un modelo "simple" y para no dejar
 * bloquear/probar números con formato absurdo.
 */
data class Pais(val nombre: String, val codigo: String, val bandera: String, val digitosNumero: Int)

object Paises {
    val LISTA = listOf(
        Pais("Perú", "51", "🇵🇪", 9),
        Pais("Estados Unidos / Canadá", "1", "🇺🇸", 10),
        Pais("México", "52", "🇲🇽", 10),
        // Argentina antepone un "9" al celular en formato internacional
        // (+54 9 11 1234-5678), por eso son 11 y no 10 dígitos.
        Pais("Argentina", "54", "🇦🇷", 11),
        Pais("Brasil", "55", "🇧🇷", 11),
        Pais("Chile", "56", "🇨🇱", 9),
        Pais("Colombia", "57", "🇨🇴", 10),
        Pais("Venezuela", "58", "🇻🇪", 10),
        Pais("España", "34", "🇪🇸", 9),
        Pais("Reino Unido", "44", "🇬🇧", 10),
        Pais("Alemania", "49", "🇩🇪", 11),
        Pais("Francia", "33", "🇫🇷", 9),
        Pais("Italia", "39", "🇮🇹", 10),
        Pais("China", "86", "🇨🇳", 11),
        Pais("India", "91", "🇮🇳", 10),
        Pais("Japón", "81", "🇯🇵", 10),
        Pais("Corea del Sur", "82", "🇰🇷", 10),
        Pais("Australia", "61", "🇦🇺", 9),
        Pais("Sudáfrica", "27", "🇿🇦", 9),
        Pais("Egipto", "20", "🇪🇬", 10),
        Pais("Nigeria", "234", "🇳🇬", 10),
        Pais("Emiratos Árabes Unidos", "971", "🇦🇪", 9),
        Pais("Ucrania", "380", "🇺🇦", 9)
    )

    val PREDETERMINADO = LISTA.first { it.codigo == "51" }

    val CODIGOS = LISTA.map { it.codigo }.sortedByDescending { it.length }
}
