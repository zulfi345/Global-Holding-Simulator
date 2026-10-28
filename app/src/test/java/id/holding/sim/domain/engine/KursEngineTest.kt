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

    @Test
    fun terpatokDiperluas() {
        for (k in listOf("QA", "HK", "EC")) {
            val s = MakroState(k, 3.0, 2.0, 4.0, 3.64, 100.0)
            assertEquals(3.64, KursEngine.tickBulanan(s, amerika, Random(1), riskOffPct = 5.0).kursPerUsd, 0.0)
        }
    }

    private fun daftarUji() = listOf(
        amerika,
        MakroState("DE", 0.8, 2.0, 2.0, 0.87, 5200.0),
        MakroState("FR", 0.8, 1.2, 2.0, 0.87, 3400.0),
        MakroState("IT", 0.7, 1.8, 2.0, 0.87, 2500.0),
        MakroState("DK", 2.0, 1.8, 1.6, 6.5, 450.0),
        MakroState("SG", 2.5, 1.0, 1.5, 1.3, 560.0),
        MakroState("BN", 2.5, 0.5, 0.5, 1.3, 16.0)
    )

    @Test
    fun negaraEuroBergerakSama() {
        val h = KursEngine.tickSemua(daftarUji(), Random(1), riskOffPct = 2.0, noiseSkala = 0.0).associateBy { it.kode }
        assertEquals(h.getValue("DE").kursPerUsd, h.getValue("FR").kursPerUsd, 0.0)
        assertEquals(h.getValue("DE").kursPerUsd, h.getValue("IT").kursPerUsd, 0.0)
        assertTrue(h.getValue("DE").kursPerUsd > 0.87)
    }

    @Test
    fun mataUangTerkaitMengikutiAcuan() {
        val h = KursEngine.tickSemua(daftarUji(), Random(1), riskOffPct = 2.0, noiseSkala = 0.0).associateBy { it.kode }
        assertEquals(h.getValue("DE").kursPerUsd * 7.46038, h.getValue("DK").kursPerUsd, 1e-9)
        assertEquals(h.getValue("SG").kursPerUsd, h.getValue("BN").kursPerUsd, 1e-9)
    }

    @Test
    fun urutanHasilSamaDenganMasukan() {
        val masuk = daftarUji()
        val keluar = KursEngine.tickSemua(masuk, Random(2))
        assertEquals(masuk.map { it.kode }, keluar.map { it.kode })
    }
}
