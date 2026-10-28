package id.holding.sim.domain.engine

import id.holding.sim.data.Negara

/** Kondisi makro satu negara pada satu waktu. Nilai persen berupa angka tahunan. */
data class MakroState(
    val kode: String,
    val pertumbuhanPct: Double,
    val inflasiPct: Double,
    val bungaPct: Double,
    val kursPerUsd: Double,
    val pdbUsdMiliar: Double
) {
    companion object {
        fun dari(n: Negara): MakroState = MakroState(
            kode = n.kode,
            pertumbuhanPct = n.pertumbuhanPct,
            inflasiPct = n.inflasiPct,
            bungaPct = n.bungaAcuanPct,
            kursPerUsd = n.kursPerUsd,
            pdbUsdMiliar = n.pdbUsdMiliar
        )
    }
}
