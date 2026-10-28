package id.holding.sim.domain.usaha

import kotlin.random.Random

/** Harga komoditas bulanan bersama (tambang, pertanian): kembali ke rata-rata + guncangan acak. */
object HargaKomoditas {
    /** Harga tidak turun di bawah 5% rata-rata. Satuan harga bebas. rataJangkaPanjang harus > 0. */
    fun tick(
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
