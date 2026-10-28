package id.holding.sim.domain.engine

import org.junit.Assert.assertEquals
import org.junit.Test

class ObligasiKorporasiTest {
    private fun contoh() = ObligasiKorporasi.terbitkan(1_000_000_000.0, 6.0, 3.0, 5)

    @Test fun kuponAcuanDitambahPremi() {
        assertEquals(9.0, contoh().kuponPct, 1e-9)
    }

    @Test fun danaDiterimaSetelahBiayaPenerbitan() {
        val p = contoh()
        assertEquals(10_000_000.0, p.biayaPenerbitan, 1e-6)
        assertEquals(990_000_000.0, p.danaDiterima, 1e-6)
    }

    @Test fun jadwalPokokHanyaDiTahunTerakhir() {
        val j = ObligasiKorporasi.jadwalPembayaran(contoh())
        assertEquals(5, j.size)
        assertEquals(0.0, j[0].pelunasanPokok, 0.0)
        assertEquals(0.0, j[3].pelunasanPokok, 0.0)
        assertEquals(1_000_000_000.0, j[4].pelunasanPokok, 1e-6)
    }

    @Test fun kuponTahunanSamaSetiapTahun() {
        val j = ObligasiKorporasi.jadwalPembayaran(contoh())
        j.forEach { assertEquals(90_000_000.0, it.kupon, 1e-6) }
        assertEquals(1_090_000_000.0, j.last().total, 1e-6)
    }

    @Test fun biayaDanaEfektifLebihTinggiDariKupon() {
        assertEquals(9.246231156, ObligasiKorporasi.biayaDanaEfektifPct(contoh()), 1e-6)
    }

    @Test fun biayaPenerbitanNolBiayaDanaSamaDenganKupon() {
        val p = ObligasiKorporasi.terbitkan(1_000_000_000.0, 6.0, 3.0, 5, 0.0)
        assertEquals(9.0, ObligasiKorporasi.biayaDanaEfektifPct(p), 1e-9)
    }

    @Test(expected = IllegalArgumentException::class) fun tenorNolDitolak() {
        ObligasiKorporasi.terbitkan(1_000_000_000.0, 6.0, 3.0, 0)
    }

    @Test(expected = IllegalArgumentException::class) fun nilaiPokokNolDitolak() {
        ObligasiKorporasi.terbitkan(0.0, 6.0, 3.0, 5)
    }
}
