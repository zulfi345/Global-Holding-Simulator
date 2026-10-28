package id.holding.sim.domain.sdm

import kotlin.random.Random

/**
 * Turnover pegawai dan biaya pelatihan. Semua angka bawaan adalah parameter desain game /
 * perkiraan umum, bukan data dunia nyata. Satuan uang bebas selama konsisten.
 */
object Turnover {
    /**
     * Turnover bulanan (%) = dasar x faktor gaji. Faktor = 1 - (gaji/median - 1), dibatasi 0.4..2.5:
     * gaji di bawah median mempercepat pegawai keluar, di atas median menahannya.
     * Dasar bawaan 1.5% per bulan (±17% per tahun) perkiraan. Median <= 0 dianggap setara.
     */
    fun turnoverBulananPct(gaji: Double, medianGaji: Double, dasarBulananPct: Double = 1.5): Double {
        val rasio = if (medianGaji <= 0.0) 1.0 else gaji / medianGaji
        val faktor = (1.0 - (rasio - 1.0)).coerceIn(0.4, 2.5)
        return dasarBulananPct * faktor
    }

    /** Pegawai masa percobaan lebih mudah keluar (x1.5); selesai percobaan x1.0. */
    fun faktorPercobaan(bulanBekerja: Int, masaPercobaanBulan: Int = 3): Double =
        if (bulanBekerja < masaPercobaanBulan) 1.5 else 1.0

    /** Turnover bulanan efektif (%) = turnoverBulananPct x faktorPercobaan. */
    fun turnoverEfektifPct(
        gaji: Double,
        medianGaji: Double,
        bulanBekerja: Int,
        masaPercobaanBulan: Int = 3
    ): Double = turnoverBulananPct(gaji, medianGaji) * faktorPercobaan(bulanBekerja, masaPercobaanBulan)

    /** Jumlah pegawai yang keluar bulan ini; tiap pegawai keluar dengan peluang turnoverPct. */
    fun jumlahKeluar(jumlahPegawai: Int, turnoverPct: Double, acak: Random): Int {
        val peluang = turnoverPct.coerceIn(0.0, 100.0) / 100.0
        var keluar = 0
        for (i in 0 until jumlahPegawai.coerceAtLeast(0)) {
            if (acak.nextDouble() < peluang) keluar++
        }
        return keluar
    }

    /** Biaya pelatihan = gaji bulanan x persen gaji x bulan pelatihan. Bawaan 20% perkiraan. */
    fun biayaPelatihan(gajiBulanan: Double, bulanPelatihan: Double, persenGaji: Double = 20.0): Double =
        gajiBulanan * persenGaji / 100.0 * bulanPelatihan.coerceAtLeast(0.0)
}
