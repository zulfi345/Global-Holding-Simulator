package id.holding.sim.domain.engine

import org.junit.Assert.assertEquals
import org.junit.Test

class ObligasiTest {
    @Test fun kuponTahunanDariNilaiParDanPct() {
        assertEquals(60_000.0, Obligasi.kuponTahunan(1_000_000.0, 6.0), 1e-9)
    }

    @Test fun kuponPerPeriodeSemesteran() {
        assertEquals(30_000.0, Obligasi.kuponPerPeriode(1_000_000.0, 6.0, 2), 1e-9)
    }

    @Test fun kuponPerPeriodeKuartalan() {
        assertEquals(15_000.0, Obligasi.kuponPerPeriode(1_000_000.0, 6.0, 4), 1e-9)
    }

    @Test fun currentYieldSamaDenganKuponPctSaatHargaSamaDenganPar() {
        val kupon = Obligasi.kuponTahunan(1_000_000.0, 6.0)
        assertEquals(6.0, Obligasi.currentYieldPct(kupon, 1_000_000.0), 1e-9)
    }

    @Test fun currentYieldLebihTinggiSaatHargaDiskon() {
        val kupon = Obligasi.kuponTahunan(1_000_000.0, 6.0) // 60000
        assertEquals(6.666666667, Obligasi.currentYieldPct(kupon, 900_000.0), 1e-6) // diskon -> yield naik
    }

    @Test fun currentYieldLebihRendahSaatHargaPremium() {
        val kupon = Obligasi.kuponTahunan(1_000_000.0, 6.0) // 60000
        assertEquals(5.454545455, Obligasi.currentYieldPct(kupon, 1_100_000.0), 1e-6) // premium -> yield turun
    }
}
