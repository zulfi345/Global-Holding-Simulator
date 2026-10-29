package id.holding.sim.domain.holding

/**
 * Pinjam & lunasi utang tingkat holding (desain game, SEMENTARA). Pinjam: kas += jumlah, utang += jumlah,
 * bunga holding = rata-rata tertimbang bunga lama dan baru. Lunasi: kas -= jumlah, utang -= jumlah
 * (bunga tak berubah; utang 0 -> bunga 0). Jumlah tak valid -> null (holding tak berubah).
 */
object Pinjam {
    fun pinjam(h: Holding, jumlah: Double, bungaPct: Double): Holding? {
        if (jumlah <= 0.0 || bungaPct < 0.0) return null
        val utangBaru = h.utangRupiah + jumlah
        val bungaBaru = (h.utangRupiah * h.bungaUtangPct + jumlah * bungaPct) / utangBaru
        return h.copy(
            kasRupiah = h.kasRupiah + jumlah,
            utangRupiah = utangBaru,
            bungaUtangPct = bungaBaru
        )
    }

    fun lunasi(h: Holding, jumlah: Double): Holding? {
        if (jumlah <= 0.0 || jumlah > h.utangRupiah || jumlah > h.kasRupiah) return null
        val utangBaru = h.utangRupiah - jumlah
        return h.copy(
            kasRupiah = h.kasRupiah - jumlah,
            utangRupiah = utangBaru,
            bungaUtangPct = if (utangBaru <= 0.0) 0.0 else h.bungaUtangPct
        )
    }
}
