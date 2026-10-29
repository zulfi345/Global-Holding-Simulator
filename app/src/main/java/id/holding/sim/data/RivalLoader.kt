package id.holding.sim.data

import android.content.Context
import org.json.JSONObject

object RivalLoader {
    private const val FOLDER = "data/rival"

    /** Membaca semua file JSON rival dari assets, diurutkan menurut id. */
    fun muatSemua(context: Context): List<Rival> {
        val files = context.assets.list(FOLDER) ?: emptyArray()
        return files
            .filter { it.endsWith(".json") }
            .sorted()
            .map { nama ->
                val teks = context.assets.open("$FOLDER/$nama")
                    .bufferedReader(Charsets.UTF_8)
                    .use { it.readText() }
                Rival.dariJson(JSONObject(teks))
            }
            .sortedBy { it.id }
    }
}
