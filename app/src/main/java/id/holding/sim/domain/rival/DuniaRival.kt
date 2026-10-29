package id.holding.sim.domain.rival

import id.holding.sim.data.Rival

/**
 * Keadaan seluruh rival pada bulan ke-N, dihitung ulang secara deterministik dari bulan 0 (seed tetap),
 * sehingga TIDAK perlu disimpan: bulan yang sama selalu memberi hasil yang sama. Biaya = N x tickSemua
 * (60 rival, maks 600 bulan: ringan). SEED_DUNIA = desain game.
 */
object DuniaRival {
    const val SEED_DUNIA = 2026L

    fun stateSetelah(rivals: List<Rival>, bulan: Int, seed: Long = SEED_DUNIA): List<StateRival> {
        require(bulan >= 0) { "bulan tidak boleh negatif" }
        var states = DaftarRival.awalSemua(rivals)
        for (b in 1..bulan) states = DaftarRival.tickSemua(states, rivals, seed, b).states
        return states
    }

    /** Akuisisi antar rival yang terjadi tepat pada bulan ke-N (kosong untuk bulan 0). */
    fun akuisisiBulan(rivals: List<Rival>, bulan: Int, seed: Long = SEED_DUNIA): List<HasilAkuisisiRival> {
        require(bulan >= 0) { "bulan tidak boleh negatif" }
        if (bulan == 0) return emptyList()
        return DaftarRival.tickSemua(stateSetelah(rivals, bulan - 1, seed), rivals, seed, bulan).akuisisi
    }
}
