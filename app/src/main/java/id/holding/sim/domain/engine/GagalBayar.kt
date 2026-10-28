package id.holding.sim.domain.engine

enum class StatusKredit { LANCAR, MENUNGGAK, GAGAL_BAYAR }

/** Hasil restrukturisasi: pokok baru (sisa + biaya) dan cicilan bulanan baru. */
data class HasilRestrukturisasi(val pokokBaru: Double, val biayaRestrukturisasi: Double, val cicilanBaru: Double)

/**
 * Keterlambatan, gagal bayar, dan restrukturisasi kredit. Denda, batas gagal bayar, dan biaya
 * restrukturisasi adalah parameter desain game (bukan aturan bank/negara tertentu).
 * Cicilan baru memakai Kredit.cicilanBulanan.
 */
object GagalBayar {
    /** Denda satu kali atas cicilan yang tertunggak = cicilanTertunggak x dendaPct/100. */
    fun dendaKeterlambatan(cicilanTertunggak: Double, dendaPct: Double = 2.0): Double =
        cicilanTertunggak * dendaPct / 100.0

    /** 0 bulan menunggak = LANCAR; di bawah batas = MENUNGGAK; mencapai batas = GAGAL_BAYAR. */
    fun status(bulanMenunggak: Int, batasGagalBayarBulan: Int = 3): StatusKredit {
        require(bulanMenunggak >= 0) { "bulanMenunggak tidak boleh negatif" }
        require(batasGagalBayarBulan >= 1) { "batasGagalBayarBulan harus >= 1" }
        return when {
            bulanMenunggak == 0 -> StatusKredit.LANCAR
            bulanMenunggak < batasGagalBayarBulan -> StatusKredit.MENUNGGAK
            else -> StatusKredit.GAGAL_BAYAR
        }
    }

    /**
     * Restrukturisasi: biaya (persen dari sisa pinjaman) ditambahkan ke pokok, lalu tenor
     * diperpanjang (tenorBaru harus > tenorSisa) dengan bunga baru (>= 0).
     */
    fun restrukturisasi(
        sisaPinjaman: Double, tenorSisaBulan: Int, tenorBaruBulan: Int,
        bungaBaruPctTahunan: Double, biayaPct: Double = 1.0
    ): HasilRestrukturisasi {
        require(tenorBaruBulan > tenorSisaBulan) { "tenor baru harus lebih panjang dari sisa tenor" }
        require(bungaBaruPctTahunan >= 0.0) { "bunga baru tidak boleh negatif" }
        val biaya = sisaPinjaman * biayaPct / 100.0
        val pokokBaru = sisaPinjaman + biaya
        return HasilRestrukturisasi(pokokBaru, biaya, Kredit.cicilanBulanan(pokokBaru, bungaBaruPctTahunan, tenorBaruBulan))
    }
}
