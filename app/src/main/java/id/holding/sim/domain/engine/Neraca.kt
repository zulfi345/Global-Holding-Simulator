package id.holding.sim.domain.engine

import kotlin.math.abs

/** Neraca satu perusahaan pada satu tanggal. Semua nilai dalam satuan uang yang sama. */
data class Neraca(
    val kas: Double,
    val piutang: Double,
    val persediaan: Double,
    val asetTetap: Double, // nilai buku bersih (setelah penyusutan)
    val utangUsaha: Double,
    val utangBank: Double,
    val modal: Double,
    val labaDitahan: Double
) {
    val totalAset: Double get() = kas + piutang + persediaan + asetTetap
    val totalLiabilitas: Double get() = utangUsaha + utangBank
    val totalEkuitas: Double get() = modal + labaDitahan
    /** Aset - (liabilitas + ekuitas). Seharusnya 0. */
    val selisih: Double get() = totalAset - (totalLiabilitas + totalEkuitas)

    fun seimbang(toleransi: Double = 0.01): Boolean = abs(selisih) <= toleransi
}

object NeracaEngine {
    fun cekSeimbang(n: Neraca, toleransi: Double = 0.01): Boolean = n.seimbang(toleransi)
}
