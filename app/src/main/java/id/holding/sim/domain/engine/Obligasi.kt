package id.holding.sim.domain.engine

/**
 * Perhitungan dasar obligasi: kupon dan current yield (yield sederhana = kupon tahunan /
 * harga pasar kini). Yield-to-maturity & durasi (Macaulay/modified) belum dimodelkan
 * (perlu jadwal arus kas penuh + sisa jatuh tempo); menyusul tahap berikutnya.
 */
object Obligasi {
    /** nilaiPar = nilai nominal/pokok obligasi. kuponPct = kupon tahunan dalam persen dari nilaiPar. */
    fun kuponTahunan(nilaiPar: Double, kuponPct: Double): Double =
        nilaiPar * kuponPct / 100.0

    /** periodePerTahun: 1=tahunan, 2=semesteran, 4=kuartalan, dst. Harus >= 1. */
    fun kuponPerPeriode(nilaiPar: Double, kuponPct: Double, periodePerTahun: Int): Double =
        kuponTahunan(nilaiPar, kuponPct) / periodePerTahun

    /** Current yield: kupon tahunan dibagi harga pasar kini (bukan nilai par) x 100. hargaPasar harus > 0. */
    fun currentYieldPct(kuponTahunan: Double, hargaPasar: Double): Double =
        kuponTahunan / hargaPasar * 100.0
}
