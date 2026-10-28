package id.holding.sim.domain.engine

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.random.Random

class HargaAsetTest {
    @Test fun hargaKembaliKeAwalTanpaNoise() {
        // volatilitas 0 -> noise term = 0, hanya kecepatanKembali yang bekerja
        assertEquals(142.5, HargaAset.tick(150.0, 100.0, 0.0, Random(1), 0.1), 1e-9)
        assertEquals(52.5, HargaAset.tick(50.0, 100.0, 0.0, Random(1), 0.1), 1e-9)
    }

    @Test fun hargaDeterministikUntukBenihSama() {
        val a = HargaAset.tick(100.0, 100.0, 15.0, Random(7))
        val b = HargaAset.tick(100.0, 100.0, 15.0, Random(7))
        assertEquals(a, b, 0.0)
    }

    @Test fun hargaTidakDiBawahBatasBawah() {
        val h = HargaAset.tick(1.0, 100.0, 300.0, Random(3), 0.0)
        assertTrue(h >= 5.0)
    }

    @Test fun volatilitasLebihTinggiBisaMenghasilkanPerubahanLebihBesar() {
        val rendah = HargaAset.tick(100.0, 100.0, 5.0, Random(2), 0.0)
        val tinggi = HargaAset.tick(100.0, 100.0, 60.0, Random(2), 0.0)
        assertTrue(Math.abs(tinggi - 100.0) >= Math.abs(rendah - 100.0))
    }
}
