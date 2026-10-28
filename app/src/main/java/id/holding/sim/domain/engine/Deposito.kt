package id.holding.sim.domain.engine

/** Hasil pencairan deposito: bunga bersih setelah pajak dan penalti, serta total diterima. */
data class HasilDeposito(
    val pokok: Double,
    val bungaBruto: Double,
    val pajakBunga: Double,
    val penalti: Double,
    val totalDiterima: Double
)

/**
 * Deposito berjangka, bunga sederhana (tidak berbunga-atas-bunga). Pajak bunga dan penalti
 * adalah parameter desain game (persen dari bunga bruto), bukan tarif negara tertentu.
 */
object Deposito {
    /** Bunga bruto = pokok x bungaPct/100 x bulan/12. bulan >= 0. */
    fun bungaBruto(pokok: Double, bungaPctTahunan: Double, bulan: Int): Double {
        require(bulan >= 0) { "bulan tidak boleh negatif" }
        return pokok * bungaPctTahunan / 100.0 * bulan / 12.0
    }

    /** Pencairan saat jatuh tempo: pokok + bunga bruto - pajak (pajakBungaPct dari bunga bruto). */
    fun jatuhTempo(pokok: Double, bungaPctTahunan: Double, tenorBulan: Int, pajakBungaPct: Double): HasilDeposito =
        hitung(pokok, bungaPctTahunan, tenorBulan, pajakBungaPct, 0.0)

    /**
     * Pencairan dini: bunga hanya untuk bulanBerjalan, dikurangi penalti (penaltiPct dari bunga bruto).
     * bulanBerjalan harus 0..tenorBulan; pokok selalu kembali utuh.
     */
    fun pencairanDini(
        pokok: Double, bungaPctTahunan: Double, bulanBerjalan: Int, tenorBulan: Int,
        pajakBungaPct: Double, penaltiPct: Double
    ): HasilDeposito {
        require(bulanBerjalan in 0..tenorBulan) { "bulanBerjalan harus di antara 0 dan tenorBulan" }
        return hitung(pokok, bungaPctTahunan, bulanBerjalan, pajakBungaPct, penaltiPct)
    }

    private fun hitung(pokok: Double, bungaPct: Double, bulan: Int, pajakPct: Double, penaltiPct: Double): HasilDeposito {
        val bruto = bungaBruto(pokok, bungaPct, bulan)
        val pajak = bruto * pajakPct / 100.0
        val penalti = bruto * penaltiPct / 100.0
        return HasilDeposito(pokok, bruto, pajak, penalti, pokok + bruto - pajak - penalti)
    }
}
