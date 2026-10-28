package id.holding.sim.domain.engine

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ArusKasTest {
    private val awal = Neraca(100.0, 50.0, 40.0, 200.0, 30.0, 100.0, 200.0, 60.0)
    // laba bersih 40, penyusutan 20, belanja modal 30, dividen 10, utang bank turun 20
    private val akhir = Neraca(90.0, 60.0, 45.0, 210.0, 35.0, 80.0, 200.0, 90.0)

    @Test fun komponenBenar() {
        val ak = ArusKasEngine.hitung(awal, akhir, 40.0, 20.0)
        assertEquals(50.0, ak.operasi, 1e-9)
        assertEquals(-30.0, ak.investasi, 1e-9)
        assertEquals(-30.0, ak.pendanaan, 1e-9)
        assertEquals(10.0, ak.dividen, 1e-9)
        assertEquals(30.0, ak.belanjaModal, 1e-9)
        assertEquals(-10.0, ak.kasBersih, 1e-9)
    }

    @Test fun cocokDenganPerubahanKas() {
        assertTrue(akhir.seimbang())
        val ak = ArusKasEngine.hitung(awal, akhir, 40.0, 20.0)
        assertTrue(ArusKasEngine.cocokDenganKas(awal, akhir, ak))
    }
}
