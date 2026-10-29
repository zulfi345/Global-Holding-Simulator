package id.holding.sim.domain.holding

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class HoldingTest {
    private fun holding(
        kas: Double,
        utang: Double = 0.0,
        usaha: List<PosisiUsaha> = emptyList()
    ) = Holding("H", "ID", 40e9, kas, utang, 0.0, usaha)

    private fun usaha(nilai: Double, pendapatan: Double = 0.0, laba: Double = 0.0) =
        PosisiUsaha(1, nilai, pendapatan, laba)

    @Test fun mulaiMemakaiSeluruhModalSebagaiKas() {
        val h = Holding.mulai("Garuda", "ID", 40)
        assertEquals(40e9, h.kasRupiah, 1.0)
        assertEquals(40e9, h.modalAwalRupiah, 1.0)
        assertEquals(40e9, h.valuasiRupiah, 1.0)
        assertEquals(0, h.bulan)
        assertTrue(h.peringatan().isEmpty())
    }

    @Test fun semuaLimaPilihanModalValid() {
        for (m in ModalAwal.PILIHAN_MILIAR) assertEquals(m * 1e9, ModalAwal.rupiah(m), 1.0)
    }

    @Test(expected = IllegalArgumentException::class)
    fun modalDiLuarPilihanDitolak() {
        Holding.mulai("X", "ID", 30)
    }

    @Test fun valuasiDanJumlahUsaha() {
        val h = holding(
            kas = 10e9, utang = 5e9,
            usaha = listOf(usaha(20e9, 3e9, 1e9), usaha(30e9, 2e9, 0.5e9))
        )
        assertEquals(50e9, h.nilaiUsahaRupiah, 1.0)
        assertEquals(60e9, h.totalAsetRupiah, 1.0)
        assertEquals(55e9, h.valuasiRupiah, 1.0)
        assertEquals(5e9, h.pendapatanBulananRupiah, 1.0)
        assertEquals(1.5e9, h.labaBersihBulananRupiah, 1.0)
    }

    @Test fun kasTipisBilaKurangDariLimaPersenAset() {
        val h = holding(kas = 1e9, usaha = listOf(usaha(99e9)))
        assertTrue(h.peringatan().contains(PeringatanHolding.KAS_TIPIS))
    }

    @Test fun utangTinggiBilaDiAtasEnamPuluhPersenAset() {
        val h = holding(kas = 50e9, utang = 70e9, usaha = listOf(usaha(50e9)))
        assertTrue(h.peringatan().contains(PeringatanHolding.UTANG_TINGGI))
        assertEquals(30e9, h.valuasiRupiah, 1.0)
    }

    @Test fun rugiBerjalanBilaLabaUsahaNegatif() {
        val h = holding(kas = 40e9, usaha = listOf(usaha(10e9, 1e9, -1e9)))
        assertTrue(h.peringatan().contains(PeringatanHolding.RUGI_BERJALAN))
    }

    @Test fun ekuitasNegatif() {
        val h = holding(kas = 10e9, utang = 20e9)
        assertTrue(h.peringatan().contains(PeringatanHolding.EKUITAS_NEGATIF))
        assertTrue(h.peringatan().contains(PeringatanHolding.UTANG_TINGGI))
    }

    @Test fun sehatTanpaPeringatan() {
        val h = holding(kas = 20e9, utang = 5e9, usaha = listOf(usaha(20e9, 2e9, 0.5e9)))
        assertFalse(h.peringatan().isNotEmpty())
    }
}
