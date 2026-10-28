package id.holding.sim.domain.engine

import kotlin.math.floor

/**
 * Pemecahan saham (stock split, rasio > 1) atau penggabungan (reverse split, rasio < 1).
 * Nilai total posisi (unit x hargaRataRata) dijaga tetap, kecuali pembulatan unit ke bawah
 * (sisa pecahan unit yang tak terbagi diabaikan, bukan dibayar tunai cash-in-lieu).
 */
object StockSplit {
    /** rasio harus > 0. rasio 2.0 = split 1:2 (unit x2, harga /2); rasio 0.5 = reverse split 2:1. */
    fun terapkan(posisi: Kepemilikan, rasio: Double): Kepemilikan {
        require(rasio > 0.0) { "rasio split harus > 0" }
        val unitBaru = floor(posisi.unit * rasio)
        val hargaRataRataBaru = if (unitBaru == 0.0) 0.0 else posisi.hargaRataRata / rasio
        return posisi.copy(unit = unitBaru, hargaRataRata = hargaRataRataBaru)
    }
}
