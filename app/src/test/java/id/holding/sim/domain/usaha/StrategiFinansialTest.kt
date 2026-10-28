package id.holding.sim.domain.usaha

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class StrategiFinansialTest {
    private val s = StrategiFinansial()

    @Test fun nim() {
        assertEquals(6.0, s.nimPct(100.0, 40.0, 1000.0), 1e-9)
        assertEquals(0.0, s.nimPct(100.0, 40.0, 0.0), 1e-9)
    }

    @Test fun cadanganDariNpl() {
        assertEquals(40.0, s.cadanganDibutuhkan(1000.0, 4.0), 1e-9)
        assertEquals(32.0, StrategiFinansial(coverageNplPct = 80.0).cadanganDibutuhkan(1000.0, 4.0), 1e-9)
    }

    @Test fun biayaCadangan() {
        assertEquals(15.0, s.biayaCadangan(30.0, 40.0, 5.0), 1e-9)
        assertEquals(-10.0, s.biayaCadangan(50.0, 40.0, 0.0), 1e-9)
    }

    @Test fun car() {
        assertEquals(12.0, s.carPct(120.0, 1000.0), 1e-9)
        assertEquals(80.0, s.modalMinimum(1000.0), 1e-9)
        assertTrue(s.memenuhiCar(120.0, 1000.0))
        assertFalse(s.memenuhiCar(70.0, 1000.0))
        assertTrue(s.memenuhiCar(0.0, 0.0))
    }

    @Test fun terdaftarDiRegistry() {
        val u = ArketipeUsahaTestData.usaha("finansial")
        assertTrue(ArketipeRegistry.untuk(u) is StrategiFinansial)
    }
}
