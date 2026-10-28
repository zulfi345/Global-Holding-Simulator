package id.holding.sim.domain.engine

import kotlin.random.Random
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class MakroEngineTest {
    private val dasar = MakroState("XX", 5.0, 2.5, 4.75, 100.0, 1000.0)

    @Test
    fun seedSamaHasilSama() {
        val a = MakroEngine.tickBulanan(dasar, dasar, Random(42))
        val b = MakroEngine.tickBulanan(dasar, dasar, Random(42))
        assertEquals(a, b)
    }

    @Test
    fun nilaiTetapDalamBatasSelama100Tahun() {
        val acak = Random(7)
        var s = dasar
        repeat(1200) {
            s = MakroEngine.tickBulanan(s, dasar, acak, guncanganGlobal = -0.5)
            assertTrue(s.pertumbuhanPct in -8.0..15.0)
            assertTrue(s.inflasiPct in -2.0..25.0)
            assertTrue(s.bungaPct in 0.0..25.0)
            assertTrue(!s.pdbUsdMiliar.isNaN() && s.pdbUsdMiliar > 0.0)
        }
    }

    @Test
    fun kembaliKeDasarTanpaAcak() {
        var s = dasar.copy(pertumbuhanPct = 12.0, inflasiPct = 10.0, bungaPct = 15.0)
        repeat(400) {
            s = MakroEngine.tickBulanan(s, dasar, Random(1), noiseSkala = 0.0)
        }
        assertEquals(dasar.pertumbuhanPct, s.pertumbuhanPct, 0.1)
        assertEquals(dasar.inflasiPct, s.inflasiPct, 0.1)
        assertEquals(dasar.bungaPct, s.bungaPct, 0.1)
    }
}
