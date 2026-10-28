package id.holding.sim.domain.usaha

import id.holding.sim.data.Usaha

/**
 * Strategi teknologi: pendapatan berulang (langganan), churn pelanggan, dan biaya R&D.
 * strukturBiaya memakai persen dari data usaha; fungsi lain dipakai engine yang melacak pelanggan.
 * Tarif R&D bawaan adalah perkiraan; isi per usaha bila ada data. Satuan uang bebas selama konsisten.
 */
class StrategiTeknologi(val rndPct: Double = 15.0) : ArketipeUsaha {
    override val arketipe: Arketipe = Arketipe.TEKNOLOGI
    override val punyaBahanBaku: Boolean = true

    override fun strukturBiaya(usaha: Usaha, pendapatan: Double): StrukturBiaya = StrukturBiaya(
        hpp = pendapatan * usaha.hppPct / 100.0,
        biayaOperasi = pendapatan * usaha.biayaOperasiPct / 100.0,
        penyusutan = pendapatan * usaha.penyusutanPct / 100.0
    )

    /** Pelanggan akhir periode = awal x (1 - churn) + pelanggan baru. Churn dibatasi 0..100%. */
    fun tickPelanggan(pelangganAwal: Double, pelangganBaru: Double, churnPct: Double): Double {
        val churn = churnPct.coerceIn(0.0, 100.0) / 100.0
        return (pelangganAwal.coerceAtLeast(0.0) * (1.0 - churn) + pelangganBaru.coerceAtLeast(0.0))
    }

    /** Pendapatan berulang per periode = pelanggan x ARPU. */
    fun pendapatanBerulang(pelanggan: Double, arpu: Double): Double = pelanggan * arpu

    /** Biaya R&D periode = pendapatan x rndPct. */
    fun biayaRnd(pendapatan: Double): Double = pendapatan * rndPct / 100.0

    /** Nilai umur pelanggan = ARPU x margin / churn; tak terbatas bila churn <= 0. */
    fun nilaiUmurPelanggan(arpu: Double, marginPct: Double, churnPct: Double): Double =
        if (churnPct <= 0.0) Double.POSITIVE_INFINITY else arpu * (marginPct / 100.0) / (churnPct / 100.0)
}
