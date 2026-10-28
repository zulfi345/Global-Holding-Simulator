package id.holding.sim.domain.engine

/**
 * Spread bid-ask generik: harga yang dibeli pemain (ask) sedikit di atas harga tengah,
 * harga yang diterima saat jual (bid) sedikit di bawah; spreadPct dibagi rata dua sisi
 * (half-spread), desain game, bukan data spread bursa sungguhan per aset.
 */
object SpreadBidAsk {
    /** hargaTengah harus > 0. spreadPct dalam persen, >= 0 (0 = tanpa spread). */
    fun hargaBeli(hargaTengah: Double, spreadPct: Double): Double =
        hargaTengah * (1.0 + spreadPct / 200.0)

    fun hargaJual(hargaTengah: Double, spreadPct: Double): Double =
        hargaTengah * (1.0 - spreadPct / 200.0)
}
