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
import id.holding.sim.data.Negara
import java.util.Locale

private val LokalNegara: Locale = Locale.forLanguageTag("id-ID")

private fun angka(v: Double, desimal: Int): String =
    String.format(LokalNegara, "%,." + desimal + "f", v)

private fun persen(v: Double): String = String.format(LokalNegara, "%.1f%%", v)

/** PDB per kapita (US$) = PDB (US$ miliar) x 1000 / populasi (juta); null bila populasi <= 0. */
internal fun pdbPerKapitaUsd(n: Negara): Double? =
    if (n.populasiJuta <= 0.0) null else n.pdbUsdMiliar * 1000.0 / n.populasiJuta

@Composable
fun RincianNegaraScreen(negara: Negara, onKembali: () -> Unit) {
    BackHandler(onBack = onKembali)
    val perKapita = pdbPerKapitaUsd(negara)
    Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp)) {
        TextButton(onClick = onKembali) { Text("< Kembali") }
        Text(negara.nama, style = MaterialTheme.typography.titleLarge)
        Text(negara.kode + " \u00B7 " + negara.mataUang, style = MaterialTheme.typography.bodySmall)
        Spacer(modifier = Modifier.height(16.dp))
        Text("Ekonomi", style = MaterialTheme.typography.titleMedium)
        BarisNilai("PDB (US$ miliar)", angka(negara.pdbUsdMiliar, 0))
        BarisNilai("Pertumbuhan PDB", persen(negara.pertumbuhanPct))
        BarisNilai("Populasi (juta)", angka(negara.populasiJuta, 1))
        BarisNilai("PDB per kapita (US$)", if (perKapita == null) "-" else angka(perKapita, 0))
        Text("PDB per kapita = PDB x 1000 / populasi (juta)", style = MaterialTheme.typography.bodySmall)
        Spacer(modifier = Modifier.height(16.dp))
        Text("Moneter", style = MaterialTheme.typography.titleMedium)
        BarisNilai("Inflasi", persen(negara.inflasiPct))
        BarisNilai("Bunga acuan", persen(negara.bungaAcuanPct))
        BarisNilai("Kurs (1 US$ =)", angka(negara.kursPerUsd, 2) + " " + negara.mataUang)
        Spacer(modifier = Modifier.height(16.dp))
        Text("Pajak & tenaga kerja", style = MaterialTheme.typography.titleMedium)
        BarisNilai("PPh badan", persen(negara.pphBadanPct))
        BarisNilai("PPN", persen(negara.ppnPct))
        BarisNilai("Pajak dividen", persen(negara.pajakDividenPct))
        BarisNilai("Upah minimum / bulan (US$)", angka(negara.upahMinBulananUsd, 0))
        Spacer(modifier = Modifier.height(16.dp))
        val perkiraan = if (negara.perkiraan) " (perkiraan)" else ""
        Text("Data " + negara.tanggalData + perkiraan, style = MaterialTheme.typography.bodySmall)
    }
}

@Composable
private fun BarisNilai(label: String, isi: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.bodyMedium)
        Text(isi, color = MaterialTheme.colorScheme.primary)
    }
}
