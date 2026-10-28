package id.holding.sim.domain.usaha

import id.holding.sim.data.Usaha

object ArketipeUsahaTestData {
    fun usaha(arketipe: String, hpp: Double = 40.0, opex: Double = 20.0, peny: Double = 5.0) = Usaha(
        id = 1, nama = "Uji", kelompok = "X", arketipe = arketipe, modalMinUsd = 1000.0,
        hppPct = hpp, biayaOperasiPct = opex, penyusutanPct = peny, pekerjaMin = 1,
        izin = emptyList(), risiko = emptyList(), kpi = emptyList(),
        tanggalData = "2026-10", perkiraan = true
    )
}
