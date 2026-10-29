package id.holding.sim.data

import org.json.JSONObject

/**
 * Rival swasta (Lampiran B). Angka keuangan bernilai null = belum diketahui (bukan 0).
 * Semua angka perkiraan kasar, lihat catatanAngka.
 */
data class Rival(
    val id: Int,
    val nama: String,
    val sektor: String,
    val negara: String,
    val status: String,
    val arketipeUtama: String,
    val tanggalData: String,
    val perkiraan: Boolean,
    val kapitalisasiUsdMiliar: Double?,
    val pendapatanUsdMiliar: Double?,
    val marginBersihPct: Double?,
    val utangUsdMiliar: Double?,
    val sumberKapitalisasi: String,
    val catatanAngka: String
) {
    val tertutup: Boolean get() = status == "tertutup"

    /** Laba bersih (USD miliar) = pendapatan x margin; null bila salah satu belum diketahui. */
    val labaBersihUsdMiliar: Double?
        get() {
            val p = pendapatanUsdMiliar
            val m = marginBersihPct
            return if (p == null || m == null) null else p * m / 100.0
        }

    companion object {
        private fun angka(o: JSONObject, kunci: String): Double? {
            if (!o.has(kunci) || o.isNull(kunci)) return null
            return o.getDouble(kunci)
        }

        fun dariJson(o: JSONObject): Rival = Rival(
            id = o.getInt("id"),
            nama = o.getString("nama"),
            sektor = o.getString("sektor"),
            negara = o.getString("negara"),
            status = o.getString("status"),
            arketipeUtama = o.getString("arketipe_utama"),
            tanggalData = o.getString("tanggal_data"),
            perkiraan = o.optBoolean("perkiraan", true),
            kapitalisasiUsdMiliar = angka(o, "kapitalisasi_usd_miliar"),
            pendapatanUsdMiliar = angka(o, "pendapatan_usd_miliar"),
            marginBersihPct = angka(o, "margin_bersih_pct"),
            utangUsdMiliar = angka(o, "utang_usd_miliar"),
            sumberKapitalisasi = o.optString("sumber_kapitalisasi", ""),
            catatanAngka = o.optString("catatan_angka", "")
        )
    }
}
