package id.holding.sim.domain.holding

import id.holding.sim.data.Usaha
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class BeliUsahaTest {
    private fun usaha(id: Int, modalUsd: Double, hpp: Double = 50.0, opr: Double = 20.0, peny: Double = 10.0) = Usaha(
        id = id, nama = "U$id", kelompok = "A", arketipe = "manufaktur", modalMinUsd = modalUsd,
        hppPct = hpp, biayaOperasiPct = opr, penyusutanPct = peny, pekerjaMin = 1,
        izin = emptyList(), risiko = emptyList(), kpi = emptyList(), tanggalData = "2026-10", perkiraan = true
    )

    @Test fun hargaDariModalMinimumDikaliKurs() {
        assertEquals(1_790_000_000.0, BeliUsaha.hargaRupiah(usaha(1, 100_000.0)), 1e-3)
        assertEquals(2_000_000.0, BeliUsaha.hargaRupiah(usaha(1, 100.0), 20_000.0), 1e-6)
    }

    @Test fun posisiAwalPendapatanDanLaba() {
        val p = BeliUsaha.posisiAwal(usaha(1, 1.0), 1_200.0)
        assertEquals(100.0, p.pendapatanBulananRupiah, 1e-9)
        assertEquals(20.0, p.labaBersihBulananRupiah, 1e-9) // margin 100-80 = 20%
    }

    @Test fun marginNegatifMenghasilkanRugi() {
        val p = BeliUsaha.posisiAwal(usaha(1, 1.0, hpp = 80.0, opr = 30.0, peny = 5.0), 1_200.0)
        assertEquals(-15.0, p.labaBersihBulananRupiah, 1e-9)
    }

    @Test fun beliBerhasilKurangiKasTambahUsaha() {
        val h = Holding.mulai("A", "ID", 20) // kas 20 miliar
        val hasil = BeliUsaha.beli(h, usaha(7, 100_000.0)) // 1,79 miliar
        val baru = hasil.holding
        assertNotNull(baru)
        assertEquals(20e9 - 1_790_000_000.0, baru!!.kasRupiah, 1e-3)
        assertEquals(listOf(7), baru.usaha.map { it.usahaId })
        assertEquals(h.valuasiRupiah, baru.valuasiRupiah, 1e-3) // beli tidak mengubah valuasi
    }

    @Test fun kasKurangDitolak() {
        val hasil = BeliUsaha.beli(Holding.mulai("A", "ID", 20), usaha(8, 15_000_000.0))
        assertNull(hasil.holding)
        assertEquals(AlasanGagalBeli.KAS_KURANG, hasil.alasan)
    }

    @Test fun usahaSamaTidakBisaDibeliDua() {
        val u = usaha(9, 100_000.0)
        val sekali = BeliUsaha.beli(Holding.mulai("A", "ID", 20), u).holding!!
        val dua = BeliUsaha.beli(sekali, u)
        assertNull(dua.holding)
        assertEquals(AlasanGagalBeli.SUDAH_DIMILIKI, dua.alasan)
    }

    @Test fun kasPasPasanBerhasil() {
        val u = usaha(10, 1_000_000.0)
        val h = Holding(nama = "A", negaraBasis = "ID", modalAwalRupiah = 1.0, kasRupiah = BeliUsaha.hargaRupiah(u))
        assertNotNull(BeliUsaha.beli(h, u).holding)
    }
}
