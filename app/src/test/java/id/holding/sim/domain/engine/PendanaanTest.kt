package id.holding.sim.domain.engine

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class PendanaanTest {
    private val daftar = listOf(
        UtangPerusahaan("Bank A", JenisUtang.PINJAMAN_BANK, 600_000_000.0, 10.0, 24),
        UtangPerusahaan("Obligasi 1", JenisUtang.OBLIGASI, 400_000_000.0, 7.0, 36),
        UtangPerusahaan("USD", JenisUtang.VALAS, 0.0, 5.0, 6)
    )

    @Test fun totalUtangMenjumlahSisaPokok() {
        assertEquals(1_000_000_000.0, Pendanaan.totalUtang(daftar), 1e-6)
    }

    @Test fun bebanBungaBulanan() {
        // 600jt x 10% + 400jt x 7% = 60jt + 28jt = 88jt / 12
        assertEquals(7_333_333.3333, Pendanaan.bebanBungaBulanan(daftar), 1e-3)
    }

    @Test fun bungaRataRataTertimbang() {
        assertEquals(8.8, Pendanaan.bungaRataRataPct(daftar), 1e-9)
    }

    @Test fun jatuhTempoTerdekatMengabaikanPokokNol() {
        assertEquals(24, Pendanaan.jatuhTempoTerdekatBulan(daftar))
    }

    @Test fun jatuhTempoDalamBatasBulan() {
        assertEquals(600_000_000.0, Pendanaan.jatuhTempoDalam(daftar, 24), 1e-6)
        assertEquals(0.0, Pendanaan.jatuhTempoDalam(daftar, 12), 1e-6)
        assertEquals(1_000_000_000.0, Pendanaan.jatuhTempoDalam(daftar, 36), 1e-6)
    }

    @Test fun daftarKosongSemuaNol() {
        assertEquals(0.0, Pendanaan.totalUtang(emptyList()), 0.0)
        assertEquals(0.0, Pendanaan.bebanBungaBulanan(emptyList()), 0.0)
        assertEquals(0.0, Pendanaan.bungaRataRataPct(emptyList()), 0.0)
        assertNull(Pendanaan.jatuhTempoTerdekatBulan(emptyList()))
    }

    @Test fun utangSudahJatuhTempoTidakDihitungSebagaiTerdekat() {
        val d = listOf(UtangPerusahaan("Lama", JenisUtang.PINJAMAN_BANK, 100.0, 5.0, 0))
        assertNull(Pendanaan.jatuhTempoTerdekatBulan(d))
    }
}
