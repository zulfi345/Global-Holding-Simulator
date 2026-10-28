package id.holding.sim.domain.usaha

import id.holding.sim.data.Usaha

/**
 * Strategi properti & konstruksi: okupansi dan sewa (properti), progres dan pengakuan pendapatan
 * proyek metode persentase penyelesaian (konstruksi). strukturBiaya memakai persen dari data usaha.
 * Satuan uang bebas selama konsisten.
 */
class StrategiPropertiKonstruksi : ArketipeUsaha {
    override val arketipe: Arketipe = Arketipe.PROPERTI_KONSTRUKSI
    override val punyaBahanBaku: Boolean = true

    override fun strukturBiaya(usaha: Usaha, pendapatan: Double): StrukturBiaya = StrukturBiaya(
        hpp = pendapatan * usaha.hppPct / 100.0,
        biayaOperasi = pendapatan * usaha.biayaOperasiPct / 100.0,
        penyusutan = pendapatan * usaha.penyusutanPct / 100.0
    )

    /** Okupansi (%) = unit terisi / total unit; 0 bila total nol; dibatasi 0..100. */
    fun okupansiPct(terisi: Double, total: Double): Double =
        if (total <= 0.0) 0.0 else (terisi / total * 100.0).coerceIn(0.0, 100.0)

    /** Pendapatan sewa periode = unit x okupansi x sewa per unit (okupansi dibatasi 0..100%). */
    fun pendapatanSewa(totalUnit: Double, okupansiPct: Double, sewaPerUnit: Double): Double =
        totalUnit * okupansiPct.coerceIn(0.0, 100.0) / 100.0 * sewaPerUnit

    /** Progres proyek (%) = biaya terpakai / anggaran total; 0 bila anggaran nol; dibatasi 0..100. */
    fun progresProyekPct(biayaTerpakai: Double, anggaranTotal: Double): Double =
        if (anggaranTotal <= 0.0) 0.0 else (biayaTerpakai / anggaranTotal * 100.0).coerceIn(0.0, 100.0)

    /** Pendapatan periode (persentase penyelesaian) = nilai kontrak x kenaikan progres; 0 bila progres mundur. */
    fun pendapatanProyek(nilaiKontrak: Double, progresAwalPct: Double, progresAkhirPct: Double): Double =
        nilaiKontrak * (progresAkhirPct - progresAwalPct).coerceAtLeast(0.0) / 100.0
}
