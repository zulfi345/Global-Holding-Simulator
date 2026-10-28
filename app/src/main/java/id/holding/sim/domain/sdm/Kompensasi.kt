package id.holding.sim.domain.sdm

/**
 * Kompensasi eksekutif: bonus berbasis kinerja, opsi saham sederhana (nilai intrinsik + vesting),
 * dan total kompensasi tahunan. Semua angka bawaan adalah parameter desain game / perkiraan umum.
 * Satuan uang bebas selama konsisten.
 */
object Kompensasi {
    /**
     * Bonus = gaji tahunan x target bonus x capaian. Capaian di bawah ambang -> 0;
     * capaian dibatasi batasMaksPct. Bawaan: target 20% gaji, ambang 80%, batas 150%.
     */
    fun bonusKinerja(
        gajiTahunan: Double,
        capaianPct: Double,
        targetBonusPct: Double = 20.0,
        ambangPct: Double = 80.0,
        batasMaksPct: Double = 150.0
    ): Double {
        if (capaianPct < ambangPct) return 0.0
        val faktor = minOf(capaianPct, batasMaksPct) / 100.0
        return gajiTahunan * targetBonusPct / 100.0 * faktor
    }

    /** Nilai intrinsik opsi = max(0, harga saham - harga pelaksanaan) x jumlah opsi. */
    fun nilaiOpsi(hargaSaham: Double, hargaPelaksanaan: Double, jumlahOpsi: Double): Double =
        (hargaSaham - hargaPelaksanaan).coerceAtLeast(0.0) * jumlahOpsi.coerceAtLeast(0.0)

    /**
     * Opsi yang sudah vested: 0 sebelum cliff; sesudahnya bertahap linear hingga penuh
     * pada bulanVesting. Bawaan: vesting 48 bulan, cliff 12 bulan.
     */
    fun opsiVested(
        jumlahOpsi: Double,
        bulanBekerja: Int,
        bulanVesting: Int = 48,
        cliffBulan: Int = 12
    ): Double {
        if (bulanBekerja < cliffBulan) return 0.0
        if (bulanVesting <= 0) return jumlahOpsi
        return jumlahOpsi * minOf(bulanBekerja, bulanVesting) / bulanVesting.toDouble()
    }

    /** Total kompensasi tahunan = gaji bulanan x 12 + bonus + beban opsi. */
    fun totalTahunan(gajiBulanan: Double, bonus: Double, bebanOpsi: Double): Double =
        gajiBulanan * 12.0 + bonus + bebanOpsi
}
