package id.holding.sim.data

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class KoordinatNegaraTest {
    @Test fun seratusNegara() = assertEquals(100, KoordinatNegara.PETA.size)

    @Test fun semuaDalamRentangValid() {
        for ((kode, p) in KoordinatNegara.PETA) {
            assertTrue("lintang $kode", p.first >= -90.0 && p.first <= 90.0)
            assertTrue("bujur $kode", p.second >= -180.0 && p.second <= 180.0)
        }
    }

    @Test fun indonesiaDiBelahanSelatanTimur() {
        val p = KoordinatNegara.cari("ID")!!
        assertTrue(p.first < 0.0)
        assertTrue(p.second > 100.0)
    }

    @Test fun kodeTakDikenalNull() {
        assertNotNull(KoordinatNegara.cari("DE"))
        assertNull(KoordinatNegara.cari("ZZ"))
    }
}
