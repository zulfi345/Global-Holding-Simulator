package id.holding.sim.domain.usaha

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.random.Random

class StrategiPertanianTest {
    private val s = StrategiPertanian(setOf(4, 10))

    @Test fun pendapatanHanyaBulanPanen() {
        assertEquals(500.0, s.pendapatanBulan(4, 1000.0), 1e-9)
        assertEquals(500.0, s.pendapatanBulan(10, 1000.0), 1e-9)
        assertEquals(0.0, s.pendapatanBulan(5, 1000.0), 1e-9)
        assertEquals(0.0, StrategiPertanian(emptySet()).pendapatanBulan(4, 1000.0), 1e-9)
    }

    @Test fun faktorHasilBatas() {
        assertEquals(1.0, s.faktorHasil(Random(1), 0.0, 50.0), 1e-9)
        repeat(20) {
            val f = s.faktorHasil(Random(it), 100.0, 50.0)
            assertTrue(f >= 0.5 && f <= 1.0)
        }
    }

    @Test fun hasilPanen() {
        assertEquals(360.0, s.hasilPanen(100.0, 4.0, 0.9), 1e-9)
    }

    @Test fun biayaDanRegistry() {
        val u = ArketipeUsahaTestData.usaha("pertanian", 55.0, 20.0, 5.0)
        val b = ArketipeRegistry.untuk(u).strukturBiaya(u, 1000.0)
        assertEquals(550.0, b.hpp, 1e-9)
        assertTrue(ArketipeRegistry.untuk(u) is StrategiPertanian)
    }

    @Test fun hargaKomoditasBersama() {
        assertEquals(142.5, HargaKomoditas.tick(150.0, 100.0, Random(1), 0.1, 0.0), 1e-9)
    }
}
