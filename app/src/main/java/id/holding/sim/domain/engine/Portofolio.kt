package id.holding.sim.domain.engine

/** Kepemilikan satu aset: jumlah unit dan harga rata-rata (average cost, termasuk biaya & pajak beli). */
data class Kepemilikan(val kodeAset: String, val unit: Int, val hargaRataRata: Double)

/**
 * Posisi portofolio per aset berbasis average cost: setiap pembelian menambah unit dan
 * menghitung ulang harga rata-rata tertimbang (termasuk biaya broker & pajak dari HasilTransaksiAset);
 * penjualan mengurangi unit tanpa mengubah harga rata-rata sisa posisi.
 */
object Portofolio {
    fun tambahBeli(posisiLama: Kepemilikan?, kodeAset: String, hasilBeli: HasilTransaksiAset): Kepemilikan {
        val unitLama = posisiLama?.unit ?: 0
        val biayaLama = unitLama * (posisiLama?.hargaRataRata ?: 0.0)
        val unitBaru = unitLama + hasilBeli.jumlahUnit
        val hargaRataRataBaru = if (unitBaru == 0) 0.0 else (biayaLama + hasilBeli.nilaiBersih) / unitBaru
        return Kepemilikan(kodeAset, unitBaru, hargaRataRataBaru)
    }

    /** unit tidak boleh turun di bawah 0; menjual lebih dari yang dimiliki dibatasi ke seluruh posisi. */
    fun kurangiJual(posisiLama: Kepemilikan, hasilJual: HasilTransaksiAset): Kepemilikan {
        val unitBaru = (posisiLama.unit - hasilJual.jumlahUnit).coerceAtLeast(0)
        val hargaRataRataBaru = if (unitBaru == 0) 0.0 else posisiLama.hargaRataRata
        return Kepemilikan(posisiLama.kodeAset, unitBaru, hargaRataRataBaru)
    }

    fun labaRugiBelumTerealisasi(posisi: Kepemilikan, hargaPasarKini: Double): Double =
        (hargaPasarKini - posisi.hargaRataRata) * posisi.unit
}
