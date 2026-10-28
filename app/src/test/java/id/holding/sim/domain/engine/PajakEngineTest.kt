package id.holding.sim.domain.engine

import org.junit.Assert.assertEquals
import org.junit.Test

class PajakEngineTest {
    private val d = 1e-9

    @Test
    fun labaTanpaRugi() {
        val h = PajakEngine.pphBadan(100.0, 0.0, 22.0)
        assertEquals(22.0, h.pajak, d)
        assertEquals(0.0, h.sisaRugi, d)
    }

    @Test
    fun rugiMenambahSaldoRugi() {
        val h = PajakEngine.pphBadan(-50.0, 10.0, 22.0)
        assertEquals(0.0, h.pajak, d)
        assertEquals(60.0, h.sisaRugi, d)
    }

    @Test
    fun rugiBawaanMengurangiLaba() {
        val h = PajakEngine.pphBadan(100.0, 30.0, 20.0)
        assertEquals(14.0, h.pajak, d)
        assertEquals(0.0, h.sisaRugi, d)
    }

    @Test
    fun rugiBawaanLebihBesarDariLaba() {
        val h = PajakEngine.pphBadan(100.0, 150.0, 20.0)
        assertEquals(0.0, h.pajak, d)
        assertEquals(50.0, h.sisaRugi, d)
    }

    @Test
    fun ppnNetoNormalDanLebihBayar() {
        assertEquals(66.0, PajakEngine.ppnNeto(1000.0, 400.0, 11.0), d)
        assertEquals(-33.0, PajakEngine.ppnNeto(100.0, 400.0, 11.0), d)
    }

    @Test
    fun pajakDividen() {
        assertEquals(200.0, PajakEngine.pajakDividen(1000.0, 20.0), d)
        assertEquals(0.0, PajakEngine.pajakDividen(-5.0, 20.0), d)
    }
}
