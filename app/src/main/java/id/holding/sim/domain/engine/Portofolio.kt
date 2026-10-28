package id.holding.sim.domain.engine

/** Kepemilikan satu aset: jumlah unit (Double, mendukung unit pecahan kripto) dan harga rata-rata (average cost, termasuk biaya & pajak beli). */
data class Kepemilikan(val kodeAset: String, val unit: Double, val hargaRataRata: Double)

/**
 * Posisi portofolio per aset berbasis average cost: setiap pembelian menambah unit dan
 * menghitung ulang harga rata-rata tertimbang (termasuk biaya broker & pajak dari HasilTransaksiAset);
 * penjualan mengurangi unit tanpa mengubah harga rata-rata sisa posisi.
 */
object Portofolio {
    /** Sisa unit di bawah ambang ini dianggap 0 (menghindari residu pembulatan Double). */
    private const val EPS_UNIT = 1e-12

    fun tambahBeli(posisiLama: Kepemilikan?, kodeAset: String, hasilBeli: HasilTransaksiAset): Kepemilikan =
        tambahBeliUnit(posisiLama, kodeAset, hasilBeli.jumlahUnit.toDouble(), hasilBeli.nilaiBersih)

    /** Pembelian kripto (unit pecahan); nilaiBersih = dana dibayar termasuk biaya. */
    fun tambahBeliKripto(posisiLama: Kepemilikan?, kodeAset: String, hasilBeli: HasilKripto): Kepemilikan =
        tambahBeliUnit(posisiLama, kodeAset, hasilBeli.jumlahUnit, hasilBeli.nilaiBersih)

    private fun tambahBeliUnit(posisiLama: Kepemilikan?, kodeAset: String, unitBeli: Double, nilaiBersih: Double): Kepemilikan {
        val unitLama = posisiLama?.unit ?: 0.0
        val biayaLama = unitLama * (posisiLama?.hargaRataRata ?: 0.0)
        val unitBaru = unitLama + unitBeli
        val hargaRataRataBaru = if (unitBaru <= EPS_UNIT) 0.0 else (biayaLama + nilaiBersih) / unitBaru
        return Kepemilikan(kodeAset, unitBaru, hargaRataRataBaru)
    }

    /** unit tidak boleh turun di bawah 0; menjual lebih dari yang dimiliki dibatasi ke seluruh posisi. */
    fun kurangiJual(posisiLama: Kepemilikan, hasilJual: HasilTransaksiAset): Kepemilikan =
        kurangiUnit(posisiLama, hasilJual.jumlahUnit.toDouble())

    /** Penjualan kripto (unit pecahan), aturan pembatasan sama dengan kurangiJual. */
    fun kurangiJualKripto(posisiLama: Kepemilikan, hasilJual: HasilKripto): Kepemilikan =
        kurangiUnit(posisiLama, hasilJual.jumlahUnit)

    private fun kurangiUnit(posisiLama: Kepemilikan, unitJual: Double): Kepemilikan {
        val sisa = (posisiLama.unit - unitJual).coerceAtLeast(0.0)
        val unitBaru = if (sisa <= EPS_UNIT) 0.0 else sisa
        val hargaRataRataBaru = if (unitBaru == 0.0) 0.0 else posisiLama.hargaRataRata
        return Kepemilikan(posisiLama.kodeAset, unitBaru, hargaRataRataBaru)
    }

    fun labaRugiBelumTerealisasi(posisi: Kepemilikan, hargaPasarKini: Double): Double =
        (hargaPasarKini - posisi.hargaRataRata) * posisi.unit
}
