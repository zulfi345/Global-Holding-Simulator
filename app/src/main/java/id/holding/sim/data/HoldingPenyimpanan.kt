package id.holding.sim.data

import android.content.Context
import id.holding.sim.domain.holding.Holding
import id.holding.sim.domain.holding.PosisiUsaha
import java.net.URLDecoder
import java.net.URLEncoder

/**
 * Simpan/muat Holding pemain. Format teks sederhana (baris kunci=nilai) agar bisa diuji unit tanpa
 * org.json; nama & negara di-URL-encode. Teks rusak/versi lain -> null (pemain kembali ke layar pilih).
 */
object HoldingPenyimpanan {
    private const val BERKAS = "holding_game"
    private const val KUNCI = "holding"
    private const val VERSI = "1"

    private fun enc(s: String): String = URLEncoder.encode(s, "UTF-8")
    private fun dec(s: String): String = URLDecoder.decode(s, "UTF-8")

    fun keTeks(h: Holding): String {
        val usaha = h.usaha.joinToString(";") {
            "${it.usahaId}:${it.nilaiRupiah}:${it.pendapatanBulananRupiah}:${it.labaBersihBulananRupiah}"
        }
        return listOf(
            "v=$VERSI",
            "nama=${enc(h.nama)}",
            "negara=${enc(h.negaraBasis)}",
            "modal=${h.modalAwalRupiah}",
            "kas=${h.kasRupiah}",
            "utang=${h.utangRupiah}",
            "investasi=${h.nilaiInvestasiRupiah}",
            "bulan=${h.bulan}",
            "usaha=$usaha",
            "riwayat=${h.riwayatValuasiRupiah.joinToString(";")}",
            "rugi=${h.rugiFiskalRupiah}"
        ).joinToString("\n")
    }

    fun dariTeks(teks: String?): Holding? {
        if (teks == null) return null
        return try {
            val peta = HashMap<String, String>()
            for (baris in teks.split("\n")) {
                val i = baris.indexOf('=')
                if (i > 0) peta[baris.substring(0, i)] = baris.substring(i + 1)
            }
            if (peta["v"] != VERSI) return null
            val usaha = ArrayList<PosisiUsaha>()
            val mentah = peta["usaha"] ?: ""
            if (mentah.isNotEmpty()) {
                for (u in mentah.split(";")) {
                    val p = u.split(":")
                    if (p.size != 4) return null
                    usaha.add(PosisiUsaha(p[0].toInt(), p[1].toDouble(), p[2].toDouble(), p[3].toDouble()))
                }
            }
            val riwayat = ArrayList<Double>()
            val mentahRiwayat = peta["riwayat"] ?: ""
            if (mentahRiwayat.isNotEmpty()) for (x in mentahRiwayat.split(";")) riwayat.add(x.toDouble())
            Holding(
                nama = dec(peta["nama"] ?: return null),
                negaraBasis = dec(peta["negara"] ?: return null),
                modalAwalRupiah = (peta["modal"] ?: return null).toDouble(),
                kasRupiah = (peta["kas"] ?: return null).toDouble(),
                utangRupiah = (peta["utang"] ?: return null).toDouble(),
                nilaiInvestasiRupiah = (peta["investasi"] ?: return null).toDouble(),
                usaha = usaha,
                bulan = (peta["bulan"] ?: return null).toInt(),
                riwayatValuasiRupiah = riwayat,
                rugiFiskalRupiah = (peta["rugi"] ?: "0.0").toDouble()
            )
        } catch (e: Exception) {
            null
        }
    }

    fun simpan(context: Context, h: Holding) {
        context.getSharedPreferences(BERKAS, Context.MODE_PRIVATE).edit().putString(KUNCI, keTeks(h)).apply()
    }

    fun muat(context: Context): Holding? =
        dariTeks(context.getSharedPreferences(BERKAS, Context.MODE_PRIVATE).getString(KUNCI, null))

    fun hapus(context: Context) {
        context.getSharedPreferences(BERKAS, Context.MODE_PRIVATE).edit().remove(KUNCI).apply()
    }
}
