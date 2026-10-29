package id.holding.sim.ui

/** Keadaan geser/zoom peta: skala dan posisi pojok kiri-atas peta di layar (piksel). */
data class TransformPeta(val skala: Float, val offsetX: Float, val offsetY: Float)

/**
 * Matematika peta dunia proyeksi equirectangular 2:1 (bujur -180..180 -> kiri..kanan, lintang 90..-90 ->
 * atas..bawah), cocok dengan assets/peta/world-map.jpg. Ukuran dasar peta (skala 1) selalu menutupi layar.
 */
object PetaMath {
    const val SKALA_MIN = 1f
    const val SKALA_MAKS = 8f

    fun xNorm(bujur: Double): Double = (bujur + 180.0) / 360.0
    fun yNorm(lintang: Double): Double = (90.0 - lintang) / 180.0

    /** (lebar, tinggi) peta pada skala 1; rasio 2:1 dan tinggi >= tinggi layar. */
    fun ukuranDasar(viewW: Float, viewH: Float): Pair<Float, Float> {
        val w = maxOf(2f * viewH, viewW)
        return Pair(w, w / 2f)
    }

    /** Skala dibatasi SKALA_MIN..SKALA_MAKS; peta tak boleh meninggalkan celah di tepi layar. */
    fun batasi(t: TransformPeta, viewW: Float, viewH: Float): TransformPeta {
        val (bw, bh) = ukuranDasar(viewW, viewH)
        val s = t.skala.coerceIn(SKALA_MIN, SKALA_MAKS)
        val w = bw * s
        val h = bh * s
        val ox = if (w <= viewW) (viewW - w) / 2f else t.offsetX.coerceIn(viewW - w, 0f)
        val oy = if (h <= viewH) (viewH - h) / 2f else t.offsetY.coerceIn(viewH - h, 0f)
        return TransformPeta(s, ox, oy)
    }

    /** Terapkan geseran (pan) dan zoom di sekitar titik (cx, cy): titik itu tetap di tempatnya. */
    fun terapkan(
        t: TransformPeta, panX: Float, panY: Float, zoom: Float,
        cx: Float, cy: Float, viewW: Float, viewH: Float
    ): TransformPeta {
        val s = (t.skala * zoom).coerceIn(SKALA_MIN, SKALA_MAKS)
        val rasio = s / t.skala
        val ox = cx - (cx - t.offsetX) * rasio + panX
        val oy = cy - (cy - t.offsetY) * rasio + panY
        return batasi(TransformPeta(s, ox, oy), viewW, viewH)
    }

    /** Pusatkan layar pada (lintang, bujur) dengan skala tertentu (setelah dibatasi). */
    fun pusatkan(lintang: Double, bujur: Double, skala: Float, viewW: Float, viewH: Float): TransformPeta {
        val s = skala.coerceIn(SKALA_MIN, SKALA_MAKS)
        val (bw, bh) = ukuranDasar(viewW, viewH)
        val px = (xNorm(bujur) * bw * s).toFloat()
        val py = (yNorm(lintang) * bh * s).toFloat()
        return batasi(TransformPeta(s, viewW / 2f - px, viewH / 2f - py), viewW, viewH)
    }

    /** Posisi layar (x, y) sebuah titik peta. */
    fun posisiLayar(lintang: Double, bujur: Double, t: TransformPeta, viewW: Float, viewH: Float): Pair<Float, Float> {
        val (bw, bh) = ukuranDasar(viewW, viewH)
        val x = t.offsetX + (xNorm(bujur) * bw * t.skala).toFloat()
        val y = t.offsetY + (yNorm(lintang) * bh * t.skala).toFloat()
        return Pair(x, y)
    }
}
