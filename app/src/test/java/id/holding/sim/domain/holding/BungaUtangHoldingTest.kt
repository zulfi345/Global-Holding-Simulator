package id.holding.sim.domain.holding

import org.junit.Assert.assertEquals
import org.junit.Test

class BungaUtangHoldingTest {
    private val dasar = Holding.mulai("H", "ID", 20)

    @Test fun bebanBungaBulanan() {
        // 12 miliar x 10% / 12 = 100 juta
        val h = dasar.copy(utangRupiah = 12e9, bungaUtangPct = 10.0)
        assertEquals(100e6, h.bebanBungaBulananRupiah, 1e-3)
    }

    @Test fun tanpaUtangTanpaBunga() {
        assertEquals(0.0, dasar.bebanBungaBulananRupiah, 1e-9)
    }

    @Test fun lanjutBulanMengurangiKasDenganBunga() {
        val h = dasar.copy(utangRupiah = 12e9, bungaUtangPct = 10.0)
        val k = LanjutBulan.maju(h)
        assertEquals(h.kasRupiah - 100e6, k.kasRupiah, 1e-3)
    }

    @Test fun bungaMenjadiRugiFiskalBilaTanpaLaba() {
        val h = dasar.copy(utangRupiah = 12e9, bungaUtangPct = 10.0)
        val k = LanjutBulan.maju(h, 22.0)
        assertEquals(100e6, k.rugiFiskalRupiah, 1e-3)
    }

    @Test fun bungaMengurangiPajak() {
        val u = PosisiUsaha(1, 1e9, 1e9, 400e6)
        val h = dasar.copy(usaha = listOf(u), utangRupiah = 12e9, bungaUtangPct = 10.0)
        // laba sebelum pajak 400jt - 100jt = 300jt; pajak 20% = 60jt
        val k = LanjutBulan.maju(h, 20.0)
        assertEquals(h.kasRupiah + 300e6 - 60e6, k.kasRupiah, 1e-3)
    }
}
