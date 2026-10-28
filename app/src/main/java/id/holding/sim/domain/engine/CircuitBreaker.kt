package id.holding.sim.domain.engine

/**
 * Auto Reject Atas/Bawah (ARA/ARB) generik untuk saham: membatasi harga baru ke rentang
 * persen tertentu dari harga kemarin. Persentase flat (bukan tiered seperti aturan asli
 * bursa per rentang harga) — penyederhanaan desain game.
 */
object CircuitBreaker {
    const val DEFAULT_ARA_PCT = 25.0
    const val DEFAULT_ARB_PCT = 25.0

    /** hargaKemarin harus > 0. batasAtasPct/batasBawahPct dalam persen, >= 0. */
    fun batasi(
        hargaBaru: Double,
        hargaKemarin: Double,
        batasAtasPct: Double = DEFAULT_ARA_PCT,
        batasBawahPct: Double = DEFAULT_ARB_PCT
    ): Double {
        val batasAtas = hargaKemarin * (1.0 + batasAtasPct / 100.0)
        val batasBawah = hargaKemarin * (1.0 - batasBawahPct / 100.0)
        return hargaBaru.coerceIn(batasBawah, batasAtas)
    }

    /** true jika hargaBaru (sebelum dibatasi) menabrak batas atas (ARA) atau bawah (ARB). */
    fun kenaAra(hargaBaru: Double, hargaKemarin: Double, batasAtasPct: Double = DEFAULT_ARA_PCT): Boolean =
        hargaBaru >= hargaKemarin * (1.0 + batasAtasPct / 100.0)

    fun kenaArb(hargaBaru: Double, hargaKemarin: Double, batasBawahPct: Double = DEFAULT_ARB_PCT): Boolean =
        hargaBaru <= hargaKemarin * (1.0 - batasBawahPct / 100.0)
}
