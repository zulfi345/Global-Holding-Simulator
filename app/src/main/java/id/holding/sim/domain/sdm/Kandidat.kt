package id.holding.sim.domain.sdm

/** Lima bidang kemampuan kandidat. */
enum class Bidang { PIMPINAN, KEUANGAN, OPERASI, TEKNOLOGI, SDM }

/**
 * Jabatan eksekutif beserta bobot kemampuan (persen, jumlah 100).
 * Bobot adalah parameter desain game, bukan data dunia nyata.
 */
enum class Jabatan(val bobot: Map<Bidang, Double>) {
    CEO(mapOf(Bidang.PIMPINAN to 50.0, Bidang.KEUANGAN to 15.0, Bidang.OPERASI to 15.0, Bidang.TEKNOLOGI to 10.0, Bidang.SDM to 10.0)),
    CFO(mapOf(Bidang.KEUANGAN to 60.0, Bidang.PIMPINAN to 20.0, Bidang.OPERASI to 10.0, Bidang.TEKNOLOGI to 5.0, Bidang.SDM to 5.0)),
    COO(mapOf(Bidang.OPERASI to 60.0, Bidang.PIMPINAN to 20.0, Bidang.SDM to 10.0, Bidang.KEUANGAN to 5.0, Bidang.TEKNOLOGI to 5.0)),
    CTO(mapOf(Bidang.TEKNOLOGI to 60.0, Bidang.PIMPINAN to 20.0, Bidang.OPERASI to 10.0, Bidang.KEUANGAN to 5.0, Bidang.SDM to 5.0)),
    CHRO(mapOf(Bidang.SDM to 60.0, Bidang.PIMPINAN to 20.0, Bidang.OPERASI to 10.0, Bidang.KEUANGAN to 5.0, Bidang.TEKNOLOGI to 5.0))
}

/**
 * Kandidat pegawai. Kemampuan per bidang 0..100 (di luar rentang dibatasi, bidang kosong = 0).
 * gajiBulanan dalam satuan mata uang yang sama dengan biaya rekrutmen.
 */
data class Kandidat(
    val id: String,
    val nama: String,
    val kemampuan: Map<Bidang, Int>,
    val gajiBulanan: Double
) {
    fun nilai(bidang: Bidang): Int = (kemampuan[bidang] ?: 0).coerceIn(0, 100)

    /** Skor kecocokan untuk jabatan, 0..100 = rata-rata tertimbang kemampuan. */
    fun skorJabatan(jabatan: Jabatan): Double =
        jabatan.bobot.entries.sumOf { (b, w) -> w * nilai(b) } / 100.0
}

object Rekrutmen {
    /** Biaya agen = gaji tahunan x fee (persen); 0 bila tanpa agen. Fee bawaan 20% adalah perkiraan. */
    fun biayaRekrutmen(gajiBulanan: Double, pakaiAgen: Boolean, feeAgenPct: Double = 20.0): Double =
        if (pakaiAgen) gajiBulanan * 12.0 * feeAgenPct / 100.0 else 0.0

    /** Lama pencarian (bulan): agen lebih cepat. Nilai adalah parameter desain game. */
    fun masaPencarianBulan(pakaiAgen: Boolean): Int = if (pakaiAgen) 1 else 3
}
