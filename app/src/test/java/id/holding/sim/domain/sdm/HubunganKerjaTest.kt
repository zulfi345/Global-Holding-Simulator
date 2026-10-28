package id.holding.sim.domain.sdm

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.random.Random

class HubunganKerjaTest {
    @Test fun kepuasanMenujuTarget() {
        assertEquals(54.0, HubunganKerja.tickKepuasan(50.0, 1200.0, 1000.0), 1e-9)
        assertEquals(46.0, HubunganKerja.tickKepuasan(50.0, 800.0, 1000.0), 1e-9)
        assertEquals(50.0, HubunganKerja.tickKepuasan(50.0, 500.0, 0.0), 1e-9)
    }

    @Test fun peluangMogok() {
        assertEquals(1.5, HubunganKerja.peluangMogokBulananPct(800.0, 1000.0, 60.0, 50.0), 1e-9)
        assertEquals(0.0, HubunganKerja.peluangMogokBulananPct(800.0, 1000.0, 60.0, 0.0), 1e-9)
        assertEquals(0.0, HubunganKerja.peluangMogokBulananPct(1200.0, 1000.0, 100.0, 80.0), 1e-9)
        assertEquals(10.0, HubunganKerja.peluangMogokBulananPct(0.0, 1000.0, 0.0, 100.0), 1e-9)
    }

    @Test fun apakahMogok() {
        assertFalse(HubunganKerja.apakahMogok(0.0, Random(1)))
        assertTrue(HubunganKerja.apakahMogok(100.0, Random(1)))
    }

    @Test fun biayaMogok() {
        assertEquals(300.0, HubunganKerja.biayaMogok(100.0, 5, 60.0), 1e-9)
        assertEquals(0.0, HubunganKerja.biayaMogok(100.0, -2, 60.0), 1e-9)
    }
}
