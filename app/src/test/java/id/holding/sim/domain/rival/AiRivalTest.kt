package id.holding.sim.domain.rival

import id.holding.sim.data.Rival
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class AiRivalTest {
    private fun rival(
        id: Int = 1,
        margin: Double? = null,
        kap: Double? = null,
        utang: Double? = null
    ) = Rival(
        id = id, nama = "X", sektor = "s", negara = "US", status = "publik",
        arketipeUtama = "manufaktur", tanggalData = "2026-10", perkiraan = true,
        kapitalisasiUsdMiliar = kap, pendapatanUsdMiliar = null,
        marginBersihPct = margin, utangUsdMiliar = utang, sumberKapitalisasi = "", catatanAngka = ""
    )

    @Test fun marginNegatifBertahan() {
        assertEquals(KeputusanRival.BERTAHAN, AiRival.putuskan(rival(margin = -2.0), 1L))
    }

    @Test fun marginTinggiUtangRendahEkspansi() {
        val r = rival(margin = 30.0, kap = 100.0, utang = 10.0)
        assertEquals(KeputusanRival.EKSPANSI, AiRival.putuskan(r, 1L))
    }

    @Test fun utangTerlaluBesarBertahanMeskiMarginTinggi() {
        val r = rival(margin = 30.0, kap = 100.0, utang = 70.0)
        assertEquals(KeputusanRival.BERTAHAN, AiRival.putuskan(r, 1L))
    }

    @Test fun utangSedangTahan() {
        val r = rival(margin = 30.0, kap = 100.0, utang = 40.0)
        assertEquals(KeputusanRival.TAHAN, AiRival.putuskan(r, 1L))
    }

    @Test fun marginRendahTahan() {
        assertEquals(KeputusanRival.TAHAN, AiRival.putuskan(rival(margin = 8.0, kap = 100.0, utang = 10.0), 1L))
    }

    @Test fun dataKosongTahan() {
        assertEquals(KeputusanRival.TAHAN, AiRival.putuskan(rival(), 1L))
    }

    @Test fun utangKosongDilewatiBukanNol() {
        assertEquals(KeputusanRival.EKSPANSI, AiRival.putuskan(rival(margin = 30.0, kap = 100.0), 1L))
    }

    @Test fun ambangEkspansiDalamRentangDanDeterministik() {
        for (seed in 0L..49L) {
            val a = AiRival.ambangEkspansiPct(7, seed)
            assertTrue(a >= 13.0 && a <= 17.0)
            assertEquals(a, AiRival.ambangEkspansiPct(7, seed), 0.0)
        }
    }
}
