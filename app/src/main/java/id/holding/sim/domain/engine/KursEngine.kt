package id.holding.sim.domain.engine

import kotlin.random.Random

/**
 * Kurs bergerak bulanan: separuh selisih inflasi (PPP parsial), dikurangi efek selisih
 * suku bunga riil, ditambah guncangan risk-off dan noise. Kurs = unit mata uang lokal per 1 USD.
 * Negara bermata uang terpatok ke USD tidak berubah. Negara euro bergerak sama (acuan DE);
 * beberapa mata uang terkait paritas tetap ke euro/SGD. Gunakan tickSemua untuk daftar negara.
 */
object KursEngine {
    val TERPATOK: Set<String> = setOf("US", "SA", "AE", "QA", "KW", "OM", "BH", "JO", "HK", "EC", "PA", "SV")

    /** Negara berbagi euro; kurs sama dengan acuan. */
    val ZONA_EURO: Set<String> = setOf(
        "DE", "FR", "NL", "IT", "ES", "BE", "AT", "IE", "FI", "PT", "GR", "SK", "LU", "BG", "LT", "SI", "LV", "EE"
    )
    const val ACUAN_EURO = "DE"

    /** negara -> (negara acuan, paritas: unit mata uang negara per 1 unit mata uang acuan). */
    val TERKAIT: Map<String, Pair<String, Double>> = mapOf(
        "DK" to Pair("DE", 7.46038),
        "CI" to Pair("DE", 655.957),
        "SN" to Pair("DE", 655.957),
        "CM" to Pair("DE", 655.957),
        "BN" to Pair("SG", 1.0)
    )

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

    /**
     * Tick bulanan untuk semua negara sekaligus. Daftar harus memuat "US".
     * Negara euro mengikuti kurs DE; negara terkait mengikuti acuannya sesuai paritas tetap.
     * Urutan hasil sama dengan urutan masukan.
     */
    fun tickSemua(
        daftar: List<MakroState>,
        acak: Random,
        riskOffPct: Double = 0.0,
        noiseSkala: Double = 1.0
    ): List<MakroState> {
        val amerika = daftar.firstOrNull { it.kode == "US" } ?: return daftar
        val hasil = HashMap<String, MakroState>()
        for (s in daftar) {
            if (s.kode in ZONA_EURO && s.kode != ACUAN_EURO) continue
            if (s.kode in TERKAIT) continue
            hasil[s.kode] = tickBulanan(s, amerika, acak, riskOffPct, noiseSkala)
        }
        val kursEuro = hasil[ACUAN_EURO]?.kursPerUsd
        for (s in daftar) {
            if (s.kode in ZONA_EURO && s.kode != ACUAN_EURO) {
                hasil[s.kode] = if (kursEuro != null) s.copy(kursPerUsd = kursEuro) else s
            }
        }
        for (s in daftar) {
            val ikatan = TERKAIT[s.kode] ?: continue
            val kursAcuan = hasil[ikatan.first]?.kursPerUsd
            hasil[s.kode] = if (kursAcuan != null) s.copy(kursPerUsd = kursAcuan * ikatan.second) else s
        }
        return daftar.map { hasil.getValue(it.kode) }
    }
}
