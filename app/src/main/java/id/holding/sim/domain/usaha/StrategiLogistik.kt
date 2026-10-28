package id.holding.sim.domain.usaha

import id.holding.sim.data.Usaha

/**
 * Strategi logistik: armada, utilisasi muatan, dan biaya bahan bakar.
 * strukturBiaya memakai persen dari data usaha; fungsi lain dipakai engine yang melacak armada.
 * Satuan bebas selama konsisten (mis. ton, km, liter, uang per liter).
 */
class StrategiLogistik : ArketipeUsaha {
    override val arketipe: Arketipe = Arketipe.LOGISTIK
    override val punyaBahanBaku: Boolean = true

    override fun strukturBiaya(usaha: Usaha, pendapatan: Double): StrukturBiaya = StrukturBiaya(
        hpp = pendapatan * usaha.hppPct / 100.0,
        biayaOperasi = pendapatan * usaha.biayaOperasiPct / 100.0,
        penyusutan = pendapatan * usaha.penyusutanPct / 100.0
    )

    /** Kapasitas total = jumlah unit armada x kapasitas per unit. */
    fun kapasitasArmada(jumlahUnit: Int, kapasitasPerUnit: Double): Double =
        jumlahUnit.coerceAtLeast(0) * kapasitasPerUnit.coerceAtLeast(0.0)

    /** Utilisasi muatan (%) = muatan terangkut / kapasitas; 0 bila kapasitas nol. */
    fun utilisasiMuatanPct(muatan: Double, kapasitasTotal: Double): Double =
        if (kapasitasTotal <= 0.0) 0.0 else muatan / kapasitasTotal * 100.0

    /** Biaya bahan bakar = jarak/100 x konsumsi per 100 km x harga per liter. */
    fun biayaBahanBakar(jarakKm: Double, konsumsiLiterPer100Km: Double, hargaPerLiter: Double): Double =
        jarakKm / 100.0 * konsumsiLiterPer100Km * hargaPerLiter

    /** Biaya per ton-km = biaya total / ton-km; 0 bila tanpa angkutan. */
    fun biayaPerTonKm(biayaTotal: Double, tonKm: Double): Double =
        if (tonKm <= 0.0) 0.0 else biayaTotal / tonKm
}
