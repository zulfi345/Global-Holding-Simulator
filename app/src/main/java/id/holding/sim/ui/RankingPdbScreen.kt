package id.holding.sim.ui

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import id.holding.sim.data.Negara
import java.util.Locale

private val LokalID: Locale = Locale.forLanguageTag("id-ID")

@Composable
fun RankingPdbScreen(negara: List<Negara>) {
    val urut = negara.sortedByDescending { it.pdbUsdMiliar }
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Peringkat PDB Negara", style = MaterialTheme.typography.titleLarge)
        Text("Perkiraan 2026, US$ miliar", style = MaterialTheme.typography.bodySmall)
        Spacer(modifier = Modifier.height(12.dp))
        LazyColumn {
            itemsIndexed(urut) { i, n -> BarisNegara(i + 1, n) }
        }
    }
}

@Composable
private fun BarisNegara(peringkat: Int, n: Negara) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
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
