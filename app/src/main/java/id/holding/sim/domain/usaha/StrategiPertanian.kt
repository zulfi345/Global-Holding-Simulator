package id.holding.sim.domain.usaha

import id.holding.sim.data.Usaha
import kotlin.random.Random

/**
 * Strategi pertanian: pendapatan hanya masuk di bulan panen (dibagi rata antar bulan panen),
 * hasil panen dipengaruhi risiko cuaca/hama, harga mengikuti HargaKomoditas.
 * Bulan panen bawaan (April dan Oktober) adalah perkiraan; isi per komoditas dan negara.
 */
class StrategiPertanian(
    val bulanPanen: Set<Int> = setOf(4, 10)
) : ArketipeUsaha {
    override val arketipe: Arketipe = Arketipe.PERTANIAN
    override val punyaBahanBaku: Boolean = true

    override fun strukturBiaya(usaha: Usaha, pendapatan: Double): StrukturBiaya = StrukturBiaya(
        hpp = pendapatan * usaha.hppPct / 100.0,
        biayaOperasi = pendapatan * usaha.biayaOperasiPct / 100.0,
        penyusutan = pendapatan * usaha.penyusutanPct / 100.0
    )

    /** Pendapatan bulan ini dari pendapatan tahunan; 0 di luar bulan panen. bulan = 1..12. */
    fun pendapatanBulan(bulan: Int, pendapatanTahunan: Double): Double =
        if (bulan in bulanPanen && bulanPanen.isNotEmpty()) pendapatanTahunan / bulanPanen.size else 0.0

    /**
     * Faktor hasil panen. Dengan peluang peluangGagalPct terjadi gangguan cuaca/hama yang
     * menurunkan hasil sebesar 0..penurunanMaksPct persen; selain itu 1.0.
     */
    fun faktorHasil(acak: Random, peluangGagalPct: Double = 10.0, penurunanMaksPct: Double = 50.0): Double {
        if (acak.nextDouble() * 100.0 >= peluangGagalPct) return 1.0
        return 1.0 - penurunanMaksPct / 100.0 * acak.nextDouble()
    }

    /** Hasil panen (unit) = luas x hasil per hektare x faktor hasil. */
    fun hasilPanen(luasHa: Double, hasilPerHa: Double, faktor: Double): Double =
        luasHa * hasilPerHa * faktor
}
