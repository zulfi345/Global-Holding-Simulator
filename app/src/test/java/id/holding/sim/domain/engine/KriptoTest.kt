package id.holding.sim.domain.engine

import org.junit.Assert.assertEquals
import org.junit.Test

class KriptoTest {
    @Test fun beliPecahanDariDanaKecil() {
        // dana 1_000_000, biaya 0.5% = 5_000, untuk aset 995_000 / 100_000_000 = 0.00995
        val h = Kripto.beliDenganDana(1_000_000.0, 100_000_000.0)
        assertEquals(0.00995, h.jumlahUnit, 1e-12)
        assertEquals(5_000.0, h.biaya, 1e-9)
        assertEquals(1_000_000.0, h.nilaiBersih, 1e-9)
    }

    @Test fun beliDenganBiayaJaringanTetap() {
        val h = Kripto.beliDenganDana(1_000_000.0, 100_000_000.0, 0.0, 10_000.0)
        assertEquals(0.0099, h.jumlahUnit, 1e-12)
    }

    @Test fun beliDanaTidakCukupBiayaJaringanMenghasilkanNol() {
        val h = Kripto.beliDenganDana(5_000.0, 100_000_000.0, 0.0, 10_000.0)
        assertEquals(0.0, h.jumlahUnit, 0.0)
        assertEquals(0.0, h.nilaiBersih, 0.0)
    }

    @Test fun jualKurangiBiaya() {
        // 0.5 unit x 100_000_000 = 50_000_000, biaya 0.5% = 250_000
        val h = Kripto.jual(0.5, 100_000_000.0)
        assertEquals(250_000.0, h.biaya, 1e-6)
        assertEquals(49_750_000.0, h.nilaiBersih, 1e-6)
    }

    @Test fun jualNilaiBersihTidakNegatif() {
        val h = Kripto.jual(0.00001, 100_000_000.0, 0.5, 10_000.0)
        assertEquals(0.0, h.nilaiBersih, 0.0)
    }

    @Test(expected = IllegalArgumentException::class) fun hargaNolDitolak() {
        Kripto.beliDenganDana(1_000.0, 0.0)
    }
}
