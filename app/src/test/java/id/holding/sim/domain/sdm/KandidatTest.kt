package id.holding.sim.domain.sdm

import org.junit.Assert.assertEquals
import org.junit.Test

class KandidatTest {
    private val k = Kandidat(
        id = "k1", nama = "Uji",
        kemampuan = mapOf(Bidang.KEUANGAN to 80, Bidang.PIMPINAN to 60, Bidang.OPERASI to 50, Bidang.TEKNOLOGI to 40, Bidang.SDM to 30),
        gajiBulanan = 1000.0
    )

    @Test fun bobotSetiapJabatanBerjumlah100() {
        for (j in Jabatan.values()) {
            assertEquals(100.0, j.bobot.values.sum(), 1e-9)
            assertEquals(Bidang.values().size, j.bobot.size)
        }
    }

    @Test fun skorCfo() {
        assertEquals(68.5, k.skorJabatan(Jabatan.CFO), 1e-9)
    }

    @Test fun nilaiDibatasiDanBidangKosongNol() {
        val x = Kandidat("k2", "Batas", mapOf(Bidang.SDM to 150, Bidang.OPERASI to -5), 500.0)
        assertEquals(100, x.nilai(Bidang.SDM))
        assertEquals(0, x.nilai(Bidang.OPERASI))
        assertEquals(0, x.nilai(Bidang.TEKNOLOGI))
    }

    @Test fun biayaDanMasaRekrutmen() {
        assertEquals(2400.0, Rekrutmen.biayaRekrutmen(1000.0, true), 1e-9)
        assertEquals(0.0, Rekrutmen.biayaRekrutmen(1000.0, false), 1e-9)
        assertEquals(1, Rekrutmen.masaPencarianBulan(true))
        assertEquals(3, Rekrutmen.masaPencarianBulan(false))
    }
}
