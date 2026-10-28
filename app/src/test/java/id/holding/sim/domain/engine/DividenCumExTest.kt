package id.holding.sim.domain.engine

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class DividenCumExTest {
    @Test fun belisebelumCumDateBerhak() {
        assertTrue(DividenCumEx.berhakDividen(bulanBeli = 5, bulanCum = 10))
    }

    @Test fun beliPadaBulanCumDateBerhak() {
        assertTrue(DividenCumEx.berhakDividen(bulanBeli = 10, bulanCum = 10))
    }

    @Test fun beliSetelahCumDateExDateTidakBerhak() {
        assertFalse(DividenCumEx.berhakDividen(bulanBeli = 11, bulanCum = 10))
    }

    @Test fun jualSetelahCumDateTetapBerhak() {
        assertTrue(DividenCumEx.berhakDividen(bulanBeli = 5, bulanCum = 10, bulanJual = 11))
    }

    @Test fun jualPadaAtauSebelumCumDateTidakBerhak() {
        assertFalse(DividenCumEx.berhakDividen(bulanBeli = 5, bulanCum = 10, bulanJual = 10))
        assertFalse(DividenCumEx.berhakDividen(bulanBeli = 5, bulanCum = 10, bulanJual = 8))
    }

    @Test fun tidakDijualBerhakJikaDibeliSebelumCum() {
        assertTrue(DividenCumEx.berhakDividen(bulanBeli = 1, bulanCum = 10, bulanJual = null))
    }
}
