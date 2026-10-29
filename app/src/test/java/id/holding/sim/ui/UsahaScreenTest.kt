package id.holding.sim.ui

import id.holding.sim.data.Usaha
import org.junit.Assert.assertEquals
import org.junit.Test

class UsahaScreenTest {
    private fun usaha(id: Int, modalUsd: Double) = Usaha(
        id = id, nama = "U$id", kelompok = "A", arketipe = "manufaktur", modalMinUsd = modalUsd,
        hppPct = 50.0, biayaOperasiPct = 20.0, penyusutanPct = 10.0, pekerjaMin = 1,
        izin = emptyList(), risiko = emptyList(), kpi = emptyList(), tanggalData = "2026-10", perkiraan = true
    )

    @Test fun terjangkauDuluLaluYangMahal() {
        val daftar = listOf(usaha(1, 50_000_000.0), usaha(2, 200_000.0), usaha(3, 100_000.0))
        val urut = susunUsaha(daftar, 20e9).map { it.id }
        assertEquals(listOf(3, 2, 1), urut) // 3 & 2 terjangkau (urut harga), 1 tidak
    }
}
