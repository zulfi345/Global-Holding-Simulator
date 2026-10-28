package id.holding.sim.domain.sdm

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class OtomatisasiTest {
    private val k = Kandidat(
        id = "k1", nama = "Uji",
        kemampuan = mapOf(Bidang.KEUANGAN to 80, Bidang.PIMPINAN to 60, Bidang.OPERASI to 50, Bidang.TEKNOLOGI to 40, Bidang.SDM to 30),
        gajiBulanan = 1000.0
    )
    private val terisi = mapOf(Jabatan.CFO to k, Jabatan.CEO to k)

    @Test fun jabatanKosongTidakAktif() {
        assertFalse(Otomatisasi.aktif(Jabatan.CTO, terisi))
        assertTrue(Otomatisasi.aktif(Jabatan.CFO, terisi))
    }

    @Test fun timpaPerusahaanMengalahkanAturanHolding() {
        val holding = mapOf(Jabatan.CFO to false)
        assertFalse(Otomatisasi.aktif(Jabatan.CFO, terisi, holding))
        assertTrue(Otomatisasi.aktif(Jabatan.CFO, terisi, holding, mapOf(Jabatan.CFO to true)))
        assertFalse(Otomatisasi.aktif(Jabatan.CEO, terisi, emptyMap(), mapOf(Jabatan.CEO to false)))
    }

    @Test fun efisiensiRataRataJabatanAktif() {
        assertEquals(0.625, Otomatisasi.efisiensi(terisi), 1e-9)
        assertEquals(0.685, Otomatisasi.efisiensi(terisi, emptyMap(), mapOf(Jabatan.CEO to false)), 1e-9)
        assertEquals(0.0, Otomatisasi.efisiensi(emptyMap()), 1e-9)
    }

    @Test fun faktorBiayaOperasi() {
        assertEquals(0.9375, Otomatisasi.faktorBiayaOperasi(0.625), 1e-9)
        assertEquals(0.9, Otomatisasi.faktorBiayaOperasi(1.0), 1e-9)
        assertEquals(1.0, Otomatisasi.faktorBiayaOperasi(0.0), 1e-9)
        assertEquals(0.9, Otomatisasi.faktorBiayaOperasi(5.0), 1e-9)
    }
}
