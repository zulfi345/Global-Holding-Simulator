package id.holding.sim.domain.sdm

import org.junit.Assert.assertEquals
import org.junit.Test

class KompensasiTest {
    @Test fun bonusKinerja() {
        assertEquals(2400.0, Kompensasi.bonusKinerja(12000.0, 100.0), 1e-9)
        assertEquals(0.0, Kompensasi.bonusKinerja(12000.0, 79.0), 1e-9)
        assertEquals(1920.0, Kompensasi.bonusKinerja(12000.0, 80.0), 1e-9)
        assertEquals(3600.0, Kompensasi.bonusKinerja(12000.0, 200.0), 1e-9)
    }

    @Test fun nilaiOpsi() {
        assertEquals(500.0, Kompensasi.nilaiOpsi(15.0, 10.0, 100.0), 1e-9)
        assertEquals(0.0, Kompensasi.nilaiOpsi(8.0, 10.0, 100.0), 1e-9)
    }

    @Test fun vesting() {
        assertEquals(0.0, Kompensasi.opsiVested(100.0, 6), 1e-9)
        assertEquals(25.0, Kompensasi.opsiVested(100.0, 12), 1e-9)
        assertEquals(100.0, Kompensasi.opsiVested(100.0, 60), 1e-9)
        assertEquals(100.0, Kompensasi.opsiVested(100.0, 12, 0, 12), 1e-9)
    }

    @Test fun totalTahunan() {
        assertEquals(14900.0, Kompensasi.totalTahunan(1000.0, 2400.0, 500.0), 1e-9)
    }
}
