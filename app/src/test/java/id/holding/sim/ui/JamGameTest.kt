package id.holding.sim.ui

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class JamGameTest {
    @Test fun jedaBerjedaNol() = assertEquals(0L, KecepatanJam.JEDA.jedaMs)

    @Test fun makinCepatJedaMakinPendek() {
        assertTrue(KecepatanJam.X1.jedaMs > KecepatanJam.X2.jedaMs)
        assertTrue(KecepatanJam.X2.jedaMs > KecepatanJam.X5.jedaMs)
        assertTrue(KecepatanJam.X5.jedaMs > 0L)
    }

    @Test fun siklusKembaliKeJeda() {
        var k = KecepatanJam.JEDA
        val urut = ArrayList<KecepatanJam>()
        for (i in 0 until 4) { k = k.berikut(); urut.add(k) }
        assertEquals(listOf(KecepatanJam.X1, KecepatanJam.X2, KecepatanJam.X5, KecepatanJam.JEDA), urut)
    }
}
