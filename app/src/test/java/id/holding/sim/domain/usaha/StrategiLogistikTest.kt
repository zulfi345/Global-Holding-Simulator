package id.holding.sim.domain.usaha

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class StrategiLogistikTest {
    private val s = StrategiLogistik()

    @Test fun kapasitasArmada() {
        assertEquals(200.0, s.kapasitasArmada(10, 20.0), 1e-9)
        assertEquals(0.0, s.kapasitasArmada(-3, 20.0), 1e-9)
    }

    @Test fun utilisasiMuatan() {
        assertEquals(75.0, s.utilisasiMuatanPct(150.0, 200.0), 1e-9)
        assertEquals(0.0, s.utilisasiMuatanPct(150.0, 0.0), 1e-9)
    }

    @Test fun biayaBahanBakar() {
        assertEquals(600.0, s.biayaBahanBakar(1000.0, 30.0, 2.0), 1e-9)
    }

    @Test fun biayaPerTonKm() {
        assertEquals(2.0, s.biayaPerTonKm(600.0, 300.0), 1e-9)
        assertEquals(0.0, s.biayaPerTonKm(600.0, 0.0), 1e-9)
    }

    @Test fun terdaftarDiRegistry() {
        assertTrue(ArketipeRegistry.untuk(ArketipeUsahaTestData.usaha("logistik")) is StrategiLogistik)
    }
}
