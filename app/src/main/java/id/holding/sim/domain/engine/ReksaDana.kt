package id.holding.sim.domain.engine

/**
 * Reksa dana / ETF sederhana berbasis NAV per unit. Biaya subscribe/redeem dan biaya
 * pengelolaan tahunan adalah parameter desain game. Pengelolaan dipotong dari NAV bulanan.
 */
object ReksaDana {
    /** NAV per unit = (total aset - total liabilitas) / unit beredar. unitBeredar > 0. */
    fun navPerUnit(totalAset: Double, totalLiabilitas: Double, unitBeredar: Double): Double {
        require(unitBeredar > 0.0) { "unitBeredar harus > 0" }
        return (totalAset - totalLiabilitas) / unitBeredar
    }

    /** Unit diperoleh dari dana: (dana - biaya subscribe) / NAV. nav > 0. */
    fun unitDariSubscribe(dana: Double, nav: Double, biayaSubscribePct: Double): Double {
        require(nav > 0.0) { "nav harus > 0" }
        return dana * (1.0 - biayaSubscribePct / 100.0) / nav
    }

    /** Dana diterima dari redeem: unit x NAV dikurangi biaya redeem (persen dari nilai kotor). */
    fun danaDariRedeem(unit: Double, nav: Double, biayaRedeemPct: Double): Double =
        unit * nav * (1.0 - biayaRedeemPct / 100.0)

    /** NAV setelah biaya pengelolaan sebulan: nav x (1 - biayaTahunanPct/100/12). */
    fun navSetelahBiayaBulanan(nav: Double, biayaPengelolaanTahunanPct: Double): Double =
        nav * (1.0 - biayaPengelolaanTahunanPct / 100.0 / 12.0)
}
