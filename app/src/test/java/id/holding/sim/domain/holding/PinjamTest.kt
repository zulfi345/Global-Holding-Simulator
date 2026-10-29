package id.holding.sim.domain.holding

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class PinjamTest {
    private val awal = Holding.mulai("H", "ID", 20)

    @Test fun pinjamPertamaMenaikkanKasDanUtang() {
        val h = Pinjam.pinjam(awal, 10e9, 8.0)!!
        assertEquals(30e9, h.kasRupiah, 1e-3)
        assertEquals(10e9, h.utangRupiah, 1e-3)
        assertEquals(8.0, h.bungaUtangPct, 1e-9)
    }

    @Test fun pinjamKeduaBungaRataRataTertimbang() {
        val h1 = Pinjam.pinjam(awal, 10e9, 8.0)!!
        val h2 = Pinjam.pinjam(h1, 30e9, 12.0)!!
        assertEquals(40e9, h2.utangRupiah, 1e-3)
        assertEquals(11.0, h2.bungaUtangPct, 1e-9)
    }

    @Test fun pinjamTakValidNull() {
        assertNull(Pinjam.pinjam(awal, 0.0, 8.0))
        assertNull(Pinjam.pinjam(awal, 5e9, -1.0))
    }

    @Test fun lunasiSebagianBungaTetap() {
        val h = Pinjam.lunasi(Pinjam.pinjam(awal, 10e9, 8.0)!!, 4e9)!!
        assertEquals(26e9, h.kasRupiah, 1e-3)
        assertEquals(6e9, h.utangRupiah, 1e-3)
        assertEquals(8.0, h.bungaUtangPct, 1e-9)
    }

    @Test fun lunasiPenuhBungaNol() {
        val h = Pinjam.lunasi(Pinjam.pinjam(awal, 10e9, 8.0)!!, 10e9)!!
        assertEquals(0.0, h.utangRupiah, 1e-3)
        assertEquals(0.0, h.bungaUtangPct, 1e-9)
    }

    @Test fun lunasiMelebihiUtangAtauKasNull() {
        val h = Pinjam.pinjam(awal, 10e9, 8.0)!!
        assertNull(Pinjam.lunasi(h, 11e9))
        assertNull(Pinjam.lunasi(h.copy(kasRupiah = 1e9), 2e9))
        assertNull(Pinjam.lunasi(h, 0.0))
    }
}
