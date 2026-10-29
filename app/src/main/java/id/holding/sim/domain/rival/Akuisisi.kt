package id.holding.sim.domain.rival

import id.holding.sim.data.Rival

/** Hasil pemain mencoba membeli rival. Semua angka USD miliar. */
data class HasilAkuisisi(
    val diterima: Boolean,
    val hargaUsdMiliar: Double,
    val biayaUsdMiliar: Double,
    val alasan: String
)

/** Tawaran rival untuk membeli usaha pemain. Pemain bebas menerima/menolak (urusan pemanggil). */
data class TawaranRival(val rivalId: Int, val hargaUsdMiliar: Double)

/**
 * Akuisisi 2 arah. Semua parameter = desain game. Satuan USD miliar; pemanggil mengonversi
 * nilai usaha pemain (Rupiah/USD biasa) ke USD miliar.
 */
object Akuisisi {
    const val PREMI_DEFAULT_PCT = 30.0
    const val PREMI_TERTUTUP_TAMBAHAN_PCT = 10.0
    const val BIAYA_TRANSAKSI_PCT = 1.0
    const val PREMI_TAWARAN_RIVAL_PCT = 20.0
    const val KAPASITAS_BELI_RIVAL_PCT = 10.0

    /** nilai rival x (1 + premi%); rival tertutup + PREMI_TERTUTUP_TAMBAHAN_PCT. Null bila nilai tak diketahui. */
    fun hargaBeliRival(rival: Rival, premiPct: Double = PREMI_DEFAULT_PCT): Double? {
        require(premiPct >= 0.0) { "premi tidak boleh negatif" }
        val nilai = ValuasiRival.nilai(rival)?.nilaiUsdMiliar ?: return null
        val tambahan = if (rival.tertutup) PREMI_TERTUTUP_TAMBAHAN_PCT else 0.0
        return nilai * (1.0 + (premiPct + tambahan) / 100.0)
    }

    /** Pemain membeli rival: butuh harga + biaya transaksi <= dana. */
    fun pemainBeliRival(
        rival: Rival,
        danaUsdMiliar: Double,
        premiPct: Double = PREMI_DEFAULT_PCT
    ): HasilAkuisisi {
        val harga = hargaBeliRival(rival, premiPct)
            ?: return HasilAkuisisi(false, 0.0, 0.0, "nilai rival tidak diketahui")
        val biaya = harga * BIAYA_TRANSAKSI_PCT / 100.0
        if (danaUsdMiliar < harga + biaya) return HasilAkuisisi(false, harga, biaya, "dana kurang")
        return HasilAkuisisi(true, harga, biaya, "")
    }

    /**
     * Rival menawar usaha pemain bila keputusannya EKSPANSI dan harga tawaran (nilai x (1+20%))
     * tidak melebihi 10% nilai rival. Selain itu null. nilaiUsahaUsdMiliar harus > 0.
     */
    fun tawaranRival(rival: Rival, seed: Long, nilaiUsahaUsdMiliar: Double): TawaranRival? {
        require(nilaiUsahaUsdMiliar > 0.0) { "nilai usaha harus > 0" }
        if (AiRival.putuskan(rival, seed) != KeputusanRival.EKSPANSI) return null
        val nilaiRival = ValuasiRival.nilai(rival)?.nilaiUsdMiliar ?: return null
        val harga = nilaiUsahaUsdMiliar * (1.0 + PREMI_TAWARAN_RIVAL_PCT / 100.0)
        if (harga > nilaiRival * KAPASITAS_BELI_RIVAL_PCT / 100.0) return null
        return TawaranRival(rival.id, harga)
    }
}
