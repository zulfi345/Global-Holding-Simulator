package id.holding.sim.domain.holding

import id.holding.sim.data.Usaha

enum class AlasanGagalBeli { SUDAH_DIMILIKI, KAS_KURANG }

/** holding != null -> berhasil; selain itu alasan terisi. */
data class HasilBeliUsaha(val holding: Holding?, val alasan: AlasanGagalBeli?)

/**
 * Beli usaha dari kas holding (Rupiah). Semua parameter = desain game SEMENTARA sampai strategi per
 * arketipe tersambung: harga = modalMinUsd x kurs; pendapatan bulanan = harga/12 (perputaran aset 1x
 * setahun); laba = pendapatan x (1 - (hpp+operasi+penyusutan)%) SEBELUM pajak, bisa negatif.
 */
object BeliUsaha {
    const val KURS_IDR_PER_USD_SEMENTARA = 17_900.0
    const val PERPUTARAN_ASET_TAHUNAN = 1.0

    fun hargaRupiah(usaha: Usaha, kursIdrPerUsd: Double = KURS_IDR_PER_USD_SEMENTARA): Double {
        require(kursIdrPerUsd > 0.0) { "kurs harus positif" }
        return usaha.modalMinUsd * kursIdrPerUsd
    }

    fun posisiAwal(usaha: Usaha, harga: Double): PosisiUsaha {
        val pendapatan = harga * PERPUTARAN_ASET_TAHUNAN / 12.0
        val marginPct = 100.0 - (usaha.hppPct + usaha.biayaOperasiPct + usaha.penyusutanPct)
        return PosisiUsaha(usaha.id, harga, pendapatan, pendapatan * marginPct / 100.0)
    }

    fun beli(holding: Holding, usaha: Usaha, kursIdrPerUsd: Double = KURS_IDR_PER_USD_SEMENTARA): HasilBeliUsaha {
        if (holding.usaha.any { it.usahaId == usaha.id }) {
            return HasilBeliUsaha(null, AlasanGagalBeli.SUDAH_DIMILIKI)
        }
        val harga = hargaRupiah(usaha, kursIdrPerUsd)
        if (harga > holding.kasRupiah) return HasilBeliUsaha(null, AlasanGagalBeli.KAS_KURANG)
        val baru = holding.copy(
            kasRupiah = holding.kasRupiah - harga,
            usaha = holding.usaha + posisiAwal(usaha, harga)
        )
        return HasilBeliUsaha(baru, null)
    }
}
