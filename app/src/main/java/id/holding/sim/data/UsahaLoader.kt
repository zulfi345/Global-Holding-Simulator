package id.holding.sim.data

import android.content.Context
import org.json.JSONObject

object UsahaLoader {
    private const val FOLDER = "data/usaha"

    /** Membaca semua file JSON usaha dari assets, diurutkan menurut id. */
    fun muatSemua(context: Context): List<Usaha> {
        val files = context.assets.list(FOLDER) ?: emptyArray()
        return files
            .filter { it.endsWith(".json") }
            .sorted()
            .map { nama ->
                val teks = context.assets.open("$FOLDER/$nama")
                    .bufferedReader(Charsets.UTF_8)
                    .use { it.readText() }
                Usaha.dariJson(JSONObject(teks))
            }
            .sortedBy { it.id }
    }
}
