package id.holding.sim.domain.usaha

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class StrategiPropertiKonstruksiTest {
    private val s = StrategiPropertiKonstruksi()

    @Test fun okupansi() {
        assertEquals(80.0, s.okupansiPct(80.0, 100.0), 1e-9)
        assertEquals(100.0, s.okupansiPct(120.0, 100.0), 1e-9)
        assertEquals(0.0, s.okupansiPct(10.0, 0.0), 1e-9)
    }

    @Test fun pendapatanSewa() {
        assertEquals(900.0, s.pendapatanSewa(100.0, 90.0, 10.0), 1e-9)
        assertEquals(1000.0, s.pendapatanSewa(100.0, 120.0, 10.0), 1e-9)
    }

    @Test fun progresProyek() {
        assertEquals(30.0, s.progresProyekPct(300.0, 1000.0), 1e-9)
        assertEquals(100.0, s.progresProyekPct(1200.0, 1000.0), 1e-9)
        assertEquals(0.0, s.progresProyekPct(300.0, 0.0), 1e-9)
    }

    @Test fun pendapatanProyek() {
        assertEquals(300.0, s.pendapatanProyek(1000.0, 40.0, 70.0), 1e-9)
        assertEquals(0.0, s.pendapatanProyek(1000.0, 70.0, 40.0), 1e-9)
    }

    @Test fun terdaftarDiRegistry() {
        assertTrue(ArketipeRegistry.untuk(ArketipeUsahaTestData.usaha("properti_konstruksi")) is StrategiPropertiKonstruksi)
    }
}
