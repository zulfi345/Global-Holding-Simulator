package id.holding.sim.domain.engine

/**
 * Pinjaman valas (USD): pokok & cicilan dihitung dalam USD (Kredit.cicilanBulanan), nilai rupiah
 * = USD x kurs (rupiah per 1 USD, lihat KursEngine / MakroState.kursPerUsd). Rupiah melemah
 * (kurs naik) membuat beban rupiah naik: risiko kurs. Fungsi murni; kurs diberikan pemanggil.
 * Lindung nilai (hedging) belum dimodelkan.
 */
object KreditValas {
    /** Dana rupiah yang diterima saat pencairan pokok USD pada kurs saat itu. */
    fun danaRupiah(pokokUsd: Double, kursPerUsd: Double): Double {
        require(kursPerUsd > 0.0) { "kursPerUsd harus > 0" }
        return pokokUsd * kursPerUsd
    }

    /** Cicilan bulanan (USD) dan nilainya dalam rupiah pada kurs saat bayar. */
    fun cicilanRupiah(pokokUsd: Double, bungaPctTahunan: Double, tenorBulan: Int, kursPerUsd: Double): Double {
        require(kursPerUsd > 0.0) { "kursPerUsd harus > 0" }
        return Kredit.cicilanBulanan(pokokUsd, bungaPctTahunan, tenorBulan) * kursPerUsd
    }

    /**
     * Cicilan rupiah per bulan mengikuti kurs tiap bulan. kursPerBulan.size harus sama dengan
     * jumlah baris jadwal (kurs bulan ke-1 untuk cicilan ke-1, dst).
     */
    fun cicilanRupiahPerBulan(jadwalUsd: List<BarisCicilan>, kursPerBulan: List<Double>): List<Double> {
        require(jadwalUsd.size == kursPerBulan.size) { "jumlah kurs harus sama dengan jumlah cicilan" }
        return jadwalUsd.mapIndexed { i, baris -> baris.cicilan * kursPerBulan[i] }
    }

    /**
     * Laba/rugi kurs belum terealisasi atas sisa utang USD: sisaUsd x (kursAwal - kursKini).
     * Positif = laba (rupiah menguat, utang rupiah turun); negatif = rugi.
     */
    fun labaRugiSelisihKurs(sisaUtangUsd: Double, kursAwal: Double, kursKini: Double): Double =
        sisaUtangUsd * (kursAwal - kursKini)
}
