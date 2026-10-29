package id.holding.sim.ui

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class GrafikValuasiTest {
    @Test fun kosongTanpaTitik() {
        assertTrue(titikGrafik(emptyList(), 100f, 50f).isEmpty())
    }

    @Test fun satuNilaiDiTengah() {
        val t = titikGrafik(listOf(5.0), 100f, 50f)
        assertEquals(Pair(50f, 25f), t[0])
    }

    @Test fun naikDariBawahKeAtas() {
        val t = titikGrafik(listOf(0.0, 10.0), 108f, 58f, margin = 4f)
        assertEquals(Pair(4f, 54f), t[0])
        assertEquals(Pair(104f, 4f), t[1])
    }

    @Test fun nilaiSamaGarisDatarDiTengah() {
        val t = titikGrafik(listOf(3.0, 3.0, 3.0), 100f, 40f)
        assertTrue(t.all { it.second == 20f })
    }
}
