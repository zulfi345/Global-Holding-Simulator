package id.holding.sim.domain.rival

import id.holding.sim.data.Rival
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class SimulasiRivalTest {
    private fun rival(id: Int = 1, margin: Double? = null, kap: Double? = null, utang: Double? = null) = Rival(
        id = id, nama = "X", sektor = "s", negara = "US", status = "publik",
        arketipeUtama = "manufaktur", tanggalData = "2026-10", perkiraan = true,
        kapitalisasiUsdMiliar = kap, pendapatanUsdMiliar = null,
        marginBersihPct = margin, utangUsdMiliar = utang, sumberKapitalisasi = "", catatanAngka = ""
    )

    @Test fun awalDariValuasiDanNullBilaTakAdaData() {
        assertEquals(100.0, SimulasiRival.awal(rival(kap = 100.0))!!.nilaiUsdMiliar, 1e-9)
        assertNull(SimulasiRival.awal(rival()))
    }

    @Test fun rivalEkspansiTumbuhSekitarSatuPersen() {
        val r = rival(margin = 30.0, kap = 100.0, utang = 10.0)
        val s = SimulasiRival.tickBulan(StateRival(1, 100.0), r, 1L, 1)
        assertTrue(s.nilaiUsdMiliar >= 100.7 && s.nilaiUsdMiliar <= 101.3)
    }

    @Test fun rivalBertahanTurunTipis() {
        val r = rival(margin = -5.0, kap = 100.0)
        val s = SimulasiRival.tickBulan(StateRival(1, 100.0), r, 1L, 1)
        assertTrue(s.nilaiUsdMiliar >= 99.5 && s.nilaiUsdMiliar <= 100.1)
    }

    @Test fun deterministikUntukSeedDanBulanSama() {
        val r = rival(margin = 30.0, kap = 100.0)
        val a = SimulasiRival.tickBulan(StateRival(1, 100.0), r, 7L, 3)
        val b = SimulasiRival.tickBulan(StateRival(1, 100.0), r, 7L, 3)
        assertEquals(a.nilaiUsdMiliar, b.nilaiUsdMiliar, 0.0)
    }

    @Test fun stateTidakAktifTidakBerubah() {
        val s = StateRival(1, 50.0, aktif = false)
        assertEquals(s, SimulasiRival.tickBulan(s, rival(margin = 30.0, kap = 100.0), 1L, 1))
    }

    @Test(expected = IllegalArgumentException::class)
    fun idTidakCocokDitolak() {
        SimulasiRival.tickBulan(StateRival(2, 100.0), rival(id = 1, kap = 100.0), 1L, 1)
    }

    @Test fun akuisisiBerhasilNilaiPembeliKurangiPremi() {
        val h = SimulasiRival.akuisisiAntarRival(
            StateRival(1, 1000.0), StateRival(2, 50.0), KeputusanRival.EKSPANSI, 30.0
        )!!
        assertEquals(65.0, h.hargaUsdMiliar, 1e-9)
        assertEquals(985.0, h.pembeli.nilaiUsdMiliar, 1e-9)
        assertFalse(h.target.aktif)
    }

    @Test fun akuisisiGagalBilaMelebihiKapasitasAtauBukanEkspansi() {
        assertNull(SimulasiRival.akuisisiAntarRival(StateRival(1, 100.0), StateRival(2, 50.0), KeputusanRival.EKSPANSI, 30.0))
        assertNull(SimulasiRival.akuisisiAntarRival(StateRival(1, 1000.0), StateRival(2, 50.0), KeputusanRival.TAHAN, 30.0))
        assertNull(SimulasiRival.akuisisiAntarRival(StateRival(1, 1000.0), StateRival(2, 50.0, aktif = false), KeputusanRival.EKSPANSI, 30.0))
    }

    @Test(expected = IllegalArgumentException::class)
    fun akuisisiDiriSendiriDitolak() {
        SimulasiRival.akuisisiAntarRival(StateRival(1, 100.0), StateRival(1, 100.0), KeputusanRival.EKSPANSI)
    }
}
