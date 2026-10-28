package id.holding.sim.domain.engine

import kotlin.math.abs

/** Laporan arus kas metode tidak langsung. Dihitung dari dua neraca berurutan. */
data class LaporanArusKas(
    val operasi: Double,
    val investasi: Double,
    val pendanaan: Double,
    val dividen: Double,
    val belanjaModal: Double
) {
    val kasBersih: Double get() = operasi + investasi + pendanaan
}

object ArusKasEngine {
    /**
     * @param labaBersih laba bersih periode (dari LaporanLabaRugi)
     * @param penyusutan beban penyusutan periode (dari LaporanLabaRugi)
     * Dividen = laba bersih - kenaikan laba ditahan. Belanja modal = kenaikan aset tetap + penyusutan.
     */
    fun hitung(awal: Neraca, akhir: Neraca, labaBersih: Double, penyusutan: Double): LaporanArusKas {
        val dividen = labaBersih - (akhir.labaDitahan - awal.labaDitahan)
        val belanjaModal = (akhir.asetTetap - awal.asetTetap) + penyusutan
        val operasi = labaBersih + penyusutan -
            (akhir.piutang - awal.piutang) -
            (akhir.persediaan - awal.persediaan) +
            (akhir.utangUsaha - awal.utangUsaha)
        val investasi = -belanjaModal
        val pendanaan = (akhir.utangBank - awal.utangBank) + (akhir.modal - awal.modal) - dividen
        return LaporanArusKas(operasi, investasi, pendanaan, dividen, belanjaModal)
    }

    /** True jika kas awal + kas bersih = kas akhir. */
    fun cocokDenganKas(awal: Neraca, akhir: Neraca, ak: LaporanArusKas, toleransi: Double = 0.01): Boolean =
        abs(awal.kas + ak.kasBersih - akhir.kas) <= toleransi
}
