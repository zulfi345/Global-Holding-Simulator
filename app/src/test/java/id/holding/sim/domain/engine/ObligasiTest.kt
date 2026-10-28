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

    @Test fun ytmSamaDenganKuponPctSaatHargaSamaDenganPar() {
        assertEquals(6.0, Obligasi.ytmPerkiraanPct(60_000.0, 1_000_000.0, 1_000_000.0, 5.0), 1e-9)
    }

    @Test fun ytmLebihTinggiSaatDiskon() {
        assertEquals(8.421052632, Obligasi.ytmPerkiraanPct(60_000.0, 1_000_000.0, 900_000.0, 5.0), 1e-6)
    }

    @Test fun ytmLebihRendahSaatPremium() {
        assertEquals(3.809523810, Obligasi.ytmPerkiraanPct(60_000.0, 1_000_000.0, 1_100_000.0, 5.0), 1e-6)
    }

    @Test(expected = IllegalArgumentException::class) fun ytmSisaTahunNolDitolak() {
        Obligasi.ytmPerkiraanPct(60_000.0, 1_000_000.0, 900_000.0, 0.0)
    }

    @Test fun hargaDariYtmSamaDenganParSaatYtmSamaDenganKupon() {
        assertEquals(1_000_000.0, Obligasi.hargaDariYtm(60_000.0, 1_000_000.0, 6.0, 3), 1e-4)
    }

    @Test fun hargaNolKuponDiskonDariPar() {
        assertEquals(680_583.197, Obligasi.hargaDariYtm(0.0, 1_000_000.0, 8.0, 5), 1e-2)
    }

    @Test fun durasiMacaulayObligasiTigaTahun() {
        assertEquals(2.833392666, Obligasi.durasiMacaulay(60_000.0, 1_000_000.0, 6.0, 3), 1e-6)
    }

    @Test fun durasiNolKuponSamaDenganSisaTahun() {
        assertEquals(5.0, Obligasi.durasiMacaulay(0.0, 1_000_000.0, 8.0, 5), 1e-9)
    }

    @Test fun durasiModifikasiLebihKecilDariMacaulay() {
        assertEquals(2.673011949, Obligasi.durasiModifikasi(60_000.0, 1_000_000.0, 6.0, 3), 1e-6)
    }
}
