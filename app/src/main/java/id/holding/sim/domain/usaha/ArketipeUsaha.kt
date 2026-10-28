package id.holding.sim.domain.usaha

import id.holding.sim.data.Usaha

enum class Arketipe {
    TAMBANG_ENERGI, PERTANIAN, MANUFAKTUR, TEKNOLOGI, FINANSIAL,
    LOGISTIK, PROPERTI_KONSTRUKSI, RITEL_JASA, LAYANAN_REGULASI;

    companion object {
        /** Mengubah teks CSV (mis. "tambang_energi") menjadi enum; error jelas bila tidak dikenal. */
        fun dari(teks: String): Arketipe = valueOf(teks.trim().uppercase())
    }
}

/** Biaya satu periode dalam satuan uang yang sama dengan pendapatan. */
data class StrukturBiaya(val hpp: Double, val biayaOperasi: Double, val penyusutan: Double)

interface ArketipeUsaha {
    val arketipe: Arketipe

    /** False untuk finansial: hpp berarti biaya dana/klaim, bukan bahan baku. */
    val punyaBahanBaku: Boolean

    fun strukturBiaya(usaha: Usaha, pendapatan: Double): StrukturBiaya
}

/** Strategi dasar: biaya = persentase pendapatan dari data usaha. Arketipe khusus akan menimpa. */
class StrategiPersen(
    override val arketipe: Arketipe,
    override val punyaBahanBaku: Boolean = true
) : ArketipeUsaha {
    override fun strukturBiaya(usaha: Usaha, pendapatan: Double): StrukturBiaya = StrukturBiaya(
        hpp = pendapatan * usaha.hppPct / 100.0,
        biayaOperasi = pendapatan * usaha.biayaOperasiPct / 100.0,
        penyusutan = pendapatan * usaha.penyusutanPct / 100.0
    )
}

object ArketipeRegistry {
    fun untuk(usaha: Usaha): ArketipeUsaha {
        val a = Arketipe.dari(usaha.arketipe)
        return when (a) {
            Arketipe.FINANSIAL -> StrategiFinansial()
            Arketipe.TAMBANG_ENERGI -> StrategiTambangEnergi()
            Arketipe.PERTANIAN -> StrategiPertanian()
            Arketipe.MANUFAKTUR -> StrategiManufaktur()
            Arketipe.TEKNOLOGI -> StrategiTeknologi()
            Arketipe.LOGISTIK -> StrategiLogistik()
            Arketipe.PROPERTI_KONSTRUKSI -> StrategiPropertiKonstruksi()
            Arketipe.RITEL_JASA -> StrategiRitelJasa()
            Arketipe.LAYANAN_REGULASI -> StrategiLayananRegulasi()
            else -> StrategiPersen(a, punyaBahanBaku = true)
        }
    }
}
