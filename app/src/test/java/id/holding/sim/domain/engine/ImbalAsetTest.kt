package id.holding.sim.domain.engine

import org.junit.Assert.assertEquals
import org.junit.Test

class ImbalAsetTest {
    @Test fun bayaranBulananDariImbalPctTahunan() {
        val posisi = Kepemilikan("BBCA", 1000, 10_000.0) // nilai beli = 10 juta
        // imbal 4% setahun -> 400rb setahun -> ~33.333/bulan
        assertEquals(33_333.3333, ImbalAset.bayaranBulanan(posisi, 4.0), 1e-3)
    }

    @Test fun imbalPctNolAtauNegatifTidakAdaBayaran() {
        val posisi = Kepemilikan("BTC", 10, 500_000.0)
        assertEquals(0.0, ImbalAset.bayaranBulanan(posisi, 0.0), 1e-9)
        assertEquals(0.0, ImbalAset.bayaranBulanan(posisi, -1.0), 1e-9)
    }

    @Test fun posisiKosongTidakAdaBayaran() {
        val posisi = Kepemilikan("XAU", 0, 0.0)
        assertEquals(0.0, ImbalAset.bayaranBulanan(posisi, 5.0), 1e-9)
    }

    @Test fun bayaranNBulanKelipatanBayaranBulanan() {
        val posisi = Kepemilikan("OBLIGASI01", 100, 1_000_000.0)
        val bulanan = ImbalAset.bayaranBulanan(posisi, 6.0)
        assertEquals(bulanan * 6, ImbalAset.bayaranNBulan(posisi, 6.0, 6), 1e-6)
    }
}
