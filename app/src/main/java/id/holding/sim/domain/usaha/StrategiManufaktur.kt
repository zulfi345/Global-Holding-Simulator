package id.holding.sim.domain.usaha

import id.holding.sim.data.Usaha

/**
 * Strategi manufaktur: kapasitas pabrik, utilisasi, dan pemisahan biaya tetap vs variabel.
 * strukturBiaya tetap memakai persen dari data usaha; fungsi lain dipakai engine yang
 * menghitung produksi per unit. Satuan uang bebas selama konsisten.
 */
class StrategiManufaktur : ArketipeUsaha {
    override val arketipe: Arketipe = Arketipe.MANUFAKTUR
    override val punyaBahanBaku: Boolean = true

    override fun strukturBiaya(usaha: Usaha, pendapatan: Double): StrukturBiaya = StrukturBiaya(
        hpp = pendapatan * usaha.hppPct / 100.0,
        biayaOperasi = pendapatan * usaha.biayaOperasiPct / 100.0,
        penyusutan = pendapatan * usaha.penyusutanPct / 100.0
    )

    /** Produksi aktual = min(permintaan, kapasitas). */
    fun produksi(kapasitasUnit: Double, permintaanUnit: Double): Double =
        minOf(kapasitasUnit.coerceAtLeast(0.0), permintaanUnit.coerceAtLeast(0.0))

    /** Utilisasi (%) = produksi / kapasitas; 0 bila kapasitas nol. */
    fun utilisasiPct(produksiUnit: Double, kapasitasUnit: Double): Double =
        if (kapasitasUnit <= 0.0) 0.0 else produksiUnit / kapasitasUnit * 100.0

    /** Biaya per unit = biaya variabel per unit + biaya tetap / produksi; 0 bila tanpa produksi. */
    fun biayaPerUnit(biayaTetap: Double, biayaVariabelPerUnit: Double, produksiUnit: Double): Double =
        if (produksiUnit <= 0.0) 0.0 else biayaVariabelPerUnit + biayaTetap / produksiUnit

    /** Titik impas (unit) = biaya tetap / margin per unit; tak terbatas bila margin <= 0. */
    fun titikImpasUnit(biayaTetap: Double, hargaPerUnit: Double, biayaVariabelPerUnit: Double): Double {
        val margin = hargaPerUnit - biayaVariabelPerUnit
        return if (margin <= 0.0) Double.POSITIVE_INFINITY else biayaTetap / margin
    }
}
