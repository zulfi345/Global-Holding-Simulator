package id.holding.sim.domain.engine

/** Satu baris jadwal amortisasi. cicilan = bunga + pokokDibayar. */
data class BarisCicilan(
    val bulanKe: Int,
    val cicilan: Double,
    val bunga: Double,
    val pokokDibayar: Double,
    val sisaPinjaman: Double
)

/**
 * Kredit anuitas (cicilan tetap per bulan). Bunga tahunan nominal, dihitung bulanan (bunga/12)
 * atas sisa pinjaman. Suku bunga = acuan + premi risiko (parameter; premi dari rasio keuangan
 * menyusul tahap lain). Tanpa biaya provisi/denda.
 */
object Kredit {
    /** Bunga tahunan pinjaman = suku bunga acuan + premi risiko (persen). */
    fun bungaPinjamanPct(sukuBungaAcuanPct: Double, premiRisikoPct: Double): Double =
        sukuBungaAcuanPct + premiRisikoPct

    /** Cicilan anuitas PMT = P x r / (1 - (1+r)^-n), r = bunga/12. Bunga 0 -> pokok / n. */
    fun cicilanBulanan(pokok: Double, bungaPctTahunan: Double, tenorBulan: Int): Double {
        require(tenorBulan >= 1) { "tenorBulan harus >= 1" }
        require(pokok >= 0.0) { "pokok tidak boleh negatif" }
        val r = bungaPctTahunan / 100.0 / 12.0
        if (r == 0.0) return pokok / tenorBulan
        return pokok * r / (1.0 - Math.pow(1.0 + r, -tenorBulan.toDouble()))
    }

    /** Jadwal amortisasi penuh; sisa pinjaman akhir dibulatkan tepat 0 (hindari residu Double). */
    fun jadwal(pokok: Double, bungaPctTahunan: Double, tenorBulan: Int): List<BarisCicilan> {
        val cicilan = cicilanBulanan(pokok, bungaPctTahunan, tenorBulan)
        val r = bungaPctTahunan / 100.0 / 12.0
        val hasil = ArrayList<BarisCicilan>(tenorBulan)
        var sisa = pokok
        for (k in 1..tenorBulan) {
            val bunga = sisa * r
            val pokokDibayar = cicilan - bunga
            sisa = if (k == tenorBulan) 0.0 else sisa - pokokDibayar
            hasil.add(BarisCicilan(k, cicilan, bunga, pokokDibayar, sisa))
        }
        return hasil
    }

    fun totalBunga(jadwal: List<BarisCicilan>): Double = jadwal.sumOf { it.bunga }
}
