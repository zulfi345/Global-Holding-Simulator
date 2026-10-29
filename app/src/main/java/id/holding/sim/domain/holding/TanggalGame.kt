package id.holding.sim.domain.holding

/** Kalender game: bulan ke-0 = Jan 2026 (desain game, mengikuti game contoh). */
object TanggalGame {
    const val TAHUN_MULAI = 2026
    private val NAMA_BULAN = listOf("Jan", "Feb", "Mar", "Apr", "Mei", "Jun", "Jul", "Agu", "Sep", "Okt", "Nov", "Des")

    fun teks(bulan: Int): String {
        require(bulan >= 0) { "bulan tidak boleh negatif" }
        return NAMA_BULAN[bulan % 12] + " " + (TAHUN_MULAI + bulan / 12)
    }
}
