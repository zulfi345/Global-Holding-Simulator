package id.holding.sim.domain.engine

/**
 * Bayaran berkala generik dari kepemilikan aset: dividen (saham), kupon (obligasi),
 * sewa (properti), dll — semua direpresentasikan oleh Aset.imbalPct (persen tahunan).
 * Basis dasar bayaran = nilai beli (hargaRataRata x unit), bukan harga pasar kini
 * (penyederhanaan desain game: imbal dihitung dari modal yang ditanam, bukan yield-on-price).
 */
object ImbalAset {
    /** Bayaran satu bulan. imbalPct <= 0 -> 0 (aset tanpa imbal berkala, mis. sebagian saham/kripto). */
    fun bayaranBulanan(posisi: Kepemilikan, imbalPct: Double): Double {
        if (imbalPct <= 0.0) return 0.0
        val nilaiBeli = posisi.unit * posisi.hargaRataRata
        return nilaiBeli * imbalPct / 100.0 / 12.0
    }

    /** Total bayaran untuk beberapa bulan sekaligus (mis. kupon obligasi semesteran = 6 bulan). */
    fun bayaranNBulan(posisi: Kepemilikan, imbalPct: Double, jumlahBulan: Int): Double =
        bayaranBulanan(posisi, imbalPct) * jumlahBulan
}
