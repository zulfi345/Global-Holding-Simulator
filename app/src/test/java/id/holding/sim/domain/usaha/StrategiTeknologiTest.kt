package id.holding.sim.domain.usaha

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class StrategiTeknologiTest {
    private val s = StrategiTeknologi()

    @Test fun pelangganDenganChurn() {
        assertEquals(1030.0, s.tickPelanggan(1000.0, 50.0, 2.0), 1e-9)
        assertEquals(50.0, s.tickPelanggan(1000.0, 50.0, 100.0), 1e-9)
        assertEquals(1050.0, s.tickPelanggan(1000.0, 50.0, -5.0), 1e-9)
    }

    @Test fun pendapatanBerulang() {
        assertEquals(10300.0, s.pendapatanBerulang(1030.0, 10.0), 1e-9)
    }

    @Test fun biayaRnd() {
        assertEquals(150.0, s.biayaRnd(1000.0), 1e-9)
        assertEquals(100.0, StrategiTeknologi(10.0).biayaRnd(1000.0), 1e-9)
    }

    @Test fun nilaiUmurPelanggan() {
        assertEquals(400.0, s.nilaiUmurPelanggan(10.0, 80.0, 2.0), 1e-9)
        assertTrue(s.nilaiUmurPelanggan(10.0, 80.0, 0.0).isInfinite())
    }

    @Test fun terdaftarDiRegistry() {
        assertTrue(ArketipeRegistry.untuk(ArketipeUsahaTestData.usaha("teknologi")) is StrategiTeknologi)
    }
}
