package id.holding.sim.domain.rival

import id.holding.sim.data.Rival

/** Hasil satu bulan seluruh rival: state terbaru (urut id) dan daftar akuisisi yang terjadi. */
data class HasilBulanRival(val states: List<StateRival>, val akuisisi: List<HasilAkuisisiRival>)

/**
 * Penggerak seluruh rival. Tiap bulan: semua rival aktif di-tick, lalu rival ber-keputusan EKSPANSI
 * (urut nilai terbesar dulu) boleh membeli SATU rival aktif terbesar yang mampu dibelinya.
 */
object DaftarRival {
    /** State awal semua rival yang nilainya diketahui, urut id. */
    fun awalSemua(rivals: List<Rival>): List<StateRival> =
        rivals.mapNotNull { SimulasiRival.awal(it) }.sortedBy { it.rivalId }

    /** Rival aktif, nilai terbesar dulu; nilai sama -> id kecil dulu. */
    fun peringkat(states: List<StateRival>): List<StateRival> =
        states.filter { it.aktif }
            .sortedWith(compareByDescending<StateRival> { it.nilaiUsdMiliar }.thenBy { it.rivalId })

    fun tickSemua(states: List<StateRival>, rivals: List<Rival>, seed: Long, bulan: Int): HasilBulanRival {
        val peta = rivals.associateBy { it.id }
        val setelahTick = states.map { s ->
            val r = peta[s.rivalId] ?: throw IllegalArgumentException("rival tidak ditemukan: ${s.rivalId}")
            SimulasiRival.tickBulan(s, r, seed, bulan)
        }
        val saatIni = LinkedHashMap<Int, StateRival>()
        for (s in setelahTick) saatIni[s.rivalId] = s
        val peristiwa = ArrayList<HasilAkuisisiRival>()
        for (urutan in peringkat(setelahTick)) {
            val pembeli = saatIni[urutan.rivalId] ?: continue
            if (!pembeli.aktif) continue
            val keputusan = AiRival.putuskan(peta.getValue(pembeli.rivalId), seed)
            if (keputusan != KeputusanRival.EKSPANSI) continue
            val kandidat = peringkat(saatIni.values.toList()).filter { it.rivalId != pembeli.rivalId }
            for (target in kandidat) {
                val h = SimulasiRival.akuisisiAntarRival(pembeli, target, keputusan) ?: continue
                saatIni[h.pembeli.rivalId] = h.pembeli
                saatIni[h.target.rivalId] = h.target
                peristiwa.add(h)
                break
            }
        }
        return HasilBulanRival(saatIni.values.sortedBy { it.rivalId }, peristiwa)
    }
}
