package id.holding.sim.domain.rival

import id.holding.sim.data.Rival
import kotlin.random.Random

/** Keadaan satu rival selama permainan (nilai ekuitas USD miliar). aktif=false: sudah diakuisisi. */
data class StateRival(val rivalId: Int, val nilaiUsdMiliar: Double, val aktif: Boolean = true)

/** Hasil rival membeli rival lain: state pembeli dan target setelah transaksi. */
data class HasilAkuisisiRival(val pembeli: StateRival, val target: StateRival, val hargaUsdMiliar: Double)

/**
 * Simulasi bulanan rival. Pertumbuhan bulanan menurut keputusan AiRival + jitter deterministik
 * (seed, id rival, bulan). Semua angka = desain game.
 */
object SimulasiRival {
    const val PERTUMBUHAN_EKSPANSI_PCT = 1.0
    const val PERTUMBUHAN_TAHAN_PCT = 0.3
    const val PERTUMBUHAN_BERTAHAN_PCT = -0.2
    const val JITTER_PCT = 0.3

    /** State awal dari ValuasiRival; null bila nilai rival tak diketahui. */
    fun awal(rival: Rival): StateRival? {
        val n = ValuasiRival.nilai(rival) ?: return null
        return StateRival(rival.id, n.nilaiUsdMiliar)
    }

    fun pertumbuhanBulananPct(keputusan: KeputusanRival, rivalId: Int, seed: Long, bulan: Int): Double {
        val dasar = when (keputusan) {
            KeputusanRival.EKSPANSI -> PERTUMBUHAN_EKSPANSI_PCT
            KeputusanRival.TAHAN -> PERTUMBUHAN_TAHAN_PCT
            KeputusanRival.BERTAHAN -> PERTUMBUHAN_BERTAHAN_PCT
        }
        val acak = Random(seed * 31L + rivalId * 1009L + bulan)
        return dasar + acak.nextDouble(-JITTER_PCT, JITTER_PCT)
    }

    /** Maju satu bulan. State tak aktif dikembalikan apa adanya. id state dan rival harus sama. */
    fun tickBulan(state: StateRival, rival: Rival, seed: Long, bulan: Int): StateRival {
        require(state.rivalId == rival.id) { "state dan rival tidak cocok" }
        if (!state.aktif) return state
        val g = pertumbuhanBulananPct(AiRival.putuskan(rival, seed), rival.id, seed, bulan)
        return state.copy(nilaiUsdMiliar = state.nilaiUsdMiliar * (1.0 + g / 100.0))
    }

    /**
     * Rival pembeli (keputusan harus EKSPANSI) membeli rival target: harga = nilai target x (1+premi),
     * maksimal KAPASITAS_BELI_RIVAL_PCT dari nilai pembeli. Nilai pembeli baru = nilai + nilai target - harga
     * (premi hangus); target jadi tak aktif. Gagal -> null. Id harus beda.
     */
    fun akuisisiAntarRival(
        pembeli: StateRival,
        target: StateRival,
        keputusanPembeli: KeputusanRival,
        premiPct: Double = Akuisisi.PREMI_DEFAULT_PCT
    ): HasilAkuisisiRival? {
        require(pembeli.rivalId != target.rivalId) { "pembeli dan target harus berbeda" }
        require(premiPct >= 0.0) { "premi tidak boleh negatif" }
        if (!pembeli.aktif || !target.aktif) return null
        if (keputusanPembeli != KeputusanRival.EKSPANSI) return null
        val harga = target.nilaiUsdMiliar * (1.0 + premiPct / 100.0)
        if (harga > pembeli.nilaiUsdMiliar * Akuisisi.KAPASITAS_BELI_RIVAL_PCT / 100.0) return null
        val pembeliBaru = pembeli.copy(nilaiUsdMiliar = pembeli.nilaiUsdMiliar + target.nilaiUsdMiliar - harga)
        return HasilAkuisisiRival(pembeliBaru, target.copy(aktif = false), harga)
    }
}
