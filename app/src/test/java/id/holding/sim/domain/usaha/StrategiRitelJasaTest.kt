package id.holding.sim.domain.usaha

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class StrategiRitelJasaTest {
    private val s = StrategiRitelJasa()

    @Test fun konversiPelanggan() {
        assertEquals(300.0, s.pelangganMembeli(1000.0, 30.0), 1e-9)
        assertEquals(1000.0, s.pelangganMembeli(1000.0, 150.0), 1e-9)
        assertEquals(0.0, s.pelangganMembeli(1000.0, -5.0), 1e-9)
    }

    @Test fun pendapatan() {
        assertEquals(15000.0, s.pendapatan(300.0, 50.0), 1e-9)
    }

    @Test fun perputaranStok() {
        assertEquals(3.0, s.perputaranStok(600.0, 200.0), 1e-9)
        assertEquals(0.0, s.perputaranStok(600.0, 0.0), 1e-9)
    }

    @Test fun hariPersediaan() {
        assertEquals(91.25, s.hariPersediaan(4.0), 1e-9)
        assertTrue(s.hariPersediaan(0.0).isInfinite())
    }

    @Test fun terdaftarDiRegistry() {
        assertTrue(ArketipeRegistry.untuk(ArketipeUsahaTestData.usaha("ritel_jasa")) is StrategiRitelJasa)
    }
}
