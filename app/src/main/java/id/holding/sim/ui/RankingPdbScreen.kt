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
import id.holding.sim.data.Negara
import java.util.Locale

private val LokalID: Locale = Locale.forLanguageTag("id-ID")

@Composable
fun RankingPdbScreen(negara: List<Negara>) {
    val urut = remember(negara) { negara.sortedByDescending { it.pdbUsdMiliar } }
    var dipilih by remember { mutableStateOf<Negara?>(null) }
    val terpilih = dipilih
    if (terpilih != null) {
        RincianNegaraScreen(terpilih, onKembali = { dipilih = null })
    } else {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Text("Peringkat PDB Negara", style = MaterialTheme.typography.titleLarge)
            Text("Perkiraan 2026, US$ miliar. Sentuh baris untuk rincian.", style = MaterialTheme.typography.bodySmall)
            Spacer(modifier = Modifier.height(12.dp))
            LazyColumn {
                itemsIndexed(urut) { i, n -> BarisNegara(i + 1, n, onKlik = { dipilih = n }) }
            }
        }
    }
}

@Composable
private fun BarisNegara(peringkat: Int, n: Negara, onKlik: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onKlik).padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("$peringkat. ${n.nama}", style = MaterialTheme.typography.bodyLarge)
        Column(horizontalAlignment = Alignment.End) {
            Text(String.format(LokalID, "%,.0f", n.pdbUsdMiliar), color = MaterialTheme.colorScheme.primary)
            Text(String.format(LokalID, "tumbuh %.1f%%", n.pertumbuhanPct), style = MaterialTheme.typography.bodySmall)
        }
    }
}
