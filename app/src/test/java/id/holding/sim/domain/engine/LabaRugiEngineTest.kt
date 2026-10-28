package id.holding.sim.domain.engine

import org.junit.Assert.assertEquals
import org.junit.Test

class LabaRugiEngineTest {
    private val d = 1e-9

    @Test
    fun alurDasar() {
        val r = LabaRugiEngine.hitung(1000.0, 400.0, 200.0, 100.0, 50.0, 0.0, 0.0, 20.0)
        assertEquals(600.0, r.labaKotor, d)
        assertEquals(400.0, r.ebitda, d)
        assertEquals(300.0, r.ebit, d)
        assertEquals(250.0, r.labaSebelumPajak, d)
        assertEquals(50.0, r.pajak, d)
        assertEquals(200.0, r.labaBersih, d)
        assertEquals(0.0, r.sisaRugiFiskal, d)
        assertEquals(0.2, r.marginBersih, d)
    }

    @Test
    fun rugiTidakDipajakiDanMenambahSaldoRugi() {
        val r = LabaRugiEngine.hitung(100.0, 80.0, 50.0, 10.0, 0.0, 0.0, 0.0, 22.0)
        assertEquals(-40.0, r.labaSebelumPajak, d)
        assertEquals(0.0, r.pajak, d)
        assertEquals(-40.0, r.labaBersih, d)
        assertEquals(40.0, r.sisaRugiFiskal, d)
    }

    @Test
    fun rugiBawaanMengurangiPajak() {
        val r = LabaRugiEngine.hitung(500.0, 100.0, 100.0, 20.0, 10.0, 5.0, 50.0, 25.0)
        assertEquals(275.0, r.labaSebelumPajak, d)
        assertEquals(56.25, r.pajak, d)
        assertEquals(218.75, r.labaBersih, d)
        assertEquals(0.0, r.sisaRugiFiskal, d)
    }
}
