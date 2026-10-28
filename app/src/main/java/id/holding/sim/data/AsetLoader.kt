package id.holding.sim.data

import android.content.Context
import org.json.JSONObject

object AsetLoader {
    private const val FOLDER = "data/aset"

    /** Membaca semua file JSON aset dari assets, diurutkan menurut kode. */
    fun muatSemua(context: Context): List<Aset> {
        val files = context.assets.list(FOLDER) ?: emptyArray()
        return files
            .filter { it.endsWith(".json") }
            .sorted()
            .map { nama ->
                val teks = context.assets.open("$FOLDER/$nama")
                    .bufferedReader(Charsets.UTF_8)
                    .use { it.readText() }
                Aset.dariJson(JSONObject(teks))
            }
            .sortedBy { it.kode }
    }
}
