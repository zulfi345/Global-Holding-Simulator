package id.holding.sim.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import id.holding.sim.data.Rival
import id.holding.sim.domain.rival.DuniaRival
import id.holding.sim.domain.rival.NilaiRival
import id.holding.sim.domain.rival.StateRival
import id.holding.sim.domain.rival.SumberNilai
import id.holding.sim.domain.rival.ValuasiRival
import java.util.Locale

private val LokalID: Locale = Locale.forLanguageTag("id-ID")

private class BarisRival(val rival: Rival, val nilai: NilaiRival?, val diakuisisi: Boolean = false)

/** Rival bernilai diurutkan nilai terbesar dulu; yang datanya kosong di bawah (urut id). */
private fun susun(rivals: List<Rival>, states: List<StateRival> = emptyList()): List<BarisRival> {
    val peta = states.associateBy { it.rivalId }
    val semua = rivals.map { r ->
        val dasar = ValuasiRival.nilai(r)
        val st = peta[r.id]
        if (st == null || dasar == null) BarisRival(r, dasar)
        else BarisRival(r, NilaiRival(st.nilaiUsdMiliar, dasar.sumber), diakuisisi = !st.aktif)
    }
    val aktif = semua.filter { !it.diakuisisi }
    val ada = aktif.filter { it.nilai != null }
        .sortedWith(compareByDescending<BarisRival> { it.nilai!!.nilaiUsdMiliar }.thenBy { it.rival.id })
    val kosong = aktif.filter { it.nilai == null }.sortedBy { it.rival.id }
    val dibeli = semua.filter { it.diakuisisi }.sortedBy { it.rival.id }
    return ada + kosong + dibeli
}

internal fun teksNilai(n: NilaiRival?): String {
    if (n == null) return "-"
    val format = if (n.nilaiUsdMiliar >= 100.0) "%,.0f" else "%,.1f"
    return String.format(LokalID, format, n.nilaiUsdMiliar)
}

internal fun teksSumber(n: NilaiRival?): String = when (n?.sumber) {
    SumberNilai.KAPITALISASI -> "kapitalisasi"
    SumberNilai.MULTIPLE_PENDAPATAN -> "perkiraan (pendapatan x multiple)"
    null -> "belum ada data"
}

@Composable
fun RankingRivalScreen(rivals: List<Rival>, bulan: Int = 0) {
    val baris = remember(rivals, bulan) { susun(rivals, DuniaRival.stateSetelah(rivals, bulan)) }
    var dipilih by remember { mutableStateOf<Rival?>(null) }
    val terpilih = dipilih
    if (terpilih != null) {
        RincianRivalScreen(terpilih, onKembali = { dipilih = null })
    } else {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Text("Peringkat Rival", style = MaterialTheme.typography.titleLarge)
            Text("Bulan ke-" + bulan + ", US$ miliar. Sentuh baris untuk rincian.", style = MaterialTheme.typography.bodySmall)
            Spacer(modifier = Modifier.height(12.dp))
            LazyColumn {
                itemsIndexed(baris) { i, b -> BarisRivalItem(i + 1, b, onKlik = { dipilih = b.rival }) }
            }
        }
    }
}

@Composable
private fun BarisRivalItem(peringkat: Int, b: BarisRival, onKlik: () -> Unit) {
    val r = b.rival
    val tertutup = (if (r.tertutup) " \u00B7 tertutup" else "") + (if (b.diakuisisi) " \u00B7 diakuisisi" else "")
    Row(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onKlik).padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text("$peringkat. ${r.nama}", style = MaterialTheme.typography.bodyLarge)
            Text("${r.negara} \u00B7 ${r.sektor}$tertutup", style = MaterialTheme.typography.bodySmall)
        }
        Column(horizontalAlignment = Alignment.End) {
            Text(teksNilai(b.nilai), color = MaterialTheme.colorScheme.primary)
            Text(teksSumber(b.nilai), style = MaterialTheme.typography.bodySmall)
        }
    }
}
