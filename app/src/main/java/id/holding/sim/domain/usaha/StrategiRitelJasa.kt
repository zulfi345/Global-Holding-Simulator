package id.holding.sim.domain.usaha

import id.holding.sim.data.Usaha

/**
 * Strategi ritel & jasa: lalu lintas pelanggan, konversi, rata-rata transaksi, dan perputaran stok.
 * strukturBiaya memakai persen dari data usaha. Satuan uang bebas selama konsisten.
 */
class StrategiRitelJasa : ArketipeUsaha {
    override val arketipe: Arketipe = Arketipe.RITEL_JASA
    override val punyaBahanBaku: Boolean = true

    override fun strukturBiaya(usaha: Usaha, pendapatan: Double): StrukturBiaya = StrukturBiaya(
        hpp = pendapatan * usaha.hppPct / 100.0,
        biayaOperasi = pendapatan * usaha.biayaOperasiPct / 100.0,
        penyusutan = pendapatan * usaha.penyusutanPct / 100.0
    )

    /** Pelanggan yang membeli = lalu lintas x konversi (konversi dibatasi 0..100%). */
    fun pelangganMembeli(lalulintas: Double, konversiPct: Double): Double =
        lalulintas.coerceAtLeast(0.0) * konversiPct.coerceIn(0.0, 100.0) / 100.0

    /** Pendapatan = pelanggan yang membeli x rata-rata nilai transaksi. */
    fun pendapatan(pelangganMembeli: Double, rataTransaksi: Double): Double =
        pelangganMembeli * rataTransaksi

    /** Perputaran stok (kali) = HPP / rata-rata persediaan; 0 bila persediaan nol. */
    fun perputaranStok(hpp: Double, rataRataPersediaan: Double): Double =
        if (rataRataPersediaan <= 0.0) 0.0 else hpp / rataRataPersediaan

    /** Hari persediaan = hari periode / perputaran; tak terbatas bila tanpa perputaran. */
    fun hariPersediaan(perputaran: Double, hariPeriode: Int = 365): Double =
        if (perputaran <= 0.0) Double.POSITIVE_INFINITY else hariPeriode / perputaran
}
