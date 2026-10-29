package id.holding.sim.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import id.holding.sim.data.Negara
import id.holding.sim.domain.holding.Holding
import id.holding.sim.domain.holding.TanggalGame
import kotlinx.coroutines.delay
import java.util.Locale

private val LokalTicker: Locale = Locale.forLanguageTag("id-ID")

/**
 * Isi ticker pasar (makro negara basis + status holding). Angka makro = data negara (perkiraan 2026-10,
 * belum bergerak tiap bulan di UI). Negara null -> hanya tanggal & valuasi.
 */
internal fun teksTicker(negara: Negara?, holding: Holding): List<String> {
    val hasil = ArrayList<String>()
    hasil.add(TanggalGame.teks(holding.bulan) + " \u00B7 valuasi " + formatRupiahSingkat(holding.valuasiRupiah))
    if (negara != null) {
        hasil.add(negara.nama + " \u00B7 bunga acuan " + String.format(LokalTicker, "%.2f", negara.bungaAcuanPct) + "%")
        hasil.add(negara.nama + " \u00B7 inflasi " + String.format(LokalTicker, "%.1f", negara.inflasiPct) + "%")
        hasil.add(negara.nama + " \u00B7 PDB tumbuh " + String.format(LokalTicker, "%.1f", negara.pertumbuhanPct) + "%")
        hasil.add("Kurs " + negara.mataUang + " " + String.format(LokalTicker, "%,.2f", negara.kursPerUsd) + " per USD")
    }
    return hasil
}

/** Satu baris ticker yang berganti isi tiap 3 detik. */
@Composable
fun TickerPasar(items: List<String>) {
    var indeks by remember { mutableStateOf(0) }
    LaunchedEffect(items.size) {
        while (true) {
            delay(3000L)
            if (items.isNotEmpty()) indeks = (indeks + 1) % items.size
        }
    }
    Surface(color = MaterialTheme.colorScheme.surfaceVariant, modifier = Modifier.fillMaxWidth()) {
        Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
            val teks = if (items.isEmpty()) "" else items[indeks % items.size]
            Text(teks, style = MaterialTheme.typography.bodySmall, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
    }
}
