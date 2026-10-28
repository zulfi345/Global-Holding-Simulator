package id.holding.sim.domain.engine

import org.junit.Assert.assertEquals
import org.junit.Test

class PropertiTest {
    @Test fun apresiasiSetahunSamaDenganPersenTahunan() {
        assertEquals(1_050_000_000.0, Properti.nilaiSetelahBulan(1_000_000_000.0, 5.0, 12), 1e-3)
    }

    @Test fun apresiasiEnamBulanMajemukBukanLinear() {
        assertEquals(1_024_695_076.6, Properti.nilaiSetelahBulan(1_000_000_000.0, 5.0, 6), 1.0)
    }

    @Test fun apresiasiNegatifMenurunkanNilai() {
        assertEquals(900_000_000.0, Properti.nilaiSetelahBulan(1_000_000_000.0, -10.0, 12), 1e-3)
    }

    @Test fun totalBeliTermasukBiayaTransaksi() {
        assertEquals(1_025_000_000.0, Properti.totalBeli(1_000_000_000.0, 2.5), 1e-3)
    }

    @Test fun bersihJualKurangiBiayaDanPajak() {
        assertEquals(950_000_000.0, Properti.bersihJual(1_000_000_000.0, 2.5, 2.5), 1e-3)
    }

    @Test fun biayaPemilikanBulananDariPajakDanPerawatan() {
        // 1.3% / tahun dari 1 miliar = 13 juta / 12
        assertEquals(1_083_333.3333, Properti.biayaPemilikanBulanan(1_000_000_000.0), 1e-3)
    }

    @Test fun hargaJualCepatDiskon() {
        assertEquals(850_000_000.0, Properti.hargaJualCepat(1_000_000_000.0, 15.0), 1e-3)
    }

    @Test(expected = IllegalArgumentException::class) fun diskonDiLuar0Sampai100Ditolak() {
        Properti.hargaJualCepat(1_000_000_000.0, 120.0)
    }
}
