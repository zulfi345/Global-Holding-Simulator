package id.holding.sim.ui

import id.holding.sim.data.Rival
import id.holding.sim.domain.rival.HasilAkuisisiRival
import id.holding.sim.domain.rival.StateRival
import org.junit.Assert.assertEquals
import org.junit.Test

class BeritaRivalTest {
    private fun rival(id: Int, nama: String) = Rival(
        id = id, nama = nama, sektor = "s", negara = "US", status = "publik",
        arketipeUtama = "manufaktur", tanggalData = "2026-10", perkiraan = true,
        kapitalisasiUsdMiliar = 10.0, pendapatanUsdMiliar = null,
        marginBersihPct = null, utangUsdMiliar = null, sumberKapitalisasi = "", catatanAngka = ""
    )

    private fun hasil(harga: Double) =
        HasilAkuisisiRival(StateRival(1, 500.0), StateRival(2, 0.0, aktif = false), harga)

    @Test fun teksMemakaiNamaDanHargaSatuDesimal() {
        val t = teksAkuisisi(hasil(12.34), listOf(rival(1, "Alfa"), rival(2, "Beta")))
        assertEquals("Alfa membeli Beta (US$12,3 miliar)", t)
    }

    @Test fun hargaBesarTanpaDesimal() {
        val t = teksAkuisisi(hasil(250.0), listOf(rival(1, "Alfa"), rival(2, "Beta")))
        assertEquals("Alfa membeli Beta (US$250 miliar)", t)
    }

    @Test fun namaTakDikenalPakaiId() {
        val t = teksAkuisisi(hasil(5.0), emptyList())
        assertEquals("Rival #1 membeli Rival #2 (US$5,0 miliar)", t)
    }
}
