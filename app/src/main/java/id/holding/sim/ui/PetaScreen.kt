package id.holding.sim.ui

import android.graphics.BitmapFactory
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import id.holding.sim.data.KoordinatNegara
import id.holding.sim.data.Negara
import kotlin.math.roundToInt

private val WarnaBasis = Color(0xFFD4A84B)
private val WarnaChip = Color(0xCC0E1A24)

/**
 * Peta dunia (assets/peta/world-map.jpg): geser & cubit untuk zoom, titik tiap negara dari
 * KoordinatNegara, baris chip negara di atas (basis dulu, lalu PDB terbesar), ketuk titik -> rincian negara.
 */
@Composable
fun PetaScreen(negara: List<Negara>, negaraBasis: String) {
    var dipilih by remember { mutableStateOf<Negara?>(null) }
    var tfSimpan by remember { mutableStateOf<TransformPeta?>(null) }
    val terpilih = dipilih
    if (terpilih != null) {
        RincianNegaraScreen(terpilih, onKembali = { dipilih = null })
        return
    }
    val konteks = LocalContext.current
    val kepadatan = LocalDensity.current
    val bitmap = remember {
        try {
            konteks.assets.open("peta/world-map.jpg").use { BitmapFactory.decodeStream(it) }?.asImageBitmap()
        } catch (e: Exception) {
            null
        }
    }
    val urut = remember(negara, negaraBasis) {
        negara.sortedWith(compareBy<Negara> { it.kode != negaraBasis }.thenByDescending { it.pdbUsdMiliar })
    }
    if (bitmap == null) {
        Text("Peta gagal dimuat.", modifier = Modifier.padding(16.dp))
        return
    }
    BoxWithConstraints(modifier = Modifier.fillMaxSize().clipToBounds()) {
        val viewW = constraints.maxWidth.toFloat()
        val viewH = constraints.maxHeight.toFloat()
        val awal = {
            val k = KoordinatNegara.cari(negaraBasis)
            if (k != null) PetaMath.pusatkan(k.first, k.second, 2f, viewW, viewH)
            else PetaMath.batasi(TransformPeta(1f, 0f, 0f), viewW, viewH)
        }
        val tf = PetaMath.batasi(tfSimpan ?: awal(), viewW, viewH)
        val (bw, bh) = PetaMath.ukuranDasar(viewW, viewH)
        val radius = with(kepadatan) { 14.dp.toPx() }
        val labelLebar = with(kepadatan) { 96.dp.toPx() }
        val labelNaik = with(kepadatan) { 34.dp.toPx() }

        Canvas(
            modifier = Modifier.fillMaxSize().clipToBounds().pointerInput(viewW, viewH) {
                detectTransformGestures { pusat, geser, zoom, _ ->
                    val sekarang = tfSimpan ?: awal()
                    tfSimpan = PetaMath.terapkan(sekarang, geser.x, geser.y, zoom, pusat.x, pusat.y, viewW, viewH)
                }
            }
        ) {
            drawImage(
                image = bitmap,
                srcOffset = IntOffset.Zero,
                srcSize = IntSize(bitmap.width, bitmap.height),
                dstOffset = IntOffset(tf.offsetX.roundToInt(), tf.offsetY.roundToInt()),
                dstSize = IntSize((bw * tf.skala).roundToInt(), (bh * tf.skala).roundToInt()),
                filterQuality = FilterQuality.Medium
            )
        }

        for (n in urut) {
            val k = KoordinatNegara.cari(n.kode) ?: continue
            val (x, y) = PetaMath.posisiLayar(k.first, k.second, tf, viewW, viewH)
            if (x < -radius || x > viewW + radius || y < -radius || y > viewH + radius) continue
            val basis = n.kode == negaraBasis
            if (basis || tf.skala >= 2.5f) {
                Text(
                    n.nama,
                    color = Color.White,
                    style = MaterialTheme.typography.labelMedium,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    modifier = Modifier
                        .width(96.dp)
                        .offset { IntOffset((x - labelLebar / 2f).roundToInt(), (y - labelNaik).roundToInt()) }
                )
            }
            Box(
                modifier = Modifier
                    .offset { IntOffset((x - radius).roundToInt(), (y - radius).roundToInt()) }
                    .size(28.dp)
                    .clickable { dipilih = n },
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(if (basis) 18.dp else 12.dp)
                        .background(if (basis) WarnaBasis else MaterialTheme.colorScheme.primary, CircleShape)
                        .border(2.dp, Color.White, CircleShape)
                )
            }
        }

        LazyRow(
            modifier = Modifier.align(Alignment.TopStart).padding(top = 8.dp),
            contentPadding = PaddingValues(horizontal = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(urut) { n ->
                Surface(
                    shape = RoundedCornerShape(50),
                    color = WarnaChip,
                    modifier = Modifier.clickable {
                        val k = KoordinatNegara.cari(n.kode)
                        if (k != null) tfSimpan = PetaMath.pusatkan(k.first, k.second, 3f, viewW, viewH)
                    }
                ) {
                    Text(
                        n.nama,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }
            }
        }

        Column(modifier = Modifier.align(Alignment.CenterEnd).padding(end = 12.dp)) {
            TombolZoom("+") {
                tfSimpan = PetaMath.terapkan(tfSimpan ?: awal(), 0f, 0f, 1.5f, viewW / 2f, viewH / 2f, viewW, viewH)
            }
            TombolZoom("\u2212") {
                tfSimpan = PetaMath.terapkan(tfSimpan ?: awal(), 0f, 0f, 1f / 1.5f, viewW / 2f, viewH / 2f, viewW, viewH)
            }
        }

        Text(
            "Geser peta \u00B7 ketuk titik negara untuk masuk",
            color = Color.White,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 10.dp)
        )
    }
}

@Composable
private fun TombolZoom(teks: String, onKlik: () -> Unit) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = WarnaChip,
        modifier = Modifier.padding(vertical = 4.dp).size(48.dp).clickable(onClick = onKlik)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(teks, color = Color.White, style = MaterialTheme.typography.titleLarge)
        }
    }
}
