package id.holding.sim.domain.holding

/**
 * Jual usaha milik holding. SEMENTARA (desain game): hasil bersih = nilai usaha x (1 - biaya jual 2%);
 * kas += hasil bersih, usaha dihapus dari daftar. Usaha tak dimiliki -> null.
 */
object JualUsaha {
    const val BIAYA_JUAL_PCT = 2.0

    fun hasilBersihRupiah(posisi: PosisiUsaha, biayaPct: Double = BIAYA_JUAL_PCT): Double {
        require(biayaPct in 0.0..100.0) { "biaya jual harus 0..100 persen" }
        return posisi.nilaiRupiah * (1.0 - biayaPct / 100.0)
    }

    fun jual(holding: Holding, usahaId: Int, biayaPct: Double = BIAYA_JUAL_PCT): Holding? {
        val posisi = holding.usaha.firstOrNull { it.usahaId == usahaId } ?: return null
        return holding.copy(
            kasRupiah = holding.kasRupiah + hasilBersihRupiah(posisi, biayaPct),
            usaha = holding.usaha.filter { it.usahaId != usahaId }
        )
    }
}
