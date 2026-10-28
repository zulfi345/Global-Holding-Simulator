package id.holding.sim.domain.engine

/** Peringkat kredit sederhana (desain game, bukan peringkat lembaga resmi). */
enum class PeringkatKredit { A, B, C, D }

/**
 * Premi risiko kredit dari tiga rasio keuangan. Tiap rasio memberi poin 0..3 (0 = sehat),
 * total poin 0..9, premi = totalPoin x PREMI_PER_POIN (persen). Ambang adalah parameter desain
 * game, bukan standar bank. Hasil premi ditambahkan ke suku bunga acuan (lihat Kredit.bungaPinjamanPct).
 */
object PremiRisiko {
    const val PREMI_PER_POIN = 0.75

    /** DER = total utang / ekuitas; ekuitas <= 0 -> tak hingga (paling berisiko). */
    fun der(totalUtang: Double, ekuitas: Double): Double =
        if (ekuitas <= 0.0) Double.POSITIVE_INFINITY else totalUtang / ekuitas

    /** Interest coverage = EBIT / beban bunga; tanpa beban bunga -> tak hingga (aman). */
    fun interestCoverage(ebit: Double, bebanBunga: Double): Double =
        if (bebanBunga <= 0.0) Double.POSITIVE_INFINITY else ebit / bebanBunga

    /** Current ratio = aset lancar / liabilitas lancar; liabilitas 0 -> tak hingga (aman). */
    fun currentRatio(asetLancar: Double, liabilitasLancar: Double): Double =
        if (liabilitasLancar <= 0.0) Double.POSITIVE_INFINITY else asetLancar / liabilitasLancar

    /** DER <= 1 -> 0; <= 2 -> 1; <= 3 -> 2; lainnya 3. */
    fun poinDer(der: Double): Int = when {
        der <= 1.0 -> 0
        der <= 2.0 -> 1
        der <= 3.0 -> 2
        else -> 3
    }

    /** ICR >= 5 -> 0; >= 3 -> 1; >= 1.5 -> 2; lainnya 3. */
    fun poinCoverage(icr: Double): Int = when {
        icr >= 5.0 -> 0
        icr >= 3.0 -> 1
        icr >= 1.5 -> 2
        else -> 3
    }

    /** CR >= 1.5 -> 0; >= 1.0 -> 1; >= 0.8 -> 2; lainnya 3. */
    fun poinCurrentRatio(cr: Double): Int = when {
        cr >= 1.5 -> 0
        cr >= 1.0 -> 1
        cr >= 0.8 -> 2
        else -> 3
    }

    fun totalPoin(der: Double, icr: Double, cr: Double): Int =
        poinDer(der) + poinCoverage(icr) + poinCurrentRatio(cr)

    /** Premi risiko dalam persen (0..6.75). */
    fun premiPct(der: Double, icr: Double, cr: Double): Double =
        totalPoin(der, icr, cr) * PREMI_PER_POIN

    /** 0-1 poin -> A; 2-3 -> B; 4-6 -> C; 7-9 -> D. */
    fun peringkat(totalPoin: Int): PeringkatKredit = when {
        totalPoin <= 1 -> PeringkatKredit.A
        totalPoin <= 3 -> PeringkatKredit.B
        totalPoin <= 6 -> PeringkatKredit.C
        else -> PeringkatKredit.D
    }
}
