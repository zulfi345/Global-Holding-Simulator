package id.holding.sim.domain.engine

import org.junit.Assert.assertEquals
import org.junit.Test

class ReksaDanaTest {
    @Test fun navPerUnitDariAsetDikurangiLiabilitas() {
        assertEquals(1_500.0, ReksaDana.navPerUnit(1_600_000.0, 100_000.0, 1_000.0), 1e-9)
    }

    @Test(expected = IllegalArgumentException::class) fun navUnitNolDitolak() {
        ReksaDana.navPerUnit(1_000.0, 0.0, 0.0)
    }

    @Test fun subscribeDenganBiayaMengurangiUnit() {
        // 1_000_000 dana, biaya 1% -> 990_000 / 1_000 = 990 unit
        assertEquals(990.0, ReksaDana.unitDariSubscribe(1_000_000.0, 1_000.0, 1.0), 1e-9)
    }

    @Test fun redeemDenganBiayaMengurangiDana() {
        assertEquals(990_000.0, ReksaDana.danaDariRedeem(1_000.0, 1_000.0, 1.0), 1e-9)
    }

    @Test fun subscribeLaluRedeemSegeraRugiSebesarKeduaBiaya() {
        val unit = ReksaDana.unitDariSubscribe(1_000_000.0, 1_000.0, 1.0)
        assertEquals(980_100.0, ReksaDana.danaDariRedeem(unit, 1_000.0, 1.0), 1e-6)
    }

    @Test fun biayaPengelolaanBulananMenurunkanNav() {
        // 1.2% / tahun -> 0.1% / bulan
        assertEquals(999.0, ReksaDana.navSetelahBiayaBulanan(1_000.0, 1.2), 1e-9)
    }
}
