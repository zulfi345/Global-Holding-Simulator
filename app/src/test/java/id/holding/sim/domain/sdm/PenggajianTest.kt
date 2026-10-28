package id.holding.sim.domain.sdm

import id.holding.sim.data.Negara
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class PenggajianTest {
    private val negara = Negara(
        kode = "XX", nama = "Uji", mataUang = "XXX", pdbUsdMiliar = 1.0, pertumbuhanPct = 1.0,
        inflasiPct = 1.0, bungaAcuanPct = 1.0, kursPerUsd = 1.0, pphBadanPct = 20.0, ppnPct = 10.0,
        pajakDividenPct = 10.0, upahMinBulananUsd = 170.0, populasiJuta = 1.0,
        tanggalData = "2026-10", perkiraan = true
    )

    @Test fun upahMinimum() {
        assertTrue(Penggajian.memenuhiUpahMinimum(170.0, negara))
        assertFalse(Penggajian.memenuhiUpahMinimum(169.9, negara))
        assertEquals(170.0, Penggajian.sesuaikanKeUpahMinimum(100.0, negara), 1e-9)
        assertEquals(300.0, Penggajian.sesuaikanKeUpahMinimum(300.0, negara), 1e-9)
    }

    @Test fun totalBiayaKaryawan() {
        assertEquals(1100.0, Penggajian.totalBiayaKaryawan(1000.0), 1e-9)
        assertEquals(1250.0, Penggajian.totalBiayaKaryawan(1000.0, 25.0), 1e-9)
    }

    @Test fun pesangon() {
        assertEquals(3000.0, Penggajian.pesangon(1000.0, 3.0), 1e-9)
        assertEquals(1500.0, Penggajian.pesangon(1000.0, 3.0, 0.5), 1e-9)
        assertEquals(0.0, Penggajian.pesangon(1000.0, -2.0), 1e-9)
    }
}
