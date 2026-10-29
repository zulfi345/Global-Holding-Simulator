package id.holding.sim.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

/** Titik (x,y) piksel untuk grafik garis; y=0 di atas. 1 nilai -> garis datar di tengah; kosong -> kosong. */
internal fun titikGrafik(nilai: List<Double>, lebar: Float, tinggi: Float, margin: Float = 8f): List<Pair<Float, Float>> {
    if (nilai.isEmpty()) return emptyList()
    val minV = nilai.min()
    val maxV = nilai.max()
    val rentang = maxV - minV
    val w = lebar - 2 * margin
    val h = tinggi - 2 * margin
    return nilai.mapIndexed { i, v ->
        val x = if (nilai.size == 1) lebar / 2f else margin + w * i / (nilai.size - 1)
        val y = if (rentang <= 0.0) tinggi / 2f else margin + h * (1.0 - (v - minV) / rentang).toFloat()
        Pair(x, y)
    }
}

@Composable
fun GrafikValuasi(riwayat: List<Double>) {
    val warna = MaterialTheme.colorScheme.primary
    Canvas(modifier = Modifier.fillMaxWidth().height(140.dp)) {
        val titik = titikGrafik(riwayat, size.width, size.height)
        if (titik.size >= 2) {
            val path = Path()
            path.moveTo(titik[0].first, titik[0].second)
            for (i in 1 until titik.size) path.lineTo(titik[i].first, titik[i].second)
            drawPath(path, warna, style = Stroke(width = 4f))
        } else if (titik.size == 1) {
            drawCircle(warna, radius = 6f, center = Offset(titik[0].first, titik[0].second))
        }
    }
}
