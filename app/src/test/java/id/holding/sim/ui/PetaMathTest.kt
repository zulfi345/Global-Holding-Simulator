package id.holding.sim.ui

import org.junit.Assert.assertEquals
import org.junit.Test

class PetaMathTest {
    @Test fun normalisasiTitikAcuan() {
        assertEquals(0.5, PetaMath.xNorm(0.0), 1e-9)
        assertEquals(0.0, PetaMath.xNorm(-180.0), 1e-9)
        assertEquals(1.0, PetaMath.xNorm(180.0), 1e-9)
        assertEquals(0.0, PetaMath.yNorm(90.0), 1e-9)
        assertEquals(0.5, PetaMath.yNorm(0.0), 1e-9)
        assertEquals(1.0, PetaMath.yNorm(-90.0), 1e-9)
    }

    @Test fun ukuranDasarPortraitDanLanskap() {
        assertEquals(Pair(1400f, 700f), PetaMath.ukuranDasar(400f, 700f))
        assertEquals(Pair(1000f, 500f), PetaMath.ukuranDasar(1000f, 500f))
        assertEquals(Pair(2000f, 1000f), PetaMath.ukuranDasar(2000f, 500f))
    }

    @Test fun batasiMenahCelahDanSkala() {
        val t = PetaMath.batasi(TransformPeta(100f, 50f, 50f), 400f, 700f)
        assertEquals(PetaMath.SKALA_MAKS, t.skala, 1e-6f)
        assertEquals(0f, t.offsetX, 1e-3f)
        assertEquals(0f, t.offsetY, 1e-3f)
        val u = PetaMath.batasi(TransformPeta(1f, -5000f, -5000f), 400f, 700f)
        assertEquals(400f - 1400f, u.offsetX, 1e-3f)
        assertEquals(0f, u.offsetY, 1e-3f)
    }

    @Test fun zoomMempertahankanTitikPusat() {
        val t = PetaMath.terapkan(TransformPeta(1f, 0f, 0f), 0f, 0f, 2f, 200f, 350f, 400f, 700f)
        assertEquals(2f, t.skala, 1e-6f)
        assertEquals(-200f, t.offsetX, 1e-3f)
        assertEquals(-350f, t.offsetY, 1e-3f)
    }

    @Test fun zoomKeluarTakMelewatiSkalaMin() {
        val t = PetaMath.terapkan(TransformPeta(1f, 0f, 0f), 0f, 0f, 0.1f, 200f, 350f, 400f, 700f)
        assertEquals(PetaMath.SKALA_MIN, t.skala, 1e-6f)
    }

    @Test fun pusatkanJakartaTepatDiTengahLayar() {
        val t = PetaMath.pusatkan(-6.2, 106.82, 2f, 400f, 700f)
        val (x, y) = PetaMath.posisiLayar(-6.2, 106.82, t, 400f, 700f)
        assertEquals(200f, x, 0.5f)
        assertEquals(350f, y, 0.5f)
    }

    @Test fun posisiLayarSkalaSatuTanpaOffset() {
        val (x, y) = PetaMath.posisiLayar(0.0, 0.0, TransformPeta(1f, 0f, 0f), 400f, 700f)
        assertEquals(700f, x, 1e-3f)
        assertEquals(350f, y, 1e-3f)
    }
}
