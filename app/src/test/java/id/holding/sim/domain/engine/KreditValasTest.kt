package id.holding.sim.domain.engine

import org.junit.Assert.assertEquals
import org.junit.Test

class KreditValasTest {
    @Test fun danaRupiahDariPokokUsd() {
        assertEquals(16_000_000_000.0, KreditValas.danaRupiah(1_000_000.0, 16_000.0), 1e-3)
    }

    @Test fun cicilanRupiahPadaKursAwal() {
        assertEquals(1_369_719_708.6155, KreditValas.cicilanRupiah(1_000_000.0, 5.0, 12, 16_000.0), 1e-2)
    }

    @Test fun rupiahMelemahMenaikkanCicilanRupiah() {
        val awal = KreditValas.cicilanRupiah(1_000_000.0, 5.0, 12, 16_000.0)
        val lemah = KreditValas.cicilanRupiah(1_000_000.0, 5.0, 12, 17_000.0)
        assertEquals(awal * 17_000.0 / 16_000.0, lemah, 1e-3)
    }

    @Test fun cicilanPerBulanMengikutiKursTiapBulan() {
        val j = Kredit.jadwal(1_200_000.0, 0.0, 3) // 400.000 USD per bulan
        val hasil = KreditValas.cicilanRupiahPerBulan(j, listOf(16_000.0, 16_500.0, 17_000.0))
        assertEquals(6_400_000_000.0, hasil[0], 1e-3)
        assertEquals(6_600_000_000.0, hasil[1], 1e-3)
        assertEquals(6_800_000_000.0, hasil[2], 1e-3)
    }

    @Test(expected = IllegalArgumentException::class) fun jumlahKursTidakCocokDitolak() {
        val j = Kredit.jadwal(1_200_000.0, 0.0, 3)
        KreditValas.cicilanRupiahPerBulan(j, listOf(16_000.0))
    }

    @Test fun rugiKursSaatRupiahMelemah() {
        assertEquals(-500_000_000.0, KreditValas.labaRugiSelisihKurs(500_000.0, 16_000.0, 17_000.0), 1e-3)
    }

    @Test fun labaKursSaatRupiahMenguat() {
        assertEquals(250_000_000.0, KreditValas.labaRugiSelisihKurs(500_000.0, 16_000.0, 15_500.0), 1e-3)
    }

    @Test(expected = IllegalArgumentException::class) fun kursNolDitolak() {
        KreditValas.danaRupiah(1_000_000.0, 0.0)
    }
}
