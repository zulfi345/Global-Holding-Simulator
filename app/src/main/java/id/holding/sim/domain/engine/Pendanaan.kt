package id.holding.sim.domain.engine

enum class JenisUtang { PINJAMAN_BANK, OBLIGASI, VALAS }

/**
 * Satu kewajiban utang perusahaan. sisaPokok dalam rupiah (utang valas sudah dikonversi pemanggil
 * ke rupiah pada kurs kini, lihat KreditValas). sisaTenorBulan <= 0 berarti sudah jatuh tempo/lunas.
 */
data class UtangPerusahaan(
    val nama: String,
    val jenis: JenisUtang,
    val sisaPokok: Double,
    val bungaPctTahunan: Double,
    val sisaTenorBulan: Int
)

/**
 * Ringkasan struktur utang untuk Neraca/LabaRugi: total utang -> Neraca.utangBank,
 * bebanBungaBulanan x 12 -> LaporanLabaRugi.biayaBunga tahunan. Fungsi murni.
 */
object Pendanaan {
    fun totalUtang(daftar: List<UtangPerusahaan>): Double = daftar.sumOf { it.sisaPokok }

    /** Beban bunga satu bulan = sum(sisaPokok x bunga% / 100 / 12). */
    fun bebanBungaBulanan(daftar: List<UtangPerusahaan>): Double =
        daftar.sumOf { it.sisaPokok * it.bungaPctTahunan / 100.0 / 12.0 }

    /** Bunga rata-rata tertimbang pokok (persen tahunan); tanpa utang -> 0. */
    fun bungaRataRataPct(daftar: List<UtangPerusahaan>): Double {
        val total = totalUtang(daftar)
        if (total <= 0.0) return 0.0
        return daftar.sumOf { it.sisaPokok * it.bungaPctTahunan } / total
    }

    /** Sisa tenor terpendek di antara utang berpokok > 0 dan tenor > 0; null bila tidak ada. */
    fun jatuhTempoTerdekatBulan(daftar: List<UtangPerusahaan>): Int? =
        daftar.filter { it.sisaPokok > 0.0 && it.sisaTenorBulan > 0 }.minOfOrNull { it.sisaTenorBulan }

    /** Pokok yang jatuh tempo dalam `bulan` bulan ke depan (sisaTenor di antara 1..bulan). */
    fun jatuhTempoDalam(daftar: List<UtangPerusahaan>, bulan: Int): Double =
        daftar.filter { it.sisaTenorBulan in 1..bulan }.sumOf { it.sisaPokok }
}
