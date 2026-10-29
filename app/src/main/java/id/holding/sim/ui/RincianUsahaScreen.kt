package id.holding.sim.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import id.holding.sim.data.Usaha
import id.holding.sim.domain.holding.JualUsaha
import id.holding.sim.domain.holding.PosisiUsaha

/** Rincian usaha milik: nilai, pendapatan, laba (sementara), izin/risiko/KPI, dan tombol jual. */
@Composable
fun RincianUsahaScreen(usaha: Usaha, posisi: PosisiUsaha, onJual: () -> Unit, onKembali: () -> Unit) {
    BackHandler(onBack = onKembali)
    Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp)) {
        TextButton(onClick = onKembali) { Text("< Kembali") }
        Text(usaha.nama, style = MaterialTheme.typography.titleLarge)
        Text("Kelompok " + usaha.kelompok + " \u00B7 " + usaha.arketipe, style = MaterialTheme.typography.bodySmall)
        Spacer(modifier = Modifier.height(12.dp))
        Text("Nilai: " + formatRupiahSingkat(posisi.nilaiRupiah))
        Text("Pendapatan bulanan: " + formatRupiahSingkat(posisi.pendapatanBulananRupiah))
        Text("Laba bulanan (perkiraan, pra-pajak): " + formatRupiahSingkat(posisi.labaBersihBulananRupiah))
        Text("Pekerja minimum: " + usaha.pekerjaMin)
        Spacer(modifier = Modifier.height(12.dp))
        TeksDaftar("Izin", usaha.izin)
        TeksDaftar("Risiko", usaha.risiko)
        TeksDaftar("KPI", usaha.kpi)
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            "Hasil jual bersih: " + formatRupiahSingkat(JualUsaha.hasilBersihRupiah(posisi)) +
                " (biaya jual " + JualUsaha.BIAYA_JUAL_PCT.toInt() + "%)",
            style = MaterialTheme.typography.bodySmall
        )
        Button(onClick = onJual, modifier = Modifier.fillMaxWidth()) { Text("Jual usaha") }
    }
}

@Composable
private fun TeksDaftar(judul: String, isi: List<String>) {
    if (isi.isEmpty()) return
    Text(judul, style = MaterialTheme.typography.titleSmall)
    Text(isi.joinToString(", "), style = MaterialTheme.typography.bodyMedium)
}
