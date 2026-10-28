package id.holding.sim.domain.engine

/**
 * Hasil likuidasi paksa. diterima = sisa tunai untuk pemilik (>= 0);
 * kekurangan = utang yang tak tertutup hasil jual (ditanggung pemilik, >= 0).
 */
data class HasilLikuidasi(val diterima: Double, val kekurangan: Double, val biayaLikuidasi: Double)

/**
 * Pembelian dengan margin/leverage. Rasio margin = ekuitas / nilai posisi x 100,
 * ekuitas = nilai posisi - pinjaman. Ambang maintenance & likuidasi serta biaya adalah
 * parameter desain game (bukan aturan broker tertentu). Fungsi murni, tanpa state.
 */
object Margin {
    /** Pinjaman = modalSendiri x (leverage - 1). leverage >= 1 (1 = tanpa pinjaman). */
    fun pinjamanDiperlukan(modalSendiri: Double, leverage: Double): Double {
        require(leverage >= 1.0) { "leverage harus >= 1" }
        return modalSendiri * (leverage - 1.0)
    }

    fun ekuitas(nilaiPosisi: Double, pinjaman: Double): Double = nilaiPosisi - pinjaman

    /** Rasio margin dalam persen; bisa negatif. nilaiPosisi harus > 0. */
    fun rasioMarginPct(nilaiPosisi: Double, pinjaman: Double): Double {
        require(nilaiPosisi > 0.0) { "nilaiPosisi harus > 0" }
        return ekuitas(nilaiPosisi, pinjaman) / nilaiPosisi * 100.0
    }

    /** Margin call jika rasio di bawah maintenance (rasio tepat sama dengan maintenance = aman). */
    fun kenaMarginCall(nilaiPosisi: Double, pinjaman: Double, maintenancePct: Double = 25.0): Boolean =
        rasioMarginPct(nilaiPosisi, pinjaman) < maintenancePct

    /** Setoran tunai (mengurangi pinjaman) agar rasio kembali ke maintenance; 0 jika sudah aman. */
    fun danaTambahan(nilaiPosisi: Double, pinjaman: Double, maintenancePct: Double = 25.0): Double =
        maxOf(0.0, nilaiPosisi * maintenancePct / 100.0 - ekuitas(nilaiPosisi, pinjaman))

    /** Likuidasi paksa dipicu jika rasio di bawah likuidasiPct (harus <= maintenance). */
    fun harusDilikuidasi(nilaiPosisi: Double, pinjaman: Double, likuidasiPct: Double = 10.0): Boolean =
        rasioMarginPct(nilaiPosisi, pinjaman) < likuidasiPct

    /** Jual seluruh posisi: biaya = persen dari nilai posisi; hasil jual melunasi pinjaman dulu. */
    fun likuidasi(nilaiPosisi: Double, pinjaman: Double, biayaLikuidasiPct: Double = 1.0): HasilLikuidasi {
        val biaya = nilaiPosisi * biayaLikuidasiPct / 100.0
        val sisa = nilaiPosisi - biaya - pinjaman
        return HasilLikuidasi(maxOf(0.0, sisa), maxOf(0.0, -sisa), biaya)
    }

    /** Bunga pinjaman sebulan = pinjaman x bungaPctTahunan/100/12. */
    fun bungaPinjamanBulanan(pinjaman: Double, bungaPctTahunan: Double): Double =
        pinjaman * bungaPctTahunan / 100.0 / 12.0
}
