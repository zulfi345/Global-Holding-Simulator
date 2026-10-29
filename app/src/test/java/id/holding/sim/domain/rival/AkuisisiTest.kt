package id.holding.sim.domain.rival

import id.holding.sim.data.Rival
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class AkuisisiTest {
    private fun rival(
        status: String = "publik",
        margin: Double? = null,
        kap: Double? = null,
        pendapatan: Double? = null,
        utang: Double? = null
    ) = Rival(
        id = 5, nama = "X", sektor = "s", negara = "US", status = status,
        arketipeUtama = "manufaktur", tanggalData = "2026-10", perkiraan = true,
        kapitalisasiUsdMiliar = kap, pendapatanUsdMiliar = pendapatan,
        marginBersihPct = margin, utangUsdMiliar = utang, sumberKapitalisasi = "", catatanAngka = ""
    )

    @Test fun hargaBeliPublikDenganPremi() {
        assertEquals(130.0, Akuisisi.hargaBeliRival(rival(kap = 100.0), 30.0)!!, 1e-9)
    }

    @Test fun rivalTertutupPremiLebihTinggi() {
        val r = rival(status = "tertutup", pendapatan = 10.0)
        assertEquals(16.8, Akuisisi.hargaBeliRival(r, 30.0)!!, 1e-9)
    }

    @Test fun nilaiTakDiketahuiHargaNullDanBeliDitolak() {
        assertNull(Akuisisi.hargaBeliRival(rival()))
        val h = Akuisisi.pemainBeliRival(rival(), 1000.0)
        assertFalse(h.diterima)
        assertEquals("nilai rival tidak diketahui", h.alasan)
    }

    @Test(expected = IllegalArgumentException::class)
    fun premiNegatifDitolak() {
        Akuisisi.hargaBeliRival(rival(kap = 100.0), -1.0)
    }

    @Test fun danaCukupDiterimaDanKurangDitolak() {
        val ok = Akuisisi.pemainBeliRival(rival(kap = 100.0), 200.0, 30.0)
        assertTrue(ok.diterima)
        assertEquals(130.0, ok.hargaUsdMiliar, 1e-9)
        assertEquals(1.3, ok.biayaUsdMiliar, 1e-9)
        val kurang = Akuisisi.pemainBeliRival(rival(kap = 100.0), 131.0, 30.0)
        assertFalse(kurang.diterima)
        assertEquals("dana kurang", kurang.alasan)
    }

    @Test fun rivalEkspansiMenawarBilaMampu() {
        val r = rival(margin = 30.0, kap = 100.0, utang = 10.0)
        val t = Akuisisi.tawaranRival(r, 1L, 5.0)!!
        assertEquals(5, t.rivalId)
        assertEquals(6.0, t.hargaUsdMiliar, 1e-9)
    }

    @Test fun tawaranMelebihiKapasitasNull() {
        val r = rival(margin = 30.0, kap = 100.0, utang = 10.0)
        assertNull(Akuisisi.tawaranRival(r, 1L, 9.0))
    }

    @Test fun rivalBukanEkspansiTidakMenawar() {
        val r = rival(margin = 8.0, kap = 100.0, utang = 10.0)
        assertNull(Akuisisi.tawaranRival(r, 1L, 5.0))
    }

    @Test(expected = IllegalArgumentException::class)
    fun nilaiUsahaNolDitolak() {
        Akuisisi.tawaranRival(rival(margin = 30.0, kap = 100.0), 1L, 0.0)
    }
}
