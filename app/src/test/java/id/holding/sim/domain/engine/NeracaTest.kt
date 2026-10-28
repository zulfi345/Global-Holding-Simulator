package id.holding.sim.domain.engine

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class NeracaTest {
    private val awal = Neraca(100.0, 50.0, 40.0, 200.0, 30.0, 100.0, 200.0, 60.0)

    @Test fun neracaSeimbang() {
        assertEquals(390.0, awal.totalAset, 1e-9)
        assertEquals(0.0, awal.selisih, 1e-9)
        assertTrue(NeracaEngine.cekSeimbang(awal))
    }

    @Test fun neracaTidakSeimbangTerdeteksi() {
        val salah = awal.copy(kas = 105.0)
        assertEquals(5.0, salah.selisih, 1e-9)
        assertFalse(NeracaEngine.cekSeimbang(salah))
    }

    @Test fun toleransiDipakai() {
        assertTrue(NeracaEngine.cekSeimbang(awal.copy(kas = 100.005), 0.01))
    }
}
