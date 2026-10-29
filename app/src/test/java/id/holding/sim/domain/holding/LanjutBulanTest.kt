package id.holding.sim.domain.holding

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class LanjutBulanTest {
    private fun holdingUsaha(laba: Double) = Holding(
        nama = "A", negaraBasis = "ID", modalAwalRupiah = 1e9, kasRupiah = 1e9,
        usaha = listOf(PosisiUsaha(1, 5e8, 1e8, laba))
    )

    @Test fun majuTambahBulanDanKasDenganLaba() {
        val h = LanjutBulan.maju(holdingUsaha(2e7))
        assertEquals(1, h.bulan)
        assertEquals(1.02e9, h.kasRupiah, 1e-3)
    }

    @Test fun tanpaUsahaKasTetap() {
        val h = LanjutBulan.maju(Holding.mulai("A", "ID", 20))
        assertEquals(20e9, h.kasRupiah, 1e-3)
        assertEquals(1, h.bulan)
    }

    @Test fun labaNegatifMengurangiKas() {
        assertEquals(9.9e8, LanjutBulan.maju(holdingUsaha(-1e7)).kasRupiah, 1e-3)
    }

    @Test fun majuBanyakAkumulatif() {
        val h = LanjutBulan.majuBanyak(holdingUsaha(1e7), 12)
        assertEquals(12, h.bulan)
        assertEquals(1.12e9, h.kasRupiah, 1e-3)
        assertEquals(h, LanjutBulan.majuBanyak(h, 0))
    }

    @Test fun majuBanyakNegatifDitolak() {
        try {
            LanjutBulan.majuBanyak(holdingUsaha(0.0), -1)
            assertTrue(false)
        } catch (e: IllegalArgumentException) {
            assertTrue(true)
        }
    }

    @Test fun valuasiNaikSebesarLaba() {
        val awal = holdingUsaha(3e7)
        assertEquals(awal.valuasiRupiah + 3e7, LanjutBulan.maju(awal).valuasiRupiah, 1e-3)
    }
}

class RiwayatValuasiTest {
    @Test fun mulaiPunyaTitikAwal() {
        assertEquals(listOf(20e9), Holding.mulai("A", "ID", 20).riwayatValuasiRupiah)
    }

    @Test fun majuMenambahTitikValuasi() {
        val h = Holding(
            "A", "ID", 1e9, 1e9, 0.0, 0.0,
            listOf(PosisiUsaha(1, 5e8, 1e8, 2e7)), 0, listOf(1.5e9)
        )
        val b = LanjutBulan.majuBanyak(h, 2)
        assertEquals(listOf(1.5e9, 1.52e9, 1.54e9), b.riwayatValuasiRupiah)
    }

    @Test fun riwayatKosongDiawaliValuasiSaatIni() {
        val h = Holding("A", "ID", 1e9, 1e9)
        assertEquals(listOf(1e9, 1e9), LanjutBulan.maju(h).riwayatValuasiRupiah)
    }

    @Test fun riwayatDibatasi() {
        val panjang = List(Holding.RIWAYAT_MAKS) { it.toDouble() }
        val h = Holding("A", "ID", 1.0, 1.0, riwayatValuasiRupiah = panjang)
        val b = LanjutBulan.maju(h)
        assertEquals(Holding.RIWAYAT_MAKS, b.riwayatValuasiRupiah.size)
        assertEquals(1.0, b.riwayatValuasiRupiah.first(), 0.0)
    }
}

class PajakBulananTest {
    private fun h(laba: Double, rugi: Double = 0.0) = Holding(
        "A", "ID", 1e9, 1e9, 0.0, 0.0, listOf(PosisiUsaha(1, 5e8, 1e8, laba)), rugiFiskalRupiah = rugi
    )

    @Test fun pajakDipotongDariKas() {
        val b = LanjutBulan.maju(h(1e7), 22.0)
        assertEquals(1e9 + 7.8e6, b.kasRupiah, 1e-3)
        assertEquals(0.0, b.rugiFiskalRupiah, 0.0)
    }

    @Test fun rugiTidakDipajakiDanDibawa() {
        val b = LanjutBulan.maju(h(-1e7), 22.0)
        assertEquals(1e9 - 1e7, b.kasRupiah, 1e-3)
        assertEquals(1e7, b.rugiFiskalRupiah, 1e-3)
    }

    @Test fun rugiBawaanMengurangiLabaKenaPajak() {
        val b = LanjutBulan.maju(h(1.5e7, rugi = 1e7), 20.0) // kena 5e6 -> pajak 1e6
        assertEquals(1e9 + 1.4e7, b.kasRupiah, 1e-3)
        assertEquals(0.0, b.rugiFiskalRupiah, 1e-3)
    }

    @Test fun tarifNegatifDitolak() {
        try {
            LanjutBulan.maju(h(1e7), -1.0)
            assertTrue(false)
        } catch (e: IllegalArgumentException) {
            assertTrue(true)
        }
    }

    @Test fun majuBanyakMemakaiTarif() {
        val b = LanjutBulan.majuBanyak(h(1e7), 2, 10.0)
        assertEquals(1e9 + 1.8e7, b.kasRupiah, 1e-3)
    }
}
