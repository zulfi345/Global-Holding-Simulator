package id.holding.sim.domain.engine

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class SpreadBidAskTest {
    @Test fun hargaBeliDiAtasHargaTengah() {
        assertEquals(1010.0, SpreadBidAsk.hargaBeli(1000.0, 2.0), 1e-9)
    }

    @Test fun hargaJualDiBawahHargaTengah() {
        assertEquals(990.0, SpreadBidAsk.hargaJual(1000.0, 2.0), 1e-9)
    }

    @Test fun spreadNolHargaBeliSamaJual() {
        assertEquals(1000.0, SpreadBidAsk.hargaBeli(1000.0, 0.0), 1e-9)
        assertEquals(1000.0, SpreadBidAsk.hargaJual(1000.0, 0.0), 1e-9)
    }

    @Test fun selisihBeliJualSamaDenganSpreadPct() {
        val beli = SpreadBidAsk.hargaBeli(1000.0, 4.0)
        val jual = SpreadBidAsk.hargaJual(1000.0, 4.0)
        assertEquals(40.0, beli - jual, 1e-9) // 4% dari 1000
    }

    @Test fun hargaBeliSelaluLebihBesarDariHargaJualUntukSpreadPositif() {
        assertTrue(SpreadBidAsk.hargaBeli(500.0, 1.5) > SpreadBidAsk.hargaJual(500.0, 1.5))
    }
}
