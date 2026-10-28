package id.holding.sim.domain.usaha

import id.holding.sim.data.Usaha
import kotlin.random.Random

/**
 * Strategi tambang & energi. Royalti = persen pendapatan, ditambahkan ke biaya operasi.
 * Deplesi memakai metode unit produksi; pemanggil mengisi penyusutan dari deplesi(...) bila
 * data cadangan tersedia. Tarif royalti bawaan adalah perkiraan; sebaiknya diisi per negara.
 */
class StrategiTambangEnergi(val royaltiPct: Double = 5.0) : ArketipeUsaha {
    override val arketipe: Arketipe = Arketipe.TAMBANG_ENERGI
    override val punyaBahanBaku: Boolean = true

    fun royalti(pendapatan: Double): Double = pendapatan * royaltiPct / 100.0

    override fun strukturBiaya(usaha: Usaha, pendapatan: Double): StrukturBiaya = StrukturBiaya(
        hpp = pendapatan * usaha.hppPct / 100.0,
        biayaOperasi = pendapatan * usaha.biayaOperasiPct / 100.0 + royalti(pendapatan),
        penyusutan = pendapatan * usaha.penyusutanPct / 100.0
    )

    /** Deplesi = biaya perolehan x produksi / cadangan total (produksi dibatasi sisa cadangan). */
    fun deplesi(biayaPerolehan: Double, cadanganTotalUnit: Double, produksiUnit: Double): Double {
        if (cadanganTotalUnit <= 0.0 || produksiUnit <= 0.0) return 0.0
        return biayaPerolehan * produksiUnit.coerceAtMost(cadanganTotalUnit) / cadanganTotalUnit
    }

    /**
     * Harga komoditas bulanan: kembali ke rata-rata jangka panjang + guncangan acak.
     * Harga tidak turun di bawah 5% rata-rata. Satuan harga bebas (mis. USD per unit).
     */
    fun tickHarga(
        harga: Double,
        rataJangkaPanjang: Double,
        acak: Random,
        kecepatanKembali: Double = 0.1,
        volatilitasPct: Double = 5.0
    ): Double {
        val noise = acak.nextDouble() * 2.0 - 1.0
        val perubahan = kecepatanKembali * (rataJangkaPanjang - harga) / rataJangkaPanjang +
            volatilitasPct / 100.0 * noise
        return (harga * (1.0 + perubahan)).coerceAtLeast(0.05 * rataJangkaPanjang)
    }
}
