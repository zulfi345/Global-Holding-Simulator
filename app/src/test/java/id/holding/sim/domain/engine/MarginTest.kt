package id.holding.sim.domain.engine

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class MarginTest {
    @Test fun pinjamanDariLeverage() {
        assertEquals(200.0, Margin.pinjamanDiperlukan(100.0, 3.0), 1e-9)
        assertEquals(0.0, Margin.pinjamanDiperlukan(100.0, 1.0), 1e-9)
    }

    @Test(expected = IllegalArgumentException::class) fun leverageDiBawahSatuDitolak() {
        Margin.pinjamanDiperlukan(100.0, 0.5)
    }

    @Test fun rasioMarginAwalLeverageTiga() {
        assertEquals(33.3333333, Margin.rasioMarginPct(300.0, 200.0), 1e-6)
    }

    @Test fun belumMarginCallSaatRasioDiAtasMaintenance() {
        assertFalse(Margin.kenaMarginCall(270.0, 200.0)) // rasio 25.93%
    }

    @Test fun marginCallSaatRasioDiBawahMaintenance() {
        assertTrue(Margin.kenaMarginCall(260.0, 200.0)) // rasio 23.08%
    }

    @Test fun rasioTepatMaintenanceAman() {
        assertFalse(Margin.kenaMarginCall(400.0, 300.0)) // rasio 25%
    }

    @Test fun danaTambahanMengembalikanKeMaintenance() {
        assertEquals(5.0, Margin.danaTambahan(260.0, 200.0), 1e-9) // 0.25*260 - 60
        assertEquals(0.0, Margin.danaTambahan(300.0, 200.0), 1e-9)
    }

    @Test fun likuidasiPaksaHanyaSaatRasioDiBawahAmbang() {
        assertFalse(Margin.harusDilikuidasi(230.0, 200.0)) // 13.04%
        assertTrue(Margin.harusDilikuidasi(215.0, 200.0)) // 6.98%
    }

    @Test fun likuidasiMenyisakanTunaiBilaHasilJualCukup() {
        val h = Margin.likuidasi(215.0, 200.0, 1.0) // biaya 2.15, sisa 12.85
        assertEquals(12.85, h.diterima, 1e-9)
        assertEquals(0.0, h.kekurangan, 1e-9)
    }

    @Test fun likuidasiMenyisakanKekuranganBilaHasilJualKurang() {
        val h = Margin.likuidasi(190.0, 200.0, 1.0) // sisa 190-1.9-200 = -11.9
        assertEquals(0.0, h.diterima, 1e-9)
        assertEquals(11.9, h.kekurangan, 1e-9)
    }

    @Test fun bungaPinjamanBulanan() {
        assertEquals(1_000.0, Margin.bungaPinjamanBulanan(200_000.0, 6.0), 1e-9)
    }
}
