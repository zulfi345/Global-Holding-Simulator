package id.holding.sim.domain.engine

/** Syarat keuangan yang disepakati dengan kreditur (nilai bawaan = desain game). */
data class Covenant(
    val derMaks: Double = 3.0,
    val coverageMin: Double = 2.0,
    val currentRatioMin: Double = 1.0
)

enum class Pelanggaran { DER, COVERAGE, CURRENT_RATIO }

enum class AkibatCovenant { AMAN, BUNGA_NAIK, WAJIB_LUNAS }

/**
 * Pemeriksaan covenant pinjaman: rasio dari PremiRisiko (der/interestCoverage/currentRatio).
 * Batas dianggap aman bila tepat sama (DER <= maks, coverage >= min, CR >= min).
 * Akibat: 0 pelanggaran = AMAN, 1 = BUNGA_NAIK, >= 2 = WAJIB_LUNAS (dipercepat). Parameter desain game.
 */
object CovenantEngine {
    fun periksa(der: Double, coverage: Double, currentRatio: Double, c: Covenant = Covenant()): List<Pelanggaran> {
        val hasil = ArrayList<Pelanggaran>(3)
        if (der > c.derMaks) hasil.add(Pelanggaran.DER)
        if (coverage < c.coverageMin) hasil.add(Pelanggaran.COVERAGE)
        if (currentRatio < c.currentRatioMin) hasil.add(Pelanggaran.CURRENT_RATIO)
        return hasil
    }

    fun akibat(jumlahPelanggaran: Int): AkibatCovenant {
        require(jumlahPelanggaran >= 0) { "jumlahPelanggaran tidak boleh negatif" }
        return when {
            jumlahPelanggaran == 0 -> AkibatCovenant.AMAN
            jumlahPelanggaran == 1 -> AkibatCovenant.BUNGA_NAIK
            else -> AkibatCovenant.WAJIB_LUNAS
        }
    }

    /** Bunga setelah pemeriksaan: naik kenaikanPct hanya bila akibat = BUNGA_NAIK; lainnya tidak berubah. */
    fun bungaSetelahPemeriksaan(bungaPct: Double, akibat: AkibatCovenant, kenaikanPct: Double = 1.0): Double =
        if (akibat == AkibatCovenant.BUNGA_NAIK) bungaPct + kenaikanPct else bungaPct
}
