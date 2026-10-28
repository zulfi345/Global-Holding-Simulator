package id.holding.sim.domain.engine

import org.junit.Assert.assertEquals
import org.junit.Test

class PortofolioTest {
    @Test fun belidariNolMengisiHargaRataRataDariNilaiBersih() {
        val h = Investasi.beli(hargaPerUnit = 1000.0, ukuranLot = 100, unitDiminta = 100, biayaBrokerPct = 0.15, pajakTransaksiPct = 0.0)
        val p = Portofolio.tambahBeli(null, "XAU", h)
        assertEquals(100.0, p.unit, 1e-9)
        assertEquals(1001.5, p.hargaRataRata, 1e-9) // (100000 + 150) / 100
    }

    @Test fun beliBertahapMenghitungUlangRataRataTertimbang() {
        val beli1 = Investasi.beli(1000.0, 100, 100, biayaBrokerPct = 0.0)
        val posisi1 = Portofolio.tambahBeli(null, "XAU", beli1) // 100 unit @ 1000
        val beli2 = Investasi.beli(1200.0, 100, 100, biayaBrokerPct = 0.0)
        val posisi2 = Portofolio.tambahBeli(posisi1, "XAU", beli2) // + 100 unit @ 1200
        assertEquals(200.0, posisi2.unit, 1e-9)
        assertEquals(1100.0, posisi2.hargaRataRata, 1e-9) // (100000+120000)/200
    }

    @Test fun jualTidakMengubahHargaRataRataSisaPosisi() {
        val beli = Investasi.beli(1000.0, 100, 200, biayaBrokerPct = 0.0)
        val posisi = Portofolio.tambahBeli(null, "XAU", beli) // 200 unit @ 1000
        val jual = Investasi.jual(1500.0, 100, 100, biayaBrokerPct = 0.0, pajakTransaksiPct = 0.0)
        val sisa = Portofolio.kurangiJual(posisi, jual)
        assertEquals(100.0, sisa.unit, 1e-9)
        assertEquals(1000.0, sisa.hargaRataRata, 1e-9)
    }

    @Test fun jualLebihDariDimilikiDibatasiKeNolBukanNegatif() {
        val beli = Investasi.beli(1000.0, 1, 100, biayaBrokerPct = 0.0)
        val posisi = Portofolio.tambahBeli(null, "XAU", beli) // 100 unit
        val jual = Investasi.jual(1500.0, 1, 150, biayaBrokerPct = 0.0, pajakTransaksiPct = 0.0) // diminta 150, hanya punya 100
        val sisa = Portofolio.kurangiJual(posisi, jual)
        assertEquals(0.0, sisa.unit, 1e-9)
        assertEquals(0.0, sisa.hargaRataRata, 1e-9)
    }

    @Test fun labaRugiBelumTerealisasiDariHargaPasarKini() {
        val posisi = Kepemilikan("XAU", 100.0, 1000.0)
        assertEquals(5000.0, Portofolio.labaRugiBelumTerealisasi(posisi, 1050.0), 1e-9)
        assertEquals(-2000.0, Portofolio.labaRugiBelumTerealisasi(posisi, 980.0), 1e-9)
    }

    @Test fun beliKriptoPecahanMenghitungHargaRataRata() {
        val h = Kripto.beliDenganDana(1_000_000.0, 100_000_000.0, biayaPct = 0.0)
        val p = Portofolio.tambahBeliKripto(null, "BTC", h)
        assertEquals(0.01, p.unit, 1e-12)
        assertEquals(100_000_000.0, p.hargaRataRata, 1e-3)
    }

    @Test fun jualSemuaKriptoMenghasilkanNolTanpaResidu() {
        val beli = Kripto.beliDenganDana(1_000_000.0, 333_333.0, biayaPct = 0.0)
        val posisi = Portofolio.tambahBeliKripto(null, "BTC", beli)
        val jual = Kripto.jual(posisi.unit, 400_000.0, biayaPct = 0.0)
        val sisa = Portofolio.kurangiJualKripto(posisi, jual)
        assertEquals(0.0, sisa.unit, 0.0)
        assertEquals(0.0, sisa.hargaRataRata, 0.0)
    }

    @Test fun jualSebagianKriptoMempertahankanHargaRataRata() {
        val posisi = Kepemilikan("BTC", 0.5, 80_000_000.0)
        val sisa = Portofolio.kurangiJualKripto(posisi, Kripto.jual(0.2, 90_000_000.0))
        assertEquals(0.3, sisa.unit, 1e-12)
        assertEquals(80_000_000.0, sisa.hargaRataRata, 1e-9)
    }
}
