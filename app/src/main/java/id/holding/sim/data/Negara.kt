package id.holding.sim.data

import org.json.JSONObject

data class Negara(
    val kode: String,
    val nama: String,
    val mataUang: String,
    val pdbUsdMiliar: Double,
    val pertumbuhanPct: Double,
    val inflasiPct: Double,
    val bungaAcuanPct: Double,
    val kursPerUsd: Double,
    val pphBadanPct: Double,
    val ppnPct: Double,
    val pajakDividenPct: Double,
    val upahMinBulananUsd: Double,
    val populasiJuta: Double,
    val tanggalData: String,
    val perkiraan: Boolean
) {
    /** Nilai 1 unit mata uang negara ini dalam Rupiah, bila 1 USD = kursIdrPerUsd Rupiah. */
    fun idrPerMataUang(kursIdrPerUsd: Double): Double = kursIdrPerUsd / kursPerUsd

    companion object {
        fun dariJson(o: JSONObject): Negara = Negara(
            kode = o.getString("kode"),
            nama = o.getString("nama"),
            mataUang = o.getString("mata_uang"),
            pdbUsdMiliar = o.getDouble("pdb_usd_miliar"),
            pertumbuhanPct = o.getDouble("pertumbuhan_pct"),
            inflasiPct = o.getDouble("inflasi_pct"),
            bungaAcuanPct = o.getDouble("bunga_acuan_pct"),
            kursPerUsd = o.getDouble("kurs_per_usd"),
            pphBadanPct = o.getDouble("pph_badan_pct"),
            ppnPct = o.getDouble("ppn_pct"),
            pajakDividenPct = o.getDouble("pajak_dividen_pct"),
            upahMinBulananUsd = o.getDouble("upah_min_bulanan_usd"),
            populasiJuta = o.getDouble("populasi_juta"),
            tanggalData = o.getString("tanggal_data"),
            perkiraan = o.optBoolean("perkiraan", true)
        )
    }
}
