package id.holding.sim.domain.rival

import id.holding.sim.data.Rival
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class DuniaRivalTest {
    private fun rival(id: Int, margin: Double?, kap: Double?, utang: Double? = null) = Rival(
        id = id, nama = "R$id", sektor = "s", negara = "US", status = "publik",
        arketipeUtama = "manufaktur", tanggalData = "2026-10", perkiraan = true,
        kapitalisasiUsdMiliar = kap, pendapatanUsdMiliar = null,
        marginBersihPct = margin, utangUsdMiliar = utang, sumberKapitalisasi = "", catatanAngka = ""
    )

    private val tiga = listOf(rival(1, 30.0, 1000.0, 10.0), rival(2, 8.0, 50.0), rival(3, 8.0, 40.0))

    @Test fun bulanNolSamaDenganAwal() {
        assertEquals(DaftarRival.awalSemua(tiga), DuniaRival.stateSetelah(tiga, 0))
    }

    @Test fun satuBulanSamaDenganSatuTick() {
        val langsung = DaftarRival.tickSemua(DaftarRival.awalSemua(tiga), tiga, DuniaRival.SEED_DUNIA, 1).states
        assertEquals(langsung, DuniaRival.stateSetelah(tiga, 1))
    }

    @Test fun deterministikBulanSama() {
        assertEquals(DuniaRival.stateSetelah(tiga, 24), DuniaRival.stateSetelah(tiga, 24))
    }

    @Test fun akuisisiBulanSatuTerjadiDanBulanNolKosong() {
        assertTrue(DuniaRival.akuisisiBulan(tiga, 0).isEmpty())
        assertEquals(1, DuniaRival.akuisisiBulan(tiga, 1).size)
    }

    @Test fun bulanNegatifDitolak() {
        try {
            DuniaRival.stateSetelah(tiga, -1)
            assertTrue(false)
        } catch (e: IllegalArgumentException) {
            assertTrue(true)
        }
    }

    @Test fun rivalTakBernilaiTidakMasukState() {
        val s = DuniaRival.stateSetelah(listOf(rival(1, 10.0, 100.0), rival(2, null, null)), 3)
        assertEquals(listOf(1), s.map { it.rivalId })
    }
}
