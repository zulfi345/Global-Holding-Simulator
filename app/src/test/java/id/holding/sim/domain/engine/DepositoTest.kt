package id.holding.sim.domain.engine

import org.junit.Assert.assertEquals
import org.junit.Test

class DepositoTest {
    @Test fun bungaBrutoDuaBelasBulanSamaDenganBungaTahunan() {
        assertEquals(50_000.0, Deposito.bungaBruto(1_000_000.0, 5.0, 12), 1e-9)
    }

    @Test fun bungaBrutoEnamBulanSetengahTahun() {
        assertEquals(25_000.0, Deposito.bungaBruto(1_000_000.0, 5.0, 6), 1e-9)
    }

    @Test fun jatuhTempoKurangiPajak() {
        val h = Deposito.jatuhTempo(1_000_000.0, 5.0, 12, 20.0)
        assertEquals(10_000.0, h.pajakBunga, 1e-9)
        assertEquals(1_040_000.0, h.totalDiterima, 1e-9)
        assertEquals(0.0, h.penalti, 1e-9)
    }

    @Test fun pencairanDiniKenaPenaltiDanBungaSebagian() {
        // 6 bulan: bruto 25000, pajak 20% = 5000, penalti 50% = 12500 -> 1_007_500
        val h = Deposito.pencairanDini(1_000_000.0, 5.0, 6, 12, 20.0, 50.0)
        assertEquals(12_500.0, h.penalti, 1e-9)
        assertEquals(1_007_500.0, h.totalDiterima, 1e-9)
    }

    @Test fun pencairanDiniPokokTidakPernahBerkurangSelamaPenaltiAtauPajak100Persen() {
        val h = Deposito.pencairanDini(1_000_000.0, 5.0, 3, 12, 0.0, 100.0)
        assertEquals(1_000_000.0, h.totalDiterima, 1e-9)
    }

    @Test(expected = IllegalArgumentException::class) fun bulanBerjalanMelebihiTenorDitolak() {
        Deposito.pencairanDini(1_000_000.0, 5.0, 13, 12, 20.0, 50.0)
    }
}
