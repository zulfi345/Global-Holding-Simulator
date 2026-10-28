package id.holding.sim.domain.sdm

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.random.Random

class TurnoverTest {
    @Test fun gajiMemengaruhiTurnover() {
        assertEquals(1.5, Turnover.turnoverBulananPct(1000.0, 1000.0), 1e-9)
        assertEquals(1.8, Turnover.turnoverBulananPct(800.0, 1000.0), 1e-9)
        assertEquals(1.2, Turnover.turnoverBulananPct(1200.0, 1000.0), 1e-9)
        assertEquals(0.6, Turnover.turnoverBulananPct(3000.0, 1000.0), 1e-9)
        assertEquals(1.5, Turnover.turnoverBulananPct(500.0, 0.0), 1e-9)
    }

    @Test fun masaPercobaan() {
        assertEquals(1.5, Turnover.faktorPercobaan(2, 3), 1e-9)
        assertEquals(1.0, Turnover.faktorPercobaan(3, 3), 1e-9)
        assertEquals(2.25, Turnover.turnoverEfektifPct(1000.0, 1000.0, 2, 3), 1e-9)
    }

    @Test fun jumlahKeluar() {
        assertEquals(0, Turnover.jumlahKeluar(100, 0.0, Random(1)))
        assertEquals(100, Turnover.jumlahKeluar(100, 100.0, Random(1)))
        assertEquals(Turnover.jumlahKeluar(200, 10.0, Random(5)), Turnover.jumlahKeluar(200, 10.0, Random(5)))
        assertTrue(Turnover.jumlahKeluar(200, 10.0, Random(9)) in 0..200)
        assertEquals(0, Turnover.jumlahKeluar(-3, 50.0, Random(1)))
    }

    @Test fun biayaPelatihan() {
        assertEquals(400.0, Turnover.biayaPelatihan(1000.0, 2.0), 1e-9)
        assertEquals(0.0, Turnover.biayaPelatihan(1000.0, -1.0), 1e-9)
    }
}
