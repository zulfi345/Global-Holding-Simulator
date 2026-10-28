package id.holding.sim.domain.engine

import org.junit.Assert.assertEquals
import org.junit.Test

class KreditTest {
    @Test fun bungaPinjamanAcuanDitambahPremi() {
        assertEquals(9.5, Kredit.bungaPinjamanPct(6.0, 3.5), 1e-9)
    }

    @Test fun cicilanAnuitasDuaBelasBulan() {
        assertEquals(10_661_854.6414, Kredit.cicilanBulanan(120_000_000.0, 12.0, 12), 1e-3)
    }

    @Test fun cicilanBungaNolMembagiRata() {
        assertEquals(10_000_000.0, Kredit.cicilanBulanan(120_000_000.0, 0.0, 12), 1e-9)
    }

    @Test fun cicilanDuaPuluhEmpatBulanBunga6Persen() {
        assertEquals(5_318_473.2303, Kredit.cicilanBulanan(120_000_000.0, 6.0, 24), 1e-3)
    }

    @Test fun barisPertamaBungaDariPokokPenuh() {
        val j = Kredit.jadwal(120_000_000.0, 12.0, 12)
        assertEquals(1_200_000.0, j[0].bunga, 1e-6)
        assertEquals(9_461_854.6414, j[0].pokokDibayar, 1e-3)
        assertEquals(110_538_145.3586, j[0].sisaPinjaman, 1e-3)
    }

    @Test fun sisaPinjamanAkhirTepatNolDanJumlahBarisSamaDenganTenor() {
        val j = Kredit.jadwal(120_000_000.0, 12.0, 12)
        assertEquals(12, j.size)
        assertEquals(0.0, j.last().sisaPinjaman, 0.0)
    }

    @Test fun totalBungaSelamaTenor() {
        val j = Kredit.jadwal(120_000_000.0, 12.0, 12)
        assertEquals(7_942_255.6968, Kredit.totalBunga(j), 1e-2)
    }

    @Test fun totalPokokDibayarSamaDenganPinjaman() {
        val j = Kredit.jadwal(120_000_000.0, 12.0, 12)
        assertEquals(120_000_000.0, j.sumOf { it.pokokDibayar }, 1e-2)
    }

    @Test(expected = IllegalArgumentException::class) fun tenorNolDitolak() {
        Kredit.cicilanBulanan(1_000_000.0, 10.0, 0)
    }
}
