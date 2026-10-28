package id.holding.sim.domain.sdm

/**
 * Otomatisasi operasional oleh jabatan eksekutif yang terisi. Aturan holding menetapkan jabatan
 * mana yang mengotomasi (bawaan: semua); aturan per perusahaan bisa menimpanya. Efisiensi berasal
 * dari skor kandidat; penghematan biaya maksimum adalah parameter desain game.
 */
object Otomatisasi {
    /** Aktif bila jabatan terisi dan aturan (timpa perusahaan > aturan holding > bawaan true) mengizinkan. */
    fun aktif(
        jabatan: Jabatan,
        terisi: Map<Jabatan, Kandidat>,
        aturanHolding: Map<Jabatan, Boolean> = emptyMap(),
        timpaPerusahaan: Map<Jabatan, Boolean> = emptyMap()
    ): Boolean {
        if (jabatan !in terisi) return false
        return timpaPerusahaan[jabatan] ?: aturanHolding[jabatan] ?: true
    }

    /** Efisiensi 0..1 = rata-rata skor jabatan aktif / 100; 0 bila tidak ada yang aktif. */
    fun efisiensi(
        terisi: Map<Jabatan, Kandidat>,
        aturanHolding: Map<Jabatan, Boolean> = emptyMap(),
        timpaPerusahaan: Map<Jabatan, Boolean> = emptyMap()
    ): Double {
        val skor = Jabatan.values()
            .filter { aktif(it, terisi, aturanHolding, timpaPerusahaan) }
            .map { terisi.getValue(it).skorJabatan(it) / 100.0 }
        return if (skor.isEmpty()) 0.0 else skor.average()
    }

    /** Pengali biaya operasi = 1 - penghematan maks x efisiensi (efisiensi dibatasi 0..1). Bawaan maks 10%. */
    fun faktorBiayaOperasi(efisiensi: Double, penghematanMaksPct: Double = 10.0): Double =
        1.0 - penghematanMaksPct / 100.0 * efisiensi.coerceIn(0.0, 1.0)
}
