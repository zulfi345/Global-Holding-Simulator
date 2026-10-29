package id.holding.sim.domain.holding

/** 5 pilihan modal awal (PROMPT_MASTER §8), satuan miliar Rupiah. */
object ModalAwal {
    val PILIHAN_MILIAR: List<Int> = listOf(20, 40, 60, 80, 100)

    /** miliar harus salah satu pilihan; selain itu IllegalArgumentException. */
    fun rupiah(miliar: Int): Double {
        require(miliar in PILIHAN_MILIAR) { "modal awal tidak valid: $miliar" }
        return miliar * 1_000_000_000.0
    }
}

/** Satu usaha yang dimiliki holding. Semua nilai Rupiah. */
data class PosisiUsaha(
    val usahaId: Int,
    val nilaiRupiah: Double,
    val pendapatanBulananRupiah: Double,
    val labaBersihBulananRupiah: Double
)

enum class PeringatanHolding { KAS_TIPIS, UTANG_TINGGI, RUGI_BERJALAN, EKUITAS_NEGATIF }

/**
 * Keadaan holding pemain (Rupiah). Valuasi = total aset - utang. Laba bersih bulanan = jumlah laba
 * usaha (bunga utang holding dihitung terpisah di LanjutBulan). Ambang peringatan = desain game.
 */
data class Holding(
    val nama: String,
    val negaraBasis: String,
    val modalAwalRupiah: Double,
    val kasRupiah: Double,
    val utangRupiah: Double = 0.0,
    val nilaiInvestasiRupiah: Double = 0.0,
    val usaha: List<PosisiUsaha> = emptyList(),
    val bulan: Int = 0,
    /** Valuasi akhir tiap bulan (indeks 0 = awal); dibatasi RIWAYAT_MAKS terakhir. */
    val riwayatValuasiRupiah: List<Double> = emptyList(),
    /** Rugi fiskal yang boleh mengurangi laba kena pajak bulan berikutnya (PajakEngine). */
    val rugiFiskalRupiah: Double = 0.0,
    /** Bunga tahunan rata-rata tertimbang atas utang holding (persen); 0 = tanpa bunga. */
    val bungaUtangPct: Double = 0.0
) {
    val nilaiUsahaRupiah: Double get() = usaha.sumOf { it.nilaiRupiah }
    val totalAsetRupiah: Double get() = kasRupiah + nilaiInvestasiRupiah + nilaiUsahaRupiah
    val valuasiRupiah: Double get() = totalAsetRupiah - utangRupiah
    val pendapatanBulananRupiah: Double get() = usaha.sumOf { it.pendapatanBulananRupiah }
    val labaBersihBulananRupiah: Double get() = usaha.sumOf { it.labaBersihBulananRupiah }

    /** Beban bunga utang holding per bulan = utang x bunga% / 100 / 12. */
    val bebanBungaBulananRupiah: Double get() = utangRupiah * bungaUtangPct / 100.0 / 12.0

    fun peringatan(): List<PeringatanHolding> {
        val hasil = ArrayList<PeringatanHolding>()
        val total = totalAsetRupiah
        if (total > 0.0 && kasRupiah / total < BATAS_KAS_TIPIS) hasil.add(PeringatanHolding.KAS_TIPIS)
        if (total > 0.0 && utangRupiah / total > BATAS_UTANG_TINGGI) hasil.add(PeringatanHolding.UTANG_TINGGI)
        if (labaBersihBulananRupiah < 0.0) hasil.add(PeringatanHolding.RUGI_BERJALAN)
        if (valuasiRupiah < 0.0) hasil.add(PeringatanHolding.EKUITAS_NEGATIF)
        return hasil
    }

    companion object {
        const val BATAS_KAS_TIPIS = 0.05
        const val BATAS_UTANG_TINGGI = 0.6
        const val RIWAYAT_MAKS = 600

        /** Holding baru: seluruh modal awal menjadi kas, belum ada usaha/utang. */
        fun mulai(nama: String, negaraBasis: String, modalMiliar: Int): Holding {
            val modal = ModalAwal.rupiah(modalMiliar)
            return Holding(nama, negaraBasis, modal, modal, riwayatValuasiRupiah = listOf(modal))
        }
    }
}
