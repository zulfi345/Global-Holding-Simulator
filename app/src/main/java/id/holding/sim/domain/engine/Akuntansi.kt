package id.holding.sim.domain.engine

/** Hasil penutupan satu periode: neraca akhir + arus kas yang konsisten dengannya. */
data class HasilPeriode(val neraca: Neraca, val arusKas: LaporanArusKas)

object AkuntansiEngine {
    /**
     * Menggulirkan neraca awal ke neraca akhir memakai laporan laba rugi periode.
     * Kas diturunkan dari arus kas, sehingga neraca otomatis seimbang bila neraca awal seimbang.
     * Parameter d* = perubahan saldo (positif = naik).
     */
    fun tutupPeriode(
        awal: Neraca,
        laba: LaporanLabaRugi,
        dPiutang: Double,
        dPersediaan: Double,
        dUtangUsaha: Double,
        belanjaModal: Double,
        dividen: Double,
        dUtangBank: Double,
        setoranModal: Double
    ): HasilPeriode {
        val operasi = laba.labaBersih + laba.penyusutan - dPiutang - dPersediaan + dUtangUsaha
        val investasi = -belanjaModal
        val pendanaan = dUtangBank + setoranModal - dividen
        val akhir = Neraca(
            kas = awal.kas + operasi + investasi + pendanaan,
            piutang = awal.piutang + dPiutang,
            persediaan = awal.persediaan + dPersediaan,
            asetTetap = awal.asetTetap + belanjaModal - laba.penyusutan,
            utangUsaha = awal.utangUsaha + dUtangUsaha,
            utangBank = awal.utangBank + dUtangBank,
            modal = awal.modal + setoranModal,
            labaDitahan = awal.labaDitahan + laba.labaBersih - dividen
        )
        return HasilPeriode(akhir, LaporanArusKas(operasi, investasi, pendanaan, dividen, belanjaModal))
    }
}
