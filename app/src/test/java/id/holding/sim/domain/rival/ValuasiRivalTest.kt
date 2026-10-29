package id.holding.sim.domain.rival

import id.holding.sim.data.Rival
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ValuasiRivalTest {
    private fun rival(
        arketipe: String = "teknologi",
        kap: Double? = null,
        pendapatan: Double? = null
    ) = Rival(
        id = 1, nama = "X", sektor = "s", negara = "US", status = "publik",
        arketipeUtama = arketipe, tanggalData = "2026-10", perkiraan = true,
        kapitalisasiUsdMiliar = kap, pendapatanUsdMiliar = pendapatan,
        marginBersihPct = null, utangUsdMiliar = null, sumberKapitalisasi = "", catatanAngka = ""
    )

    @Test fun kapitalisasiDipakaiBilaAda() {
        val n = ValuasiRival.nilai(rival(kap = 4870.0, pendapatan = 416.0))!!
        assertEquals(4870.0, n.nilaiUsdMiliar, 1e-9)
        assertEquals(SumberNilai.KAPITALISASI, n.sumber)
    }

    @Test fun tanpaKapitalisasiPakaiMultiplePendapatan() {
        val n = ValuasiRival.nilai(rival(arketipe = "pertanian", pendapatan = 160.0))!!
        assertEquals(96.0, n.nilaiUsdMiliar, 1e-9)
        assertEquals(SumberNilai.MULTIPLE_PENDAPATAN, n.sumber)
    }

    @Test fun tanpaKapitalisasiDanPendapatanNull() {
        assertNull(ValuasiRival.nilai(rival(arketipe = "manufaktur")))
    }

    @Test fun semuaSembilanArketipeAdaMultiple() {
        val daftar = listOf(
            "tambang_energi", "pertanian", "manufaktur", "teknologi", "finansial",
            "logistik", "properti_konstruksi", "ritel_jasa", "layanan_regulasi"
        )
        for (a in daftar) assertEquals(true, ValuasiRival.multiplePendapatan(a) > 0.0)
    }

    @Test(expected = IllegalArgumentException::class)
    fun arketipeTakDikenalDitolak() {
        ValuasiRival.multiplePendapatan("tidak_ada")
    }
}
