package id.holding.sim.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import id.holding.sim.domain.holding.Holding
import id.holding.sim.domain.holding.PeringatanHolding
import id.holding.sim.domain.holding.TanggalGame
import java.util.Locale

private val LokalDashboard: Locale = Locale.forLanguageTag("id-ID")

/** Rupiah ringkas: triliun / miliar / juta; di bawah 1 juta ditulis utuh. Negatif tetap bertanda. */
internal fun formatRupiahSingkat(v: Double): String {
    val a = Math.abs(v)
    return when {
        a >= 1e12 -> String.format(LokalDashboard, "Rp%,.2f triliun", v / 1e12)
        a >= 1e9 -> String.format(LokalDashboard, "Rp%,.1f miliar", v / 1e9)
        a >= 1e6 -> String.format(LokalDashboard, "Rp%,.1f juta", v / 1e6)
        else -> String.format(LokalDashboard, "Rp%,.0f", v)
    }
}

internal fun teksPeringatan(p: PeringatanHolding): String = when (p) {
    PeringatanHolding.KAS_TIPIS -> "Kas tipis: kurang dari 5% total aset"
    PeringatanHolding.UTANG_TINGGI -> "Utang tinggi: lebih dari 60% total aset"
    PeringatanHolding.RUGI_BERJALAN -> "Usaha merugi pada bulan ini"
    PeringatanHolding.EKUITAS_NEGATIF -> "Ekuitas negatif: utang melebihi aset"
}

@Composable
fun DashboardScreen(
    holding: Holding,
    berita: List<String> = emptyList(),
    kecepatan: KecepatanJam = KecepatanJam.JEDA,
    onUbahKecepatan: () -> Unit = {},
    bungaPinjamPct: Double = 0.0,
    onPinjam: () -> Unit = {},
    onLunasi: () -> Unit = {},
    onLanjutBulan: () -> Unit = {},
    onMulaiUlang: () -> Unit = {}
) {
    val peringatan = holding.peringatan()
    Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp)) {
        Text(holding.nama, style = MaterialTheme.typography.titleLarge)
        Text(
            TanggalGame.teks(holding.bulan) + " \u00B7 " + holding.negaraBasis + " \u00B7 bulan ke-" + holding.bulan,
            style = MaterialTheme.typography.bodySmall
        )
        Spacer(modifier = Modifier.height(12.dp))
        Button(onClick = onUbahKecepatan, modifier = Modifier.fillMaxWidth()) {
            Text("Jam game: " + kecepatan.label + " (ketuk untuk ganti)")
        }
        OutlinedButton(onClick = onLanjutBulan, modifier = Modifier.fillMaxWidth()) {
            Text("Lanjut 1 bulan")
        }
        Spacer(modifier = Modifier.height(8.dp))
        KartuAngka("Valuasi holding", formatRupiahSingkat(holding.valuasiRupiah))
        Text("Riwayat valuasi (" + holding.riwayatValuasiRupiah.size + " titik)", style = MaterialTheme.typography.bodySmall)
        GrafikValuasi(holding.riwayatValuasiRupiah)
        KartuAngka("Kas", formatRupiahSingkat(holding.kasRupiah))
        KartuAngka("Pendapatan bulanan", formatRupiahSingkat(holding.pendapatanBulananRupiah))
        KartuAngka("Laba bersih bulanan", formatRupiahSingkat(holding.labaBersihBulananRupiah))
        KartuAngka("Utang holding", formatRupiahSingkat(holding.utangRupiah))
        Text(
            "Bunga utang " + String.format(LokalDashboard, "%.1f", holding.bungaUtangPct) + "% per tahun (beban " +
                formatRupiahSingkat(holding.bebanBungaBulananRupiah) + " per bulan)",
            style = MaterialTheme.typography.bodySmall
        )
        Button(onClick = onPinjam, modifier = Modifier.fillMaxWidth()) {
            Text("Pinjam Rp1 miliar (bunga " + String.format(LokalDashboard, "%.1f", bungaPinjamPct) + "%)")
        }
        OutlinedButton(onClick = onLunasi, modifier = Modifier.fillMaxWidth()) {
            Text("Lunasi Rp1 miliar")
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text("Peringatan risiko", style = MaterialTheme.typography.titleMedium)
        if (peringatan.isEmpty()) {
            Text("Tidak ada peringatan.", style = MaterialTheme.typography.bodyMedium)
        } else {
            for (p in peringatan) {
                Text(
                    teksPeringatan(p),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text("Berita rival bulan ini", style = MaterialTheme.typography.titleMedium)
        if (berita.isEmpty()) {
            Text("Tidak ada akuisisi antar rival.", style = MaterialTheme.typography.bodyMedium)
        } else {
            for (b in berita) Text(b, style = MaterialTheme.typography.bodyMedium)
        }
        Spacer(modifier = Modifier.height(24.dp))
        OutlinedButton(onClick = onMulaiUlang, modifier = Modifier.fillMaxWidth()) {
            Text("Mulai ulang holding")
        }
    }
}

@Composable
private fun KartuAngka(label: String, isi: String) {
    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(label, style = MaterialTheme.typography.bodySmall)
            Text(isi, style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.primary)
        }
    }
}
