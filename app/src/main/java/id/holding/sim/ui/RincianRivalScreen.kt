package id.holding.sim.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import id.holding.sim.data.Rival
import id.holding.sim.domain.rival.AiRival
import id.holding.sim.domain.rival.NilaiRival
import id.holding.sim.domain.rival.SumberNilai
import id.holding.sim.domain.rival.ValuasiRival
import java.util.Locale

private val LokalRincian: Locale = Locale.forLanguageTag("id-ID")

private fun formatAngka(v: Double?): String {
    if (v == null) return "-"
    val a = Math.abs(v)
    val format = if (a >= 100.0) "%,.0f" else if (a >= 1.0) "%,.1f" else "%,.2f"
    return String.format(LokalRincian, format, v)
}

private fun formatPersen(v: Double?): String =
    if (v == null) "-" else String.format(LokalRincian, "%.1f%%", v)

private fun penjelasanNilai(rival: Rival, nilai: NilaiRival?): String = when (nilai?.sumber) {
    SumberNilai.KAPITALISASI ->
        "Kapitalisasi pasar; sumber: " + rival.sumberKapitalisasi.ifBlank { "tidak dicatat" }
    SumberNilai.MULTIPLE_PENDAPATAN ->
        "Perkiraan: pendapatan " + formatAngka(rival.pendapatanUsdMiliar) + " x multiple " +
            formatAngka(ValuasiRival.multiplePendapatan(rival.arketipeUtama)) +
            " (arketipe " + rival.arketipeUtama + "; multiple = parameter desain game, bukan data pasar)"
    null -> "Belum ada kapitalisasi maupun pendapatan, nilai tidak bisa dihitung."
}

@Composable
fun RincianRivalScreen(rival: Rival, onKembali: () -> Unit) {
    BackHandler(onBack = onKembali)
    val nilai = ValuasiRival.nilai(rival)
    val rasio = AiRival.rasioUtangTerhadapNilai(rival)
    Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp)) {
        TextButton(onClick = onKembali) { Text("< Kembali") }
        Text(rival.nama, style = MaterialTheme.typography.titleLarge)
        Text(
            rival.negara + " \u00B7 " + rival.sektor + " \u00B7 " + rival.status,
            style = MaterialTheme.typography.bodySmall
        )
        Spacer(modifier = Modifier.height(16.dp))
        BarisInfo("Nilai (US$ miliar)", teksNilai(nilai))
        Text(penjelasanNilai(rival, nilai), style = MaterialTheme.typography.bodySmall)
        Spacer(modifier = Modifier.height(16.dp))
        BarisInfo("Pendapatan (US$ miliar)", formatAngka(rival.pendapatanUsdMiliar))
        BarisInfo("Margin bersih", formatPersen(rival.marginBersihPct))
        BarisInfo("Laba bersih (US$ miliar)", formatAngka(rival.labaBersihUsdMiliar))
        Text("Laba bersih = pendapatan x margin bersih", style = MaterialTheme.typography.bodySmall)
        BarisInfo("Utang (US$ miliar)", formatAngka(rival.utangUsdMiliar))
        BarisInfo("Utang / nilai", formatPersen(rasio?.times(100.0)))
        Spacer(modifier = Modifier.height(16.dp))
        Text("Catatan angka", style = MaterialTheme.typography.titleMedium)
        Text(rival.catatanAngka.ifBlank { "-" }, style = MaterialTheme.typography.bodySmall)
        val perkiraan = if (rival.perkiraan) " (perkiraan)" else ""
        Text("Data " + rival.tanggalData + perkiraan, style = MaterialTheme.typography.bodySmall)
    }
}

@Composable
private fun BarisInfo(label: String, isi: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.bodyMedium)
        Text(isi, color = MaterialTheme.colorScheme.primary)
    }
}
