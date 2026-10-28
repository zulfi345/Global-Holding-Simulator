package id.holding.sim.domain.engine

import org.junit.Assert.assertEquals
import org.junit.Test

class StockSplitTest {
    @Test fun splitDuaBandingSatuGandakanUnitSeparuhHarga() {
        val posisi = Kepemilikan("BBCA", 100, 1000.0)
        val hasil = StockSplit.terapkan(posisi, 2.0)
        assertEquals(200, hasil.unit)
        assertEquals(500.0, hasil.hargaRataRata, 1e-9)
    }

    @Test fun reverseSplitDuaBandingSatuSeparuhUnitGandakanHarga() {
        val posisi = Kepemilikan("BBCA", 100, 1000.0)
        val hasil = StockSplit.terapkan(posisi, 0.5)
        assertEquals(50, hasil.unit)
        assertEquals(2000.0, hasil.hargaRataRata, 1e-9)
    }

    @Test fun nilaiTotalPosisiTetapSetelahSplit() {
        val posisi = Kepemilikan("BBCA", 300, 900.0) // nilai total 270000
        val hasil = StockSplit.terapkan(posisi, 3.0)
        assertEquals(900, hasil.unit)
        assertEquals(300.0, hasil.hargaRataRata, 1e-9)
        assertEquals(270_000.0, hasil.unit * hasil.hargaRataRata, 1e-6)
    }

    @Test fun pembulatanUnitKeBawahSaatTidakHabisDibagi() {
        val posisi = Kepemilikan("BBCA", 101, 1000.0)
        val hasil = StockSplit.terapkan(posisi, 0.5) // 101 * 0.5 = 50.5 -> 50
        assertEquals(50, hasil.unit)
    }

    @Test(expected = IllegalArgumentException::class)
    fun rasioNolDitolak() {
        StockSplit.terapkan(Kepemilikan("BBCA", 100, 1000.0), 0.0)
    }
}
