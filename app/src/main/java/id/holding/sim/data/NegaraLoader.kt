package id.holding.sim.data

import android.content.Context
import org.json.JSONObject

object NegaraLoader {
    private const val FOLDER = "data/negara"

    /** Membaca semua file JSON negara dari assets, diurutkan menurut kode. */
    fun muatSemua(context: Context): List<Negara> {
        val files = context.assets.list(FOLDER) ?: emptyArray()
        return files
            .filter { it.endsWith(".json") }
            .sorted()
            .map { nama ->
                val teks = context.assets.open("$FOLDER/$nama")
                    .bufferedReader(Charsets.UTF_8)
                    .use { it.readText() }
                Negara.dariJson(JSONObject(teks))
            }
    }
}
