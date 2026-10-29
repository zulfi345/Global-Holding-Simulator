package id.holding.sim.domain.rival

import id.holding.sim.data.Rival

enum class SumberNilai { KAPITALISASI, MULTIPLE_PENDAPATAN }

/** Nilai ekuitas rival dalam USD miliar beserta asal angkanya. */
data class NilaiRival(val nilaiUsdMiliar: Double, val sumber: SumberNilai)

/**
 * Valuasi rival: pakai kapitalisasi bila ada; jika tidak, pendapatan x multiple per arketipe.
 * Semua multiple = parameter desain game (kasar, bukan data pasar). Tak ada data -> null.
 */
object ValuasiRival {
    private val MULTIPLE_PENDAPATAN = mapOf(
        "tambang_energi" to 1.5,
        "pertanian" to 0.6,
        "manufaktur" to 1.2,
        "teknologi" to 5.0,
        "finansial" to 2.5,
        "logistik" to 0.8,
        "properti_konstruksi" to 2.0,
        "ritel_jasa" to 1.5,
        "layanan_regulasi" to 2.0
    )

    /** arketipe harus salah satu dari 9 arketipe; selain itu IllegalArgumentException. */
    fun multiplePendapatan(arketipe: String): Double =
        MULTIPLE_PENDAPATAN[arketipe] ?: throw IllegalArgumentException("arketipe tidak dikenal: $arketipe")

    fun nilai(rival: Rival): NilaiRival? {
        val kap = rival.kapitalisasiUsdMiliar
        if (kap != null) return NilaiRival(kap, SumberNilai.KAPITALISASI)
        val pendapatan = rival.pendapatanUsdMiliar ?: return null
        val multiple = multiplePendapatan(rival.arketipeUtama)
        return NilaiRival(pendapatan * multiple, SumberNilai.MULTIPLE_PENDAPATAN)
    }
}
