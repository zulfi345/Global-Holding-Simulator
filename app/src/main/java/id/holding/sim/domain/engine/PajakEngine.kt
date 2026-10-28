package id.holding.sim.domain.engine

import id.holding.sim.data.Negara

/** Hasil PPh badan: pajak terutang dan sisa rugi fiskal yang dibawa ke periode berikutnya. */
data class HasilPph(val pajak: Double, val sisaRugi: Double)

/**
 * Pajak dasar per negara. Semua tarif dalam persen. Penyederhanaan:
 * rugi fiskal boleh dikompensasi 100% tanpa batas waktu (aturan batas tahun/persen per negara belum dimodelkan).
 */
object PajakEngine {
    fun pphBadan(labaSebelumPajak: Double, rugiBawaan: Double, tarifPct: Double): HasilPph {
        if (labaSebelumPajak <= 0.0) {
            return HasilPph(0.0, rugiBawaan + (-labaSebelumPajak))
        }
        val dipakai = minOf(rugiBawaan, labaSebelumPajak)
        val kena = labaSebelumPajak - dipakai
        return HasilPph(kena * tarifPct / 100.0, rugiBawaan - dipakai)
    }

    fun pphBadan(labaSebelumPajak: Double, rugiBawaan: Double, negara: Negara): HasilPph =
        pphBadan(labaSebelumPajak, rugiBawaan, negara.pphBadanPct)

    /** PPN neto = PPN keluaran - PPN masukan. Negatif berarti lebih bayar (bisa direstitusi). */
    fun ppnNeto(penjualanBersih: Double, pembelianBersih: Double, tarifPct: Double): Double =
        (penjualanBersih - pembelianBersih) * tarifPct / 100.0

    /** Pajak pemotongan atas dividen; dividen nol atau negatif tidak dipajaki. */
    fun pajakDividen(dividen: Double, tarifPct: Double): Double =
        if (dividen <= 0.0) 0.0 else dividen * tarifPct / 100.0
}
