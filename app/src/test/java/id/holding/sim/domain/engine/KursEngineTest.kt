package id.holding.sim.domain.engine

import kotlin.random.Random
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class KursEngineTest {
    private val amerika = MakroState("US", 2.0, 3.0, 3.75, 1.0, 31000.0)

    @Test
    fun kursTerpatokTidakBerubah() {
        val sa = MakroState("SA", 4.0, 2.0, 4.25, 3.75, 1150.0)
        val hasil = KursEngine.tickBulanan(sa, amerika, Random(1), riskOffPct = 5.0)
        assertEquals(3.75, hasil.kursPerUsd, 0.0)
    }

    @Test
    fun inflasiLokalTinggiMelemahkanMataUang() {
        val lokal = MakroState("XX", 3.0, 10.0, 5.0, 100.0, 500.0)
        val hasil = KursEngine.tickBulanan(lokal, amerika, Random(1), noiseSkala = 0.0)
        assertTrue(hasil.kursPerUsd > 100.0)
    }

    @Test
    fun riskOffMelemahkanMataUangLokal() {
        val lokal = MakroState("XX", 2.0, 3.0, 3.75, 100.0, 500.0)
        val hasil = KursEngine.tickBulanan(lokal, amerika, Random(1), riskOffPct = 2.0, noiseSkala = 0.0)
        assertEquals(102.0, hasil.kursPerUsd, 1e-9)
    }

    @Test
    fun kursTetapPositifSelama100Tahun() {
        val acak = Random(3)
        var s = MakroState("XX", 3.0, 8.0, 6.0, 100.0, 500.0)
        repeat(1200) {
            s = KursEngine.tickBulanan(s, amerika, acak)
            assertTrue(s.kursPerUsd > 0.0 && !s.kursPerUsd.isNaN())
        }
    }
}
