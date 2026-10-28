package id.holding.sim.domain.engine

import org.junit.Assert.assertEquals
import org.junit.Test

class CovenantTest {
    @Test fun semuaMemenuhiTidakAdaPelanggaran() {
        assertEquals(emptyList<Pelanggaran>(), CovenantEngine.periksa(1.5, 4.0, 1.5))
    }

    @Test fun tepatDiBatasAman() {
        assertEquals(emptyList<Pelanggaran>(), CovenantEngine.periksa(3.0, 2.0, 1.0))
    }

    @Test fun derMelebihiMaksimum() {
        assertEquals(listOf(Pelanggaran.DER), CovenantEngine.periksa(3.1, 4.0, 1.5))
    }

    @Test fun coverageDanCurrentRatioDiBawahMinimum() {
        assertEquals(
            listOf(Pelanggaran.COVERAGE, Pelanggaran.CURRENT_RATIO),
            CovenantEngine.periksa(1.0, 1.9, 0.9)
        )
    }

    @Test fun ekuitasNegatifDerTakHinggaMelanggar() {
        val der = PremiRisiko.der(100.0, -10.0)
        assertEquals(listOf(Pelanggaran.DER), CovenantEngine.periksa(der, 4.0, 1.5))
    }

    @Test fun covenantKustomDipakai() {
        val ketat = Covenant(derMaks = 1.0, coverageMin = 5.0, currentRatioMin = 2.0)
        assertEquals(3, CovenantEngine.periksa(1.5, 4.0, 1.5, ketat).size)
    }

    @Test fun akibatDariJumlahPelanggaran() {
        assertEquals(AkibatCovenant.AMAN, CovenantEngine.akibat(0))
        assertEquals(AkibatCovenant.BUNGA_NAIK, CovenantEngine.akibat(1))
        assertEquals(AkibatCovenant.WAJIB_LUNAS, CovenantEngine.akibat(2))
        assertEquals(AkibatCovenant.WAJIB_LUNAS, CovenantEngine.akibat(3))
    }

    @Test fun bungaNaikHanyaUntukAkibatBungaNaik() {
        assertEquals(10.0, CovenantEngine.bungaSetelahPemeriksaan(9.0, AkibatCovenant.BUNGA_NAIK), 1e-9)
        assertEquals(9.0, CovenantEngine.bungaSetelahPemeriksaan(9.0, AkibatCovenant.AMAN), 1e-9)
        assertEquals(9.0, CovenantEngine.bungaSetelahPemeriksaan(9.0, AkibatCovenant.WAJIB_LUNAS), 1e-9)
    }

    @Test(expected = IllegalArgumentException::class) fun jumlahPelanggaranNegatifDitolak() {
        CovenantEngine.akibat(-1)
    }
}
