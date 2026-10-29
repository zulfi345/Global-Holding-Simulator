package id.holding.sim.ui

import id.holding.sim.data.Negara
import id.holding.sim.domain.holding.Holding
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class TickerPasarTest {
    private val negara = Negara(
        kode = "ID", nama = "Indonesia", mataUang = "IDR", pdbUsdMiliar = 1.0, pertumbuhanPct = 5.0,
        inflasiPct = 2.5, bungaAcuanPct = 4.75, kursPerUsd = 17900.0, pphBadanPct = 22.0, ppnPct = 11.0,
        pajakDividenPct = 10.0, upahMinBulananUsd = 1.0, populasiJuta = 1.0, tanggalData = "2026-10", perkiraan = true
    )
    private val h = Holding.mulai("H", "ID", 20)

    @Test fun denganNegaraLimaItem() {
        val t = teksTicker(negara, h)
        assertEquals(5, t.size)
        assertTrue(t[0].startsWith("Jan 2026"))
        assertTrue(t[1].contains("4,75%"))
        assertTrue(t[2].contains("2,5%"))
        assertTrue(t[3].contains("5,0%"))
        assertTrue(t[4].startsWith("Kurs IDR"))
    }

    @Test fun tanpaNegaraHanyaSatuItem() {
        assertEquals(1, teksTicker(null, h).size)
    }

    @Test fun tanggalMengikutiBulan() {
        assertTrue(teksTicker(null, h.copy(bulan = 13))[0].startsWith("Feb 2027"))
    }
}
