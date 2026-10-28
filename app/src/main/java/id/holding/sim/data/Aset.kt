package id.holding.sim.data

import org.json.JSONObject

data class Aset(
    val kode: String,
    val nama: String,
    val jenis: String,
    val negara: String,
    val mataUang: String,
    val hargaAwalUsd: Double,
    val volatilitasTahunanPct: Double,
    val imbalPct: Double,
    val ukuranLot: Int,
    val tanggalData: String,
    val perkiraan: Boolean
) {
    companion object {
        fun dariJson(o: JSONObject): Aset = Aset(
            kode = o.getString("kode"),
            nama = o.getString("nama"),
            jenis = o.getString("jenis"),
            negara = o.getString("negara"),
            mataUang = o.getString("mata_uang"),
            hargaAwalUsd = o.getDouble("harga_awal_usd"),
            volatilitasTahunanPct = o.getDouble("volatilitas_tahunan_pct"),
            imbalPct = o.getDouble("imbal_pct"),
            ukuranLot = o.getInt("ukuran_lot"),
            tanggalData = o.getString("tanggal_data"),
            perkiraan = o.optBoolean("perkiraan", true)
        )
    }
}
