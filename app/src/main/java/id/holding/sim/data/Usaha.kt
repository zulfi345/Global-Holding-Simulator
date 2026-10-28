package id.holding.sim.data

import org.json.JSONObject

data class Usaha(
    val id: Int,
    val nama: String,
    val kelompok: String,
    val arketipe: String,
    val modalMinUsd: Double,
    val hppPct: Double,
    val biayaOperasiPct: Double,
    val penyusutanPct: Double,
    val pekerjaMin: Int,
    val izin: List<String>,
    val risiko: List<String>,
    val kpi: List<String>,
    val tanggalData: String,
    val perkiraan: Boolean
) {
    companion object {
        private fun daftar(o: JSONObject, kunci: String): List<String> {
            val a = o.optJSONArray(kunci) ?: return emptyList()
            val hasil = ArrayList<String>()
            for (i in 0 until a.length()) hasil.add(a.getString(i))
            return hasil
        }

        fun dariJson(o: JSONObject): Usaha = Usaha(
            id = o.getInt("id"),
            nama = o.getString("nama"),
            kelompok = o.getString("kelompok"),
            arketipe = o.getString("arketipe"),
            modalMinUsd = o.getDouble("modal_min_usd"),
            hppPct = o.getDouble("hpp_pct"),
            biayaOperasiPct = o.getDouble("biaya_operasi_pct"),
            penyusutanPct = o.getDouble("penyusutan_pct"),
            pekerjaMin = o.getInt("pekerja_min"),
            izin = daftar(o, "izin"),
            risiko = daftar(o, "risiko"),
            kpi = daftar(o, "kpi"),
            tanggalData = o.getString("tanggal_data"),
            perkiraan = o.optBoolean("perkiraan", true)
        )
    }
}
