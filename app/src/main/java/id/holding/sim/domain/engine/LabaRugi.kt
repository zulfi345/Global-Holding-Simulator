package id.holding.sim.domain.engine

/** Laporan laba rugi satu periode untuk satu perusahaan. Semua nilai dalam satuan uang yang sama. */
data class LaporanLabaRugi(
    val pendapatan: Double,
    val hpp: Double,
    val biayaOperasi: Double,
    val penyusutan: Double,
    val biayaBunga: Double,
    val pendapatanLain: Double,
    val labaKotor: Double,
    val ebitda: Double,
    val ebit: Double,
    val labaSebelumPajak: Double,
    val pajak: Double,
    val labaBersih: Double,
    val sisaRugiFiskal: Double
) {
    val marginBersih: Double
        get() = if (pendapatan > 0.0) labaBersih / pendapatan else 0.0
}

object LabaRugiEngine {
    /**
     * @param hpp harga pokok penjualan. Untuk bank/asuransi isi dengan biaya dana / klaim, BUKAN bahan baku.
     * @param rugiBawaan saldo rugi fiskal dari periode sebelumnya
     * @param tarifPphPct tarif PPh badan negara (persen)
     */
    fun hitung(
        pendapatan: Double,
        hpp: Double,
        biayaOperasi: Double,
        penyusutan: Double,
        biayaBunga: Double,
        pendapatanLain: Double,
        rugiBawaan: Double,
        tarifPphPct: Double
    ): LaporanLabaRugi {
        val labaKotor = pendapatan - hpp
        val ebitda = labaKotor - biayaOperasi
        val ebit = ebitda - penyusutan
        val labaSebelumPajak = ebit - biayaBunga + pendapatanLain
        val pph = PajakEngine.pphBadan(labaSebelumPajak, rugiBawaan, tarifPphPct)
        return LaporanLabaRugi(
            pendapatan = pendapatan,
            hpp = hpp,
            biayaOperasi = biayaOperasi,
            penyusutan = penyusutan,
            biayaBunga = biayaBunga,
            pendapatanLain = pendapatanLain,
            labaKotor = labaKotor,
            ebitda = ebitda,
            ebit = ebit,
            labaSebelumPajak = labaSebelumPajak,
            pajak = pph.pajak,
            labaBersih = labaSebelumPajak - pph.pajak,
            sisaRugiFiskal = pph.sisaRugi
        )
    }
}
