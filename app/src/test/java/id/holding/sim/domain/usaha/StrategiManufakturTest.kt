package id.holding.sim.domain.usaha

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class StrategiManufakturTest {
    private val s = StrategiManufaktur()

    @Test fun produksiDibatasiKapasitas() {
        assertEquals(800.0, s.produksi(1000.0, 800.0), 1e-9)
        assertEquals(1000.0, s.produksi(1000.0, 1200.0), 1e-9)
    }

    @Test fun utilisasi() {
        assertEquals(75.0, s.utilisasiPct(750.0, 1000.0), 1e-9)
        assertEquals(0.0, s.utilisasiPct(10.0, 0.0), 1e-9)
    }

    @Test fun biayaPerUnitTurunSaatProduksiNaik() {
        assertEquals(10.0, s.biayaPerUnit(1000.0, 5.0, 200.0), 1e-9)
        assertEquals(7.5, s.biayaPerUnit(1000.0, 5.0, 400.0), 1e-9)
        assertEquals(0.0, s.biayaPerUnit(1000.0, 5.0, 0.0), 1e-9)
    }

    @Test fun titikImpas() {
        assertEquals(100.0, s.titikImpasUnit(1000.0, 15.0, 5.0), 1e-9)
        assertTrue(s.titikImpasUnit(1000.0, 5.0, 5.0).isInfinite())
    }

    @Test fun terdaftarDiRegistry() {
        assertTrue(ArketipeRegistry.untuk(ArketipeUsahaTestData.usaha("manufaktur")) is StrategiManufaktur)
    }
}
