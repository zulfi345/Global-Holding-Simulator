package id.holding.sim.domain.sdm

import kotlin.random.Random

/**
 * Hubungan kerja: kepuasan pegawai, risiko mogok oleh serikat, dan biaya mogok.
 * Semua angka bawaan adalah parameter desain game, bukan data dunia nyata.
 * Kepuasan dan kekuatan serikat dalam skala 0..100.
 */
object HubunganKerja {
    private fun rasio(gaji: Double, medianGaji: Double): Double =
        if (medianGaji <= 0.0) 1.0 else gaji / medianGaji

    /**
     * Kepuasan bergerak 20% menuju target per bulan. Target = 50 + 100 x (gaji/median - 1),
     * dibatasi 0..100 (gaji setara median -> target 50).
     */
    fun tickKepuasan(kepuasan: Double, gaji: Double, medianGaji: Double): Double {
        val target = (50.0 + 100.0 * (rasio(gaji, medianGaji) - 1.0)).coerceIn(0.0, 100.0)
        return (kepuasan + 0.2 * (target - kepuasan)).coerceIn(0.0, 100.0)
    }

    /**
     * Peluang mogok bulanan (%) = tekanan x kekuatan serikat x maksimum. Tekanan = rata-rata
     * tekanan gaji (kekurangan terhadap median) dan ketidakpuasan, 0..1. Tanpa serikat -> 0.
     * Maksimum bawaan 10% per bulan.
     */
    fun peluangMogokBulananPct(
        gaji: Double,
        medianGaji: Double,
        kepuasan: Double,
        kekuatanSerikat: Double,
        maksPct: Double = 10.0
    ): Double {
        val kekuatan = kekuatanSerikat.coerceIn(0.0, 100.0)
        if (kekuatan <= 0.0) return 0.0
        val tekananGaji = (1.0 - rasio(gaji, medianGaji)).coerceIn(0.0, 1.0)
        val tekananPuas = (100.0 - kepuasan.coerceIn(0.0, 100.0)) / 100.0
        val tekanan = 0.5 * tekananGaji + 0.5 * tekananPuas
        return tekanan * kekuatan / 100.0 * maksPct
    }

    fun apakahMogok(peluangPct: Double, acak: Random): Boolean =
        acak.nextDouble() * 100.0 < peluangPct

    /** Biaya mogok = pendapatan harian x hari mogok x persen pegawai yang ikut mogok. */
    fun biayaMogok(pendapatanHarian: Double, hariMogok: Int, persenPegawaiMogok: Double): Double =
        pendapatanHarian * hariMogok.coerceAtLeast(0) * persenPegawaiMogok.coerceIn(0.0, 100.0) / 100.0
}
