package id.holding.sim.domain.engine

/**
 * Properti: nilai bergerak lambat (apresiasi tahunan majemuk), biaya transaksi besar,
 * biaya pemilikan berkala, dan likuiditas rendah (jual cepat = diskon dari nilai pasar).
 * Semua persentase adalah parameter desain game, bukan tarif negara tertentu.
 * Sewa bulanan memakai ImbalAset.bayaranBulanan (Aset.imbalPct) agar konsisten dengan aset lain.
 */
object Properti {
    /** Nilai setelah `bulan` bulan: nilaiAwal x (1 + apresiasiTahunanPct/100)^(bulan/12). Apresiasi > -100%. */
    fun nilaiSetelahBulan(nilaiAwal: Double, apresiasiTahunanPct: Double, bulan: Int): Double {
        require(bulan >= 0) { "bulan tidak boleh negatif" }
        require(apresiasiTahunanPct > -100.0) { "apresiasi harus > -100%" }
        return nilaiAwal * Math.pow(1.0 + apresiasiTahunanPct / 100.0, bulan / 12.0)
    }

    /** Total dibayar pembeli = harga + biaya akta/notaris/pajak beli (persen dari harga). */
    fun totalBeli(harga: Double, biayaBeliPct: Double = 2.5): Double =
        harga * (1.0 + biayaBeliPct / 100.0)

    /** Diterima penjual = harga - biaya jual (agen/notaris) - pajak penghasilan atas penjualan (persen dari harga). */
    fun bersihJual(harga: Double, biayaJualPct: Double = 2.5, pajakJualPct: Double = 2.5): Double =
        harga * (1.0 - (biayaJualPct + pajakJualPct) / 100.0)

    /** Biaya pemilikan sebulan (PBB/pajak tahunan + perawatan) = nilai x (pajakPct + perawatanPct)/100/12. */
    fun biayaPemilikanBulanan(nilaiPasar: Double, pajakTahunanPct: Double = 0.3, perawatanTahunanPct: Double = 1.0): Double =
        nilaiPasar * (pajakTahunanPct + perawatanTahunanPct) / 100.0 / 12.0

    /** Harga jual cepat (likuiditas rendah): nilai pasar dikurangi diskon, diskonPct di 0..100. */
    fun hargaJualCepat(nilaiPasar: Double, diskonPct: Double = 15.0): Double {
        require(diskonPct in 0.0..100.0) { "diskonPct harus 0..100" }
        return nilaiPasar * (1.0 - diskonPct / 100.0)
    }
}
