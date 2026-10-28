package id.holding.sim.domain.usaha

import id.holding.sim.data.Usaha
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ArketipeUsahaTest {
    private fun usaha(arketipe: String, hpp: Double, opex: Double, peny: Double) = Usaha(
        id = 1, nama = "Uji", kelompok = "X", arketipe = arketipe, modalMinUsd = 1000.0,
        hppPct = hpp, biayaOperasiPct = opex, penyusutanPct = peny, pekerjaMin = 1,
        izin = emptyList(), risiko = emptyList(), kpi = emptyList(),
        tanggalData = "2026-10", perkiraan = true
    )

    @Test fun enumDariTeksCsv() {
        assertEquals(Arketipe.TAMBANG_ENERGI, Arketipe.dari("tambang_energi"))
        assertEquals(Arketipe.LAYANAN_REGULASI, Arketipe.dari("layanan_regulasi"))
    }

    @Test fun biayaPersenDariPendapatan() {
        val s = ArketipeRegistry.untuk(usaha("manufaktur", 45.0, 30.0, 2.0)).strukturBiaya(usaha("manufaktur", 45.0, 30.0, 2.0), 1000.0)
        assertEquals(450.0, s.hpp, 1e-9)
        assertEquals(300.0, s.biayaOperasi, 1e-9)
        assertEquals(20.0, s.penyusutan, 1e-9)
    }

    @Test fun finansialTanpaBahanBaku() {
        assertFalse(ArketipeRegistry.untuk(usaha("finansial", 45.0, 30.0, 2.0)).punyaBahanBaku)
        assertTrue(ArketipeRegistry.untuk(usaha("pertanian", 55.0, 20.0, 5.0)).punyaBahanBaku)
    }
}
