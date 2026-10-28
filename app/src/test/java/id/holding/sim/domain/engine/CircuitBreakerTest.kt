package id.holding.sim.domain.engine

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class CircuitBreakerTest {
    @Test fun hargaDalamRentangTidakDiubah() {
        assertEquals(1050.0, CircuitBreaker.batasi(1050.0, 1000.0, 25.0, 25.0), 1e-9)
    }

    @Test fun hargaDiAtasBatasAtasDipotongKeAra() {
        assertEquals(1250.0, CircuitBreaker.batasi(2000.0, 1000.0, 25.0, 25.0), 1e-9)
    }

    @Test fun hargaDiBawahBatasBawahDipotongKeArb() {
        assertEquals(750.0, CircuitBreaker.batasi(100.0, 1000.0, 25.0, 25.0), 1e-9)
    }

    @Test fun batasAsimetris() {
        assertEquals(1100.0, CircuitBreaker.batasi(5000.0, 1000.0, 10.0, 35.0), 1e-9)
        assertEquals(650.0, CircuitBreaker.batasi(10.0, 1000.0, 10.0, 35.0), 1e-9)
    }

    @Test fun deteksiKenaAraDanArb() {
        assertTrue(CircuitBreaker.kenaAra(1300.0, 1000.0, 25.0))
        assertFalse(CircuitBreaker.kenaAra(1200.0, 1000.0, 25.0))
        assertTrue(CircuitBreaker.kenaArb(700.0, 1000.0, 25.0))
        assertFalse(CircuitBreaker.kenaArb(800.0, 1000.0, 25.0))
    }

    @Test fun defaultPctDigunakanJikaTidakDisebut() {
        assertEquals(1250.0, CircuitBreaker.batasi(9999.0, 1000.0), 1e-9)
        assertEquals(750.0, CircuitBreaker.batasi(1.0, 1000.0), 1e-9)
    }
}
