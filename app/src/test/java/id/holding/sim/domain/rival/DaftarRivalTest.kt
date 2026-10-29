package id.holding.sim.domain.rival

import id.holding.sim.data.Rival
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class DaftarRivalTest {
    private fun rival(id: Int, margin: Double?, kap: Double?, utang: Double? = null) = Rival(
        id = id, nama = "R$id", sektor = "s", negara = "US", status = "publik",
        arketipeUtama = "manufaktur", tanggalData = "2026-10", perkiraan = true,
        kapitalisasiUsdMiliar = kap, pendapatanUsdMiliar = null,
        marginBersihPct = margin, utangUsdMiliar = utang, sumberKapitalisasi = "", catatanAngka = ""
    )

    private val tiga = listOf(
        rival(1, 30.0, 1000.0, 10.0),
        rival(2, 8.0, 50.0),
        rival(3, 8.0, 40.0)
    )

    @Test fun awalSemuaMelewatiYangTakPunyaNilaiDanUrutId() {
        val s = DaftarRival.awalSemua(listOf(rival(3, null, 40.0), rival(2, null, null), rival(1, null, 10.0)))
        assertEquals(listOf(1, 3), s.map { it.rivalId })
    }

    @Test fun peringkatHanyaAktifUrutNilaiLaluId() {
        val p = DaftarRival.peringkat(
            listOf(StateRival(1, 10.0), StateRival(2, 30.0), StateRival(3, 30.0), StateRival(4, 99.0, aktif = false))
        )
        assertEquals(listOf(2, 3, 1), p.map { it.rivalId })
    }

    @Test fun rivalEkspansiMembeliSatuTargetTerbesarYangMampu() {
        val hasil = DaftarRival.tickSemua(DaftarRival.awalSemua(tiga), tiga, 1L, 1)
        assertEquals(1, hasil.akuisisi.size)
        assertEquals(2, hasil.akuisisi[0].target.rivalId)
        val byId = hasil.states.associateBy { it.rivalId }
        assertFalse(byId.getValue(2).aktif)
        assertTrue(byId.getValue(3).aktif)
        assertTrue(byId.getValue(1).aktif)
    }

    @Test fun tanpaYangMampuTidakAdaAkuisisi() {
        val dua = listOf(rival(1, 30.0, 100.0, 10.0), rival(2, 8.0, 50.0))
        val hasil = DaftarRival.tickSemua(DaftarRival.awalSemua(dua), dua, 1L, 1)
        assertTrue(hasil.akuisisi.isEmpty())
        assertTrue(hasil.states.all { it.aktif })
    }

    @Test fun deterministik() {
        val a = DaftarRival.tickSemua(DaftarRival.awalSemua(tiga), tiga, 5L, 2)
        val b = DaftarRival.tickSemua(DaftarRival.awalSemua(tiga), tiga, 5L, 2)
        assertEquals(a, b)
    }

    @Test(expected = IllegalArgumentException::class)
    fun stateTanpaRivalDitolak() {
        DaftarRival.tickSemua(listOf(StateRival(99, 10.0)), tiga, 1L, 1)
    }
}
