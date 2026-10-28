package id.holding.sim.domain.usaha

import id.holding.sim.data.Usaha

/**
 * Strategi bank/finansial: tanpa bahan baku. hpp = biaya dana/klaim (persen pendapatan, dari data usaha).
 * Cadangan kerugian kredit TIDAK masuk strukturBiaya karena bergantung pada saldo kredit;
 * pemanggil menambahkan biayaCadangan(...) ke biaya operasi. Semua nilai uang dalam satuan yang sama.
 * Parameter bawaan adalah perkiraan (CAR minimum 8% = acuan Basel; negara tertentu bisa lebih tinggi).
 */
class StrategiFinansial(
    val carMinimumPct: Double = 8.0,
    val coverageNplPct: Double = 100.0
) : ArketipeUsaha {
    override val arketipe: Arketipe = Arketipe.FINANSIAL
    override val punyaBahanBaku: Boolean = false

    override fun strukturBiaya(usaha: Usaha, pendapatan: Double): StrukturBiaya = StrukturBiaya(
        hpp = pendapatan * usaha.hppPct / 100.0,
        biayaOperasi = pendapatan * usaha.biayaOperasiPct / 100.0,
        penyusutan = pendapatan * usaha.penyusutanPct / 100.0
    )

    /** Net interest margin (%) = (pendapatan bunga - biaya dana) / aset produktif. */
    fun nimPct(pendapatanBunga: Double, biayaDana: Double, asetProduktif: Double): Double =
        if (asetProduktif <= 0.0) 0.0 else (pendapatanBunga - biayaDana) / asetProduktif * 100.0

    /** Saldo cadangan yang dibutuhkan = kredit x NPL% x coverage%. */
    fun cadanganDibutuhkan(kreditBeredar: Double, nplPct: Double): Double =
        kreditBeredar * (nplPct / 100.0) * (coverageNplPct / 100.0)

    /** Beban cadangan periode = saldo akhir - saldo awal + kredit dihapusbukukan. Bisa negatif (pemulihan). */
    fun biayaCadangan(cadanganAwal: Double, cadanganAkhir: Double, hapusBuku: Double): Double =
        cadanganAkhir - cadanganAwal + hapusBuku

    /** CAR (%) = modal / aset tertimbang menurut risiko. Tanpa aset berisiko dianggap tak terbatas. */
    fun carPct(modal: Double, atmr: Double): Double =
        if (atmr <= 0.0) Double.POSITIVE_INFINITY else modal / atmr * 100.0

    fun modalMinimum(atmr: Double): Double = atmr * carMinimumPct / 100.0

    fun memenuhiCar(modal: Double, atmr: Double): Boolean = carPct(modal, atmr) >= carMinimumPct
}
