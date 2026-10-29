package id.holding.sim.ui

import id.holding.sim.data.Rival
import id.holding.sim.domain.rival.HasilAkuisisiRival
import java.util.Locale

private val LokalBerita: Locale = Locale.forLanguageTag("id-ID")

/** Satu baris berita: "A membeli B (US$ x miliar)". Nama tak dikenal ditulis "Rival #id". */
internal fun teksAkuisisi(h: HasilAkuisisiRival, rivals: List<Rival>): String {
    val nama = rivals.associate { it.id to it.nama }
    val pembeli = nama[h.pembeli.rivalId] ?: "Rival #" + h.pembeli.rivalId
    val target = nama[h.target.rivalId] ?: "Rival #" + h.target.rivalId
    val harga = String.format(LokalBerita, if (h.hargaUsdMiliar >= 100.0) "%,.0f" else "%,.1f", h.hargaUsdMiliar)
    return pembeli + " membeli " + target + " (US$" + harga + " miliar)"
}
