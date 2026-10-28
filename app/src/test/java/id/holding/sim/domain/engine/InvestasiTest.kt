package id.holding.sim.domain.engine

import org.junit.Assert.assertEquals
import org.junit.Test

class InvestasiTest {
    @Test fun beliBulatkanKeLotDanHitungBiaya() {
        val h = Investasi.beli(hargaPerUnit = 1000.0, ukuranLot = 100, unitDiminta = 250, biayaBrokerPct = 0.15, pajakTransaksiPct = 0.0)
        assertEquals(2, h.jumlahLot)
        assertEquals(200, h.jumlahUnit)
        assertEquals(200_000.0, h.nilaiKotor, 1e-9)
        assertEquals(300.0, h.biayaBroker, 1e-9)
        assertEquals(0.0, h.pajakTransaksi, 1e-9)
        assertEquals(200_300.0, h.nilaiBersih, 1e-9)
    }

    @Test fun jualKurangiBiayaDanPajakDariHasil() {
        val h = Investasi.jual(hargaPerUnit = 1000.0, ukuranLot = 100, unitDiminta = 200, biayaBrokerPct = 0.15, pajakTransaksiPct = 0.1)
        assertEquals(200_000.0, h.nilaiKotor, 1e-9)
        assertEquals(300.0, h.biayaBroker, 1e-9)
        assertEquals(200.0, h.pajakTransaksi, 1e-9)
        assertEquals(199_500.0, h.nilaiBersih, 1e-9)
    }

    @Test fun ukuranLot1UntukBursaNonIdx() {
        val h = Investasi.beli(hargaPerUnit = 50.0, ukuranLot = 1, unitDiminta = 7, biayaBrokerPct = 0.0)
        assertEquals(7, h.jumlahLot)
        assertEquals(7, h.jumlahUnit)
        assertEquals(350.0, h.nilaiKotor, 1e-9)
    }

    @Test fun unitKurangDari1LotDiabaikan() {
        val h = Investasi.beli(hargaPerUnit = 1000.0, ukuranLot = 100, unitDiminta = 50)
        assertEquals(0, h.jumlahLot)
        assertEquals(0, h.jumlahUnit)
        assertEquals(0.0, h.nilaiKotor, 1e-9)
        assertEquals(0.0, h.nilaiBersih, 1e-9)
    }
}
