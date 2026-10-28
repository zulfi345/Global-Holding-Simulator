package id.holding.sim.domain.usaha

import id.holding.sim.data.Usaha

/**
 * Strategi layanan teregulasi (rumah sakit, sekolah, telekomunikasi, media, air & utilitas):
 * tarif dibatasi regulator, biaya kepatuhan, dan masa berlaku lisensi.
 * Tarif kepatuhan bawaan adalah perkiraan; isi per usaha dan negara bila ada data.
 * Bulan lisensi dinyatakan sebagai nomor bulan berurutan sejak awal permainan.
 */
class StrategiLayananRegulasi(val kepatuhanPct: Double = 3.0) : ArketipeUsaha {
    override val arketipe: Arketipe = Arketipe.LAYANAN_REGULASI
    override val punyaBahanBaku: Boolean = true

    override fun strukturBiaya(usaha: Usaha, pendapatan: Double): StrukturBiaya = StrukturBiaya(
        hpp = pendapatan * usaha.hppPct / 100.0,
        biayaOperasi = pendapatan * usaha.biayaOperasiPct / 100.0,
        penyusutan = pendapatan * usaha.penyusutanPct / 100.0
    )

    /** Tarif efektif = tarif yang diminta, dibatasi plafon regulator. */
    fun tarifEfektif(tarifDiminta: Double, plafonRegulator: Double): Double =
        minOf(tarifDiminta, plafonRegulator)

    /** Pendapatan = volume layanan x tarif efektif. */
    fun pendapatan(volume: Double, tarifDiminta: Double, plafonRegulator: Double): Double =
        volume.coerceAtLeast(0.0) * tarifEfektif(tarifDiminta, plafonRegulator)

    /** Biaya kepatuhan periode = pendapatan x kepatuhanPct. */
    fun biayaKepatuhan(pendapatan: Double): Double = pendapatan * kepatuhanPct / 100.0

    /** Lisensi berlaku selama bulan sekarang <= bulan kedaluwarsa. */
    fun lisensiBerlaku(bulanSekarang: Int, bulanKedaluwarsa: Int): Boolean =
        bulanSekarang <= bulanKedaluwarsa

    /** Sisa bulan lisensi; 0 bila sudah kedaluwarsa. */
    fun sisaBulanLisensi(bulanSekarang: Int, bulanKedaluwarsa: Int): Int =
        (bulanKedaluwarsa - bulanSekarang).coerceAtLeast(0)
}
