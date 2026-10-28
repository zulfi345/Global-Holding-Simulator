package id.holding.sim.domain.engine

/** Obligasi yang diterbitkan perusahaan pemain. danaDiterima = nilaiPokok - biayaPenerbitan. */
data class PenerbitanObligasi(
    val nilaiPokok: Double,
    val kuponPct: Double,
    val tenorTahun: Int,
    val biayaPenerbitan: Double,
    val danaDiterima: Double
)

/** Kewajiban pembayaran satu tahun: kupon, plus pokok pada tahun jatuh tempo. */
data class PembayaranObligasi(val tahunKe: Int, val kupon: Double, val pelunasanPokok: Double) {
    val total: Double get() = kupon + pelunasanPokok
}

/**
 * Penerbitan obligasi korporasi sendiri: kupon = suku bunga acuan + premi risiko (lihat
 * Kredit.bungaPinjamanPct & PremiRisiko), kupon tahunan, pokok dilunasi sekaligus saat jatuh
 * tempo (bullet). Biaya penerbitan = parameter desain game. Dijual pada nilai par.
 */
object ObligasiKorporasi {
    fun terbitkan(
        nilaiPokok: Double, sukuBungaAcuanPct: Double, premiRisikoPct: Double,
        tenorTahun: Int, biayaPenerbitanPct: Double = 1.0
    ): PenerbitanObligasi {
        require(nilaiPokok > 0.0) { "nilaiPokok harus > 0" }
        require(tenorTahun >= 1) { "tenorTahun harus >= 1" }
        val kupon = Kredit.bungaPinjamanPct(sukuBungaAcuanPct, premiRisikoPct)
        val biaya = nilaiPokok * biayaPenerbitanPct / 100.0
        return PenerbitanObligasi(nilaiPokok, kupon, tenorTahun, biaya, nilaiPokok - biaya)
    }

    /** Jadwal pembayaran tahunan; pokok hanya dibayar pada tahun terakhir. */
    fun jadwalPembayaran(p: PenerbitanObligasi): List<PembayaranObligasi> {
        val kupon = Obligasi.kuponTahunan(p.nilaiPokok, p.kuponPct)
        return (1..p.tenorTahun).map { t ->
            PembayaranObligasi(t, kupon, if (t == p.tenorTahun) p.nilaiPokok else 0.0)
        }
    }

    /** Biaya dana efektif (perkiraan YTM) dari sisi penerbit: dana diterima dibanding kewajiban. */
    fun biayaDanaEfektifPct(p: PenerbitanObligasi): Double =
        Obligasi.ytmPerkiraanPct(Obligasi.kuponTahunan(p.nilaiPokok, p.kuponPct), p.nilaiPokok, p.danaDiterima, p.tenorTahun.toDouble())
}
