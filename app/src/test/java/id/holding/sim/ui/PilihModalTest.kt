package id.holding.sim.ui

import id.holding.sim.data.Negara
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class PilihModalTest {
    private fun negara(kode: String) = Negara(
        kode = kode, nama = kode, mataUang = "XXX", pdbUsdMiliar = 1.0, pertumbuhanPct = 1.0,
        inflasiPct = 1.0, bungaAcuanPct = 1.0, kursPerUsd = 1.0, pphBadanPct = 1.0, ppnPct = 1.0,
        pajakDividenPct = 1.0, upahMinBulananUsd = 1.0, populasiJuta = 1.0,
        tanggalData = "2026-10", perkiraan = true
    )

    @Test fun namaKosongDitolak() {
        assertNull(namaHoldingValid("   "))
    }

    @Test fun namaDirapikanDanDibatasi() {
        assertEquals("Maju", namaHoldingValid("  Maju  "))
        assertEquals(NAMA_HOLDING_MAKS, namaHoldingValid("a".repeat(100))!!.length)
    }

    @Test fun negaraBasisAwalUtamakanIndonesia() {
        assertEquals("ID", negaraBasisAwal(listOf(negara("AU"), negara("ID")))!!.kode)
    }

    @Test fun negaraBasisAwalTanpaIdPakaiPertama() {
        assertEquals("AU", negaraBasisAwal(listOf(negara("AU"), negara("SG")))!!.kode)
    }

    @Test fun negaraBasisAwalDaftarKosongNull() {
        assertNull(negaraBasisAwal(emptyList()))
    }
}
