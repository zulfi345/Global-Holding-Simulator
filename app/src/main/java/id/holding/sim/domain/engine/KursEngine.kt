package id.holding.sim.domain.engine

import kotlin.random.Random

/**
 * Kurs bergerak bulanan: separuh selisih inflasi (PPP parsial), dikurangi efek selisih
 * suku bunga riil, ditambah guncangan risk-off dan noise. Kurs = unit mata uang lokal per 1 USD.
 * Negara bermata uang terpatok (USD, riyal Saudi, dirham UEA) tidak berubah.
 */
object KursEngine {
    val TERPATOK: Set<String> = setOf("US", "SA", "AE")

    /** @param riskOffPct kenaikan kurs per USD (poin persen, melemahkan mata uang lokal) */
    fun tickBulanan(
        lokal: MakroState,
        amerika: MakroState,
        acak: Random,
        riskOffPct: Double = 0.0,
        noiseSkala: Double = 1.0
    ): MakroState {
        if (lokal.kode in TERPATOK) return lokal

        val selisihInflasi = lokal.inflasiPct - amerika.inflasiPct
        val selisihRiil = (lokal.bungaPct - lokal.inflasiPct) - (amerika.bungaPct - amerika.inflasiPct)
        val noise = (acak.nextDouble() * 2.0 - 1.0) * noiseSkala

        val perubahanPct = (0.5 * selisihInflasi / 12.0 -
            0.1 * selisihRiil / 12.0 +
            riskOffPct +
            0.8 * noise).coerceIn(-10.0, 10.0)

        return lokal.copy(kursPerUsd = lokal.kursPerUsd * (1.0 + perubahanPct / 100.0))
    }
}
