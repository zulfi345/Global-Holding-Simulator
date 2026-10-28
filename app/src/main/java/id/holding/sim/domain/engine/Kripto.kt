package id.holding.sim.domain.engine

/**
 * Hasil transaksi kripto. jumlahUnit pecahan (Double), tanpa lot.
 * nilaiBersih beli = dana yang dibayar (nilaiKotor + biaya); jual = nilaiKotor - biaya.
 */
data class HasilKripto(
    val jumlahUnit: Double,
    val nilaiKotor: Double,
    val biaya: Double,
    val nilaiBersih: Double
)

/**
 * Transaksi kripto minimal: unit pecahan, biaya persentase + biaya jaringan tetap per transaksi
 * (parameter desain game, bukan tarif bursa tertentu). Pasar 24/7, jadi tanpa jam bursa/ARA-ARB.
 * Posisi disimpan lewat Portofolio.tambahBeliKripto / kurangiJualKripto (Kepemilikan.unit Double).
 */
object Kripto {
    /** Beli dengan anggaran: biaya = dana x biayaPct/100 + biayaJaringan; sisanya jadi unit. Dana tak cukup -> 0 unit. */
    fun beliDenganDana(
        dana: Double, hargaPerUnit: Double,
        biayaPct: Double = 0.5, biayaJaringan: Double = 0.0
    ): HasilKripto {
        require(hargaPerUnit > 0.0) { "hargaPerUnit harus > 0" }
        val biaya = dana * biayaPct / 100.0 + biayaJaringan
        val untukAset = dana - biaya
        if (dana <= 0.0 || untukAset <= 0.0) return HasilKripto(0.0, 0.0, 0.0, 0.0)
        return HasilKripto(untukAset / hargaPerUnit, untukAset, biaya, dana)
    }

    /** Jual sejumlah unit pecahan: nilaiBersih = nilaiKotor - biayaPct% - biayaJaringan (minimal 0). */
    fun jual(
        unit: Double, hargaPerUnit: Double,
        biayaPct: Double = 0.5, biayaJaringan: Double = 0.0
    ): HasilKripto {
        require(hargaPerUnit > 0.0 && unit >= 0.0) { "unit >= 0 dan hargaPerUnit > 0" }
        val kotor = unit * hargaPerUnit
        val biaya = kotor * biayaPct / 100.0 + biayaJaringan
        return HasilKripto(unit, kotor, biaya, maxOf(0.0, kotor - biaya))
    }
}
