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
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import id.holding.sim.data.Usaha
import id.holding.sim.domain.holding.BeliUsaha
import id.holding.sim.domain.holding.Holding

/** Daftar 60 usaha: terjangkau dulu (harga naik), lalu yang belum terjangkau; sudah dimiliki diberi tanda. */
internal fun susunUsaha(daftar: List<Usaha>, kas: Double): List<Usaha> =
    daftar.sortedWith(
        compareBy<Usaha> { BeliUsaha.hargaRupiah(it) > kas }
            .thenBy { it.modalMinUsd }
            .thenBy { it.id }
    )

@Composable
fun UsahaScreen(daftar: List<Usaha>, holding: Holding, onBeli: (Usaha) -> Unit, onJual: (Usaha) -> Unit = {}) {
    var dipilihId by remember { mutableStateOf<Int?>(null) }
    val terpilih = daftar.firstOrNull { it.id == dipilihId }
    val posisiTerpilih = holding.usaha.firstOrNull { it.usahaId == dipilihId }
    if (terpilih != null && posisiTerpilih != null) {
        RincianUsahaScreen(terpilih, posisiTerpilih, onJual = {
            onJual(terpilih)
            dipilihId = null
        }, onKembali = { dipilihId = null })
    } else {
        DaftarUsaha(daftar, holding, onBeli, onRincian = { dipilihId = it.id })
    }
}

@Composable
private fun DaftarUsaha(daftar: List<Usaha>, holding: Holding, onBeli: (Usaha) -> Unit, onRincian: (Usaha) -> Unit) {
    val urut = remember(daftar, holding.kasRupiah) { susunUsaha(daftar, holding.kasRupiah) }
    val dimiliki = holding.usaha.map { it.usahaId }.toSet()
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Beli Usaha", style = MaterialTheme.typography.titleLarge)
        Text(
            "Kas: " + formatRupiahSingkat(holding.kasRupiah) +
                " \u00B7 kurs sementara Rp17.900/US$ \u00B7 pendapatan & laba = perkiraan sementara",
            style = MaterialTheme.typography.bodySmall
        )
        Spacer(modifier = Modifier.height(12.dp))
        LazyColumn {
            items(urut) { u ->
                val harga = BeliUsaha.hargaRupiah(u)
                val punya = u.id in dimiliki
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(u.nama, style = MaterialTheme.typography.bodyLarge)
                        Text(
                            "Kelompok " + u.kelompok + " \u00B7 " + formatRupiahSingkat(harga),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    if (punya) {
                        TextButton(onClick = { onRincian(u) }) { Text("Dimiliki \u203A") }
                    } else {
                        Button(onClick = { onBeli(u) }, enabled = harga <= holding.kasRupiah) { Text("Beli") }
                    }
                }
            }
        }
    }
}
