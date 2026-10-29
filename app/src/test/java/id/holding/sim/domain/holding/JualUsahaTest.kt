package id.holding.sim.domain.holding

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class JualUsahaTest {
    private val awal = Holding(
        "A", "ID", 1e10, 1e9, 0.0, 0.0,
        listOf(PosisiUsaha(1, 5e9, 4e8, 1e7), PosisiUsaha(2, 2e9, 1e8, -5e6))
    )

    @Test fun hasilBersihPotongBiayaDuaPersen() {
        assertEquals(4.9e9, JualUsaha.hasilBersihRupiah(awal.usaha[0]), 1e-3)
    }

    @Test fun jualTambahKasDanHapusUsaha() {
        val h = JualUsaha.jual(awal, 1)!!
        assertEquals(1e9 + 4.9e9, h.kasRupiah, 1e-3)
        assertEquals(listOf(2), h.usaha.map { it.usahaId })
    }

    @Test fun usahaTakDimilikiNull() {
        assertNull(JualUsaha.jual(awal, 99))
    }

    @Test fun valuasiTurunSebesarBiayaJual() {
        val h = JualUsaha.jual(awal, 1)!!
        assertEquals(awal.valuasiRupiah - 1e8, h.valuasiRupiah, 1e-3)
    }

    @Test fun biayaNolValuasiTetap() {
        val h = JualUsaha.jual(awal, 2, biayaPct = 0.0)!!
        assertEquals(awal.valuasiRupiah, h.valuasiRupiah, 1e-3)
    }

    @Test fun biayaDiluarRentangDitolak() {
        try {
            JualUsaha.jual(awal, 1, biayaPct = 120.0)
            assertTrue(false)
        } catch (e: IllegalArgumentException) {
            assertTrue(true)
        }
    }

    @Test fun jualLaluBeliLagiBisa() {
        val jual = JualUsaha.jual(awal, 2)!!
        assertEquals(1, jual.usaha.size)
    }
}
