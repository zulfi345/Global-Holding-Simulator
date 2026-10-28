package id.holding.sim.domain.engine

/**
 * Perhitungan dasar obligasi: kupon dan current yield (yield sederhana = kupon tahunan /
 * harga pasar kini), YTM aproksimasi (M-059), harga dari yield & durasi (M-060).
 * Model arus kas disederhanakan: kupon tahunan (periode 1x/tahun), sisaTahun bilangan bulat.
 */
object Obligasi {
    /** nilaiPar = nilai nominal/pokok obligasi. kuponPct = kupon tahunan dalam persen dari nilaiPar. */
    fun kuponTahunan(nilaiPar: Double, kuponPct: Double): Double =
        nilaiPar * kuponPct / 100.0

    /** periodePerTahun: 1=tahunan, 2=semesteran, 4=kuartalan, dst. Harus >= 1. */
    fun kuponPerPeriode(nilaiPar: Double, kuponPct: Double, periodePerTahun: Int): Double =
        kuponTahunan(nilaiPar, kuponPct) / periodePerTahun

    /** Current yield: kupon tahunan dibagi harga pasar kini (bukan nilai par) x 100. hargaPasar harus > 0. */
    fun currentYieldPct(kuponTahunan: Double, hargaPasar: Double): Double =
        kuponTahunan / hargaPasar * 100.0

    /**
     * YTM aproksimasi standar (bukan solusi iteratif presisi):
     * (C + (F - P) / n) / ((F + P) / 2) x 100. hargaPasar > 0, nilaiPar > 0, sisaTahun > 0.
     */
    fun ytmPerkiraanPct(kuponTahunan: Double, nilaiPar: Double, hargaPasar: Double, sisaTahun: Double): Double {
        require(hargaPasar > 0.0 && nilaiPar > 0.0) { "hargaPasar dan nilaiPar harus > 0" }
        require(sisaTahun > 0.0) { "sisaTahun harus > 0" }
        return (kuponTahunan + (nilaiPar - hargaPasar) / sisaTahun) / ((nilaiPar + hargaPasar) / 2.0) * 100.0
    }

    /** Harga = nilai kini seluruh kupon tahunan + nilai par pada tahun ke-sisaTahun, didiskon ytmPct (persen). */
    fun hargaDariYtm(kuponTahunan: Double, nilaiPar: Double, ytmPct: Double, sisaTahun: Int): Double {
        require(sisaTahun >= 1) { "sisaTahun harus >= 1" }
        val y = ytmPct / 100.0
        require(y > -1.0) { "ytmPct tidak valid" }
        var total = 0.0
        for (t in 1..sisaTahun) {
            val arus = if (t == sisaTahun) kuponTahunan + nilaiPar else kuponTahunan
            total += arus / Math.pow(1.0 + y, t.toDouble())
        }
        return total
    }

    /** Durasi Macaulay (tahun): rata-rata tertimbang waktu arus kas, bobot = nilai kini / harga. */
    fun durasiMacaulay(kuponTahunan: Double, nilaiPar: Double, ytmPct: Double, sisaTahun: Int): Double {
        val harga = hargaDariYtm(kuponTahunan, nilaiPar, ytmPct, sisaTahun)
        val y = ytmPct / 100.0
        var bobotWaktu = 0.0
        for (t in 1..sisaTahun) {
            val arus = if (t == sisaTahun) kuponTahunan + nilaiPar else kuponTahunan
            bobotWaktu += t * arus / Math.pow(1.0 + y, t.toDouble())
        }
        return bobotWaktu / harga
    }

    /** Durasi modifikasi = Macaulay / (1 + y); sensitivitas harga terhadap perubahan yield. */
    fun durasiModifikasi(kuponTahunan: Double, nilaiPar: Double, ytmPct: Double, sisaTahun: Int): Double =
        durasiMacaulay(kuponTahunan, nilaiPar, ytmPct, sisaTahun) / (1.0 + ytmPct / 100.0)
}
