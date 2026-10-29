package id.holding.sim.data

import id.holding.sim.domain.holding.Holding
import id.holding.sim.domain.holding.PosisiUsaha
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class HoldingPenyimpananTest {
    private val contoh = Holding(
        nama = "Maju=Jaya; \u00C9 & Co\nbaris", negaraBasis = "ID", modalAwalRupiah = 4e10,
        kasRupiah = 1.234567890123e10, utangRupiah = 5e9, nilaiInvestasiRupiah = 2.5e9,
        usaha = listOf(PosisiUsaha(3, 1e9, 2e8, -1.5e7), PosisiUsaha(12, 7.5e9, 1e9, 3e8)), bulan = 7
    )

    @Test fun bolakBalikSamaPersis() {
        assertEquals(contoh, HoldingPenyimpanan.dariTeks(HoldingPenyimpanan.keTeks(contoh)))
    }

    @Test fun tanpaUsahaBolakBalik() {
        val h = Holding.mulai("A", "SG", 20)
        assertEquals(h, HoldingPenyimpanan.dariTeks(HoldingPenyimpanan.keTeks(h)))
    }

    @Test fun teksNullAtauRusakMenjadiNull() {
        assertNull(HoldingPenyimpanan.dariTeks(null))
        assertNull(HoldingPenyimpanan.dariTeks("sampah"))
        assertNull(HoldingPenyimpanan.dariTeks(HoldingPenyimpanan.keTeks(contoh).replace("bulan=7", "bulan=x")))
    }

    @Test fun versiLainDitolak() {
        assertNull(HoldingPenyimpanan.dariTeks(HoldingPenyimpanan.keTeks(contoh).replace("v=1", "v=9")))
    }

    @Test fun riwayatValuasiBolakBalik() {
        val h = contoh.copy(riwayatValuasiRupiah = listOf(4e10, 4.01e10, 3.9e10))
        assertEquals(h, HoldingPenyimpanan.dariTeks(HoldingPenyimpanan.keTeks(h)))
    }

    @Test fun teksLamaTanpaRiwayatTetapTermuat() {
        val lama = HoldingPenyimpanan.keTeks(contoh).lines().filter { !it.startsWith("riwayat=") }.joinToString("\n")
        assertEquals(emptyList<Double>(), HoldingPenyimpanan.dariTeks(lama)!!.riwayatValuasiRupiah)
    }

    @Test fun rugiFiskalBolakBalik() {
        val h = contoh.copy(rugiFiskalRupiah = 1.5e7)
        assertEquals(h, HoldingPenyimpanan.dariTeks(HoldingPenyimpanan.keTeks(h)))
    }

    @Test fun bungaUtangBolakBalik() {
        val h = contoh.copy(bungaUtangPct = 9.5)
        assertEquals(h, HoldingPenyimpanan.dariTeks(HoldingPenyimpanan.keTeks(h)))
    }

    @Test fun teksLamaTanpaBungaTermuatNol() {
        val lama = HoldingPenyimpanan.keTeks(contoh).lines().filter { !it.startsWith("bunga=") }.joinToString("\n")
        assertEquals(0.0, HoldingPenyimpanan.dariTeks(lama)!!.bungaUtangPct, 1e-9)
    }
}
