package id.holding.sim.domain.engine

import org.junit.Assert.assertEquals
import org.junit.Test

class GagalBayarTest {
    @Test fun dendaDuaPersenDariCicilanTertunggak() {
        assertEquals(100_000.0, GagalBayar.dendaKeterlambatan(5_000_000.0), 1e-9)
    }

    @Test fun tanpaTunggakanStatusLancar() {
        assertEquals(StatusKredit.LANCAR, GagalBayar.status(0))
    }

    @Test fun menunggakDiBawahBatas() {
        assertEquals(StatusKredit.MENUNGGAK, GagalBayar.status(2))
    }

    @Test fun mencapaiBatasGagalBayar() {
        assertEquals(StatusKredit.GAGAL_BAYAR, GagalBayar.status(3))
        assertEquals(StatusKredit.GAGAL_BAYAR, GagalBayar.status(10))
    }

    @Test(expected = IllegalArgumentException::class) fun bulanMenunggakNegatifDitolak() {
        GagalBayar.status(-1)
    }

    @Test fun restrukturisasiMenambahBiayaKePokok() {
        val h = GagalBayar.restrukturisasi(100_000_000.0, 12, 24, 8.0, 1.0)
        assertEquals(1_000_000.0, h.biayaRestrukturisasi, 1e-6)
        assertEquals(101_000_000.0, h.pokokBaru, 1e-6)
    }

    @Test fun restrukturisasiCicilanBaruDariPmt() {
        val h = GagalBayar.restrukturisasi(100_000_000.0, 12, 24, 8.0, 1.0)
        assertEquals(4_567_956.437, h.cicilanBaru, 1e-2)
    }

    @Test fun restrukturisasiPerpanjangTenorMenurunkanCicilan() {
        val sebelum = Kredit.cicilanBulanan(100_000_000.0, 8.0, 12)
        val sesudah = GagalBayar.restrukturisasi(100_000_000.0, 12, 24, 8.0, 1.0).cicilanBaru
        assertEquals(true, sesudah < sebelum)
    }

    @Test(expected = IllegalArgumentException::class) fun tenorBaruTidakLebihPanjangDitolak() {
        GagalBayar.restrukturisasi(100_000_000.0, 12, 12, 8.0)
    }
}
