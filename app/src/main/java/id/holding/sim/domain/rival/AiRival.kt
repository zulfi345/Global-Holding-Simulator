package id.holding.sim.domain.rival

import id.holding.sim.data.Rival
import kotlin.random.Random

enum class KeputusanRival { EKSPANSI, TAHAN, BERTAHAN }

/**
 * Keputusan bulanan rival berbasis aturan (tanpa acak liar). Semua ambang = desain game.
 * Data kosong (null) tidak dianggap 0: kriteria yang datanya tak ada dilewati.
 * seed hanya menggeser ambang margin ekspansi +-JITTER_PCT per rival agar rival tidak seragam;
 * seed sama + rival sama -> hasil sama.
 */
object AiRival {
    const val MARGIN_EKSPANSI_PCT = 15.0
    const val JITTER_PCT = 2
    const val RASIO_UTANG_EKSPANSI_MAKS = 0.3
    const val RASIO_UTANG_BERTAHAN = 0.6

    /** Ambang margin bersih (%) untuk ekspansi; 13..17 tergantung seed dan id rival. */
    fun ambangEkspansiPct(rivalId: Int, seed: Long): Double {
        val acak = Random(seed * 31L + rivalId)
        return MARGIN_EKSPANSI_PCT + acak.nextInt(-JITTER_PCT, JITTER_PCT + 1)
    }

    /** utang / nilai; null bila utang atau nilai tak diketahui, atau nilai <= 0. */
    fun rasioUtangTerhadapNilai(rival: Rival): Double? {
        val utang = rival.utangUsdMiliar ?: return null
        val nilai = ValuasiRival.nilai(rival)?.nilaiUsdMiliar ?: return null
        if (nilai <= 0.0) return null
        return utang / nilai
    }

    fun putuskan(rival: Rival, seed: Long): KeputusanRival {
        val margin = rival.marginBersihPct
        if (margin != null && margin < 0.0) return KeputusanRival.BERTAHAN
        val rasio = rasioUtangTerhadapNilai(rival)
        if (rasio != null && rasio > RASIO_UTANG_BERTAHAN) return KeputusanRival.BERTAHAN
        val rasioAman = rasio == null || rasio <= RASIO_UTANG_EKSPANSI_MAKS
        if (margin != null && margin >= ambangEkspansiPct(rival.id, seed) && rasioAman) {
            return KeputusanRival.EKSPANSI
        }
        return KeputusanRival.TAHAN
    }
}
