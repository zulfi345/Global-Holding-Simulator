package id.holding.sim.domain.engine

import kotlin.random.Random

/**
 * Tick harga bulanan generik untuk aset investasi (saham, komoditas, indeks,
 * obligasi, kripto, properti): kembali ke harga awal + guncangan acak berskala
 * volatilitas tahunan aset (volatilitasTahunanPct dari Aset.kt).
 */
object HargaAset {
    /**
     * Harga tidak turun di bawah 5% harga awal. hargaAwal harus > 0.
     * volatilitasBulananPct = volatilitasTahunanPct / 12 (perkiraan kasar, bukan skala akar-waktu).
     */
    fun tick(
        harga: Double,
        hargaAwal: Double,
        volatilitasTahunanPct: Double,
        acak: Random,
        kecepatanKembali: Double = 0.05
    ): Double {
        val volatilitasBulananPct = volatilitasTahunanPct / 12.0
        val noise = acak.nextDouble() * 2.0 - 1.0
        val perubahan = kecepatanKembali * (hargaAwal - harga) / hargaAwal +
            volatilitasBulananPct / 100.0 * noise
        return (harga * (1.0 + perubahan)).coerceAtLeast(0.05 * hargaAwal)
    }
}
