package id.holding.sim.ui

/**
 * Kecepatan jam game otomatis: jeda antar bulan game dalam milidetik (0 = jeda).
 * Nilai = desain game; game contoh 1 hari = 1 detik, di sini satuan terkecil = 1 bulan.
 */
enum class KecepatanJam(val label: String, val jedaMs: Long) {
    JEDA("Jeda", 0L),
    X1("1x", 3000L),
    X2("2x", 1500L),
    X5("5x", 600L);

    /** Urutan siklus tombol: Jeda -> 1x -> 2x -> 5x -> Jeda. */
    fun berikut(): KecepatanJam {
        val semua = values()
        return semua[(ordinal + 1) % semua.size]
    }
}
