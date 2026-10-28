package id.holding.sim.domain.usaha

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class StrategiLayananRegulasiTest {
    private val s = StrategiLayananRegulasi()

    @Test fun tarifDibatasiPlafon() {
        assertEquals(10.0, s.tarifEfektif(12.0, 10.0), 1e-9)
        assertEquals(8.0, s.tarifEfektif(8.0, 10.0), 1e-9)
        assertEquals(1000.0, s.pendapatan(100.0, 12.0, 10.0), 1e-9)
    }

    @Test fun biayaKepatuhan() {
        assertEquals(30.0, s.biayaKepatuhan(1000.0), 1e-9)
        assertEquals(50.0, StrategiLayananRegulasi(5.0).biayaKepatuhan(1000.0), 1e-9)
    }

    @Test fun lisensi() {
        assertTrue(s.lisensiBerlaku(10, 12))
        assertFalse(s.lisensiBerlaku(13, 12))
        assertEquals(2, s.sisaBulanLisensi(10, 12))
        assertEquals(0, s.sisaBulanLisensi(13, 12))
    }

    @Test fun terdaftarDiRegistry() {
        assertTrue(ArketipeRegistry.untuk(ArketipeUsahaTestData.usaha("layanan_regulasi")) is StrategiLayananRegulasi)
    }
}
