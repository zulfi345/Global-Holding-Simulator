package id.holding.sim.domain.holding

import id.holding.sim.domain.engine.PajakEngine

/**
 * Maju 1 bulan: bulan + 1, kas += laba usaha - PPh badan (PajakEngine.pphBadan, tarif negara basis,
 * rugi fiskal dibawa ke bulan berikutnya). SEMENTARA (desain game): pajak dihitung bulanan (aslinya
 * tahunan), belum ada bunga utang holding, pertumbuhan nilai usaha, atau harga aset; laba tetap.
 * tarif default 0 = tanpa pajak.
 * Kas boleh menjadi negatif bila usaha merugi (peringatan EKUITAS/KAS tetap menyala lewat Holding).
 */
object LanjutBulan {
    fun maju(h: Holding, pphBadanPct: Double = 0.0): Holding {
        require(pphBadanPct >= 0.0) { "tarif pajak tidak boleh negatif" }
        val dasar = if (h.riwayatValuasiRupiah.isEmpty()) listOf(h.valuasiRupiah) else h.riwayatValuasiRupiah
        val laba = h.labaBersihBulananRupiah
        val pph = PajakEngine.pphBadan(laba, h.rugiFiskalRupiah, pphBadanPct)
        val tanpaRiwayat = h.copy(
            kasRupiah = h.kasRupiah + laba - pph.pajak,
            bulan = h.bulan + 1,
            rugiFiskalRupiah = pph.sisaRugi
        )
        val riwayat = (dasar + tanpaRiwayat.valuasiRupiah).takeLast(Holding.RIWAYAT_MAKS)
        return tanpaRiwayat.copy(riwayatValuasiRupiah = riwayat)
    }

    fun majuBanyak(h: Holding, jumlah: Int, pphBadanPct: Double = 0.0): Holding {
        require(jumlah >= 0) { "jumlah bulan tidak boleh negatif" }
        var x = h
        for (i in 0 until jumlah) x = maju(x, pphBadanPct)
        return x
    }
}
