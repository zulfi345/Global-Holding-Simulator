package id.holding.sim.domain.sdm

import id.holding.sim.data.Negara

/**
 * Penggajian: kepatuhan upah minimum negara, total biaya karyawan, pesangon sederhana.
 * Beban majikan (jaminan sosial) dan pesangon bawaan adalah perkiraan umum; aturan tiap negara
 * berbeda dan perlu data per negara pada tahap lanjut. Gaji untuk cek upah minimum dalam USD
 * (sama dengan Negara.upahMinBulananUsd); fungsi lain memakai satuan uang bebas.
 */
object Penggajian {
    fun memenuhiUpahMinimum(gajiBulananUsd: Double, negara: Negara): Boolean =
        gajiBulananUsd >= negara.upahMinBulananUsd

    /** Gaji dinaikkan ke upah minimum bila di bawahnya. */
    fun sesuaikanKeUpahMinimum(gajiBulananUsd: Double, negara: Negara): Double =
        maxOf(gajiBulananUsd, negara.upahMinBulananUsd)

    /** Total biaya karyawan bulanan = gaji + beban majikan (persen gaji). Bawaan 10% perkiraan. */
    fun totalBiayaKaryawan(gajiBulanan: Double, bebanMajikanPct: Double = 10.0): Double =
        gajiBulanan * (1.0 + bebanMajikanPct / 100.0)

    /** Pesangon = gaji bulanan x bulan gaji per tahun kerja x masa kerja (tahun). Bawaan 1 bulan/tahun perkiraan. */
    fun pesangon(gajiBulanan: Double, masaKerjaTahun: Double, bulanGajiPerTahun: Double = 1.0): Double =
        gajiBulanan * bulanGajiPerTahun * masaKerjaTahun.coerceAtLeast(0.0)
}
