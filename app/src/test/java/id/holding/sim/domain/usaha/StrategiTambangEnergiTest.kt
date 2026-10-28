package id.holding.sim.domain.usaha

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.random.Random

class StrategiTambangEnergiTest {
    private val s = StrategiTambangEnergi(royaltiPct = 5.0)

    @Test fun royaltiMasukBiayaOperasi() {
        val b = s.strukturBiaya(ArketipeUsahaTestData.usaha("tambang_energi"), 1000.0)
        assertEquals(400.0, b.hpp, 1e-9)
        assertEquals(250.0, b.biayaOperasi, 1e-9)
        assertEquals(50.0, b.penyusutan, 1e-9)
        assertEquals(50.0, s.royalti(1000.0), 1e-9)
    }

    @Test fun deplesiUnitProduksi() {
        assertEquals(100.0, s.deplesi(1000.0, 500.0, 50.0), 1e-9)
        assertEquals(1000.0, s.deplesi(1000.0, 500.0, 600.0), 1e-9)
        assertEquals(0.0, s.deplesi(1000.0, 0.0, 50.0), 1e-9)
    }

    @Test fun hargaKembaliKeRataTanpaNoise() {
        assertEquals(142.5, s.tickHarga(150.0, 100.0, Random(1), 0.1, 0.0), 1e-9)
        assertEquals(52.5, s.tickHarga(50.0, 100.0, Random(1), 0.1, 0.0), 1e-9)
    }

    @Test fun hargaDeterministikDanBerbatas() {
        assertEquals(s.tickHarga(100.0, 100.0, Random(7)), s.tickHarga(100.0, 100.0, Random(7)), 0.0)
        assertTrue(s.tickHarga(1.0, 100.0, Random(3), 0.0, 5.0) >= 5.0)
    }

    @Test fun terdaftarDiRegistry() {
        assertTrue(ArketipeRegistry.untuk(ArketipeUsahaTestData.usaha("tambang_energi")) is StrategiTambangEnergi)
    }
}
