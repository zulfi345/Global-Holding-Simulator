package id.holding.sim.domain.engine

import kotlin.random.Random

/**
 * Mesin makro sederhana: tiap bulan, pertumbuhan/inflasi/bunga bergerak kembali
 * ke nilai dasar negara (mean reversion) ditambah guncangan acak.
 * Kurs belum diubah di sini (lihat KursEngine).
 */
object MakroEngine {
    /**
     * @param guncanganGlobal poin persen untuk pertumbuhan (negatif = resesi global)
     * @param noiseSkala 1.0 normal; 0.0 = tanpa acak (untuk uji)
     */
    fun tickBulanan(
        sekarang: MakroState,
        dasar: MakroState,
        acak: Random,
        guncanganGlobal: Double = 0.0,
        noiseSkala: Double = 1.0
    ): MakroState {
        val n1 = (acak.nextDouble() * 2.0 - 1.0) * noiseSkala
        val n2 = (acak.nextDouble() * 2.0 - 1.0) * noiseSkala

        val pertumbuhan = (sekarang.pertumbuhanPct +
            0.15 * (dasar.pertumbuhanPct - sekarang.pertumbuhanPct) +
            0.3 * n1 + guncanganGlobal).coerceIn(-8.0, 15.0)

        val inflasi = (sekarang.inflasiPct +
            0.1 * (dasar.inflasiPct - sekarang.inflasiPct) +
            0.05 * (pertumbuhan - dasar.pertumbuhanPct) +
            0.15 * n2).coerceIn(-2.0, 25.0)

        val bungaTarget = dasar.bungaPct + 0.5 * (inflasi - dasar.inflasiPct)
        val bunga = (sekarang.bungaPct + 0.2 * (bungaTarget - sekarang.bungaPct))
            .coerceIn(0.0, 25.0)

        val pdb = sekarang.pdbUsdMiliar * (1.0 + pertumbuhan / 100.0 / 12.0)

        return sekarang.copy(
            pertumbuhanPct = pertumbuhan,
            inflasiPct = inflasi,
            bungaPct = bunga,
            pdbUsdMiliar = pdb
        )
    }
}
