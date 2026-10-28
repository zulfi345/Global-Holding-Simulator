package id.holding.sim.domain.engine

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class AkuntansiTest {
    private val awal = Neraca(100.0, 50.0, 40.0, 200.0, 30.0, 100.0, 200.0, 60.0)
    private val laba = LaporanLabaRugi(
        pendapatan = 0.0, hpp = 0.0, biayaOperasi = 0.0, penyusutan = 20.0, biayaBunga = 0.0,
        pendapatanLain = 0.0, labaKotor = 0.0, ebitda = 0.0, ebit = 0.0, labaSebelumPajak = 0.0,
        pajak = 0.0, labaBersih = 40.0, sisaRugiFiskal = 0.0
    )

    private fun tutup() = AkuntansiEngine.tutupPeriode(awal, laba, 10.0, 5.0, 5.0, 30.0, 10.0, -20.0, 0.0)

    @Test fun neracaAkhirSeimbangDanBenar() {
        val h = tutup()
        assertEquals(90.0, h.neraca.kas, 1e-9)
        assertEquals(210.0, h.neraca.asetTetap, 1e-9)
        assertEquals(90.0, h.neraca.labaDitahan, 1e-9)
        assertTrue(h.neraca.seimbang())
    }

    @Test fun arusKasKonsistenDenganEngineArusKas() {
        val h = tutup()
        val ak = ArusKasEngine.hitung(awal, h.neraca, laba.labaBersih, laba.penyusutan)
        assertEquals(ak.operasi, h.arusKas.operasi, 1e-9)
        assertEquals(ak.pendanaan, h.arusKas.pendanaan, 1e-9)
        assertTrue(ArusKasEngine.cocokDenganKas(awal, h.neraca, h.arusKas))
    }
}
