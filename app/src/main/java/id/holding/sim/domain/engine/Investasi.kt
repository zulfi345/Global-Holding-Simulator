package id.holding.sim.domain.engine

/**
 * Hasil satu transaksi beli/jual aset.
 * jumlahUnit sudah dibulatkan ke kelipatan ukuranLot (sisa unit yang tak cukup 1 lot diabaikan).
 * nilaiBersih untuk beli = nilaiKotor + biayaBroker + pajakTransaksi (total yang dibayar pembeli);
 * untuk jual = nilaiKotor - biayaBroker - pajakTransaksi (yang diterima penjual).
 */
data class HasilTransaksiAset(
    val jumlahLot: Int,
    val jumlahUnit: Int,
    val nilaiKotor: Double,
    val biayaBroker: Double,
    val pajakTransaksi: Double,
    val nilaiBersih: Double
)

/**
 * Transaksi investasi generik untuk semua jenis aset (saham, komoditas, dll).
 * biayaBrokerPct & pajakTransaksiPct: persen dari nilai kotor, desain game/perkiraan
 * (acuan kasar bursa saham ritel; belum dibedakan per bursa/jenis aset).
 */
object Investasi {
    fun beli(
        hargaPerUnit: Double,
        ukuranLot: Int,
        unitDiminta: Int,
        biayaBrokerPct: Double = 0.15,
        pajakTransaksiPct: Double = 0.0
    ): HasilTransaksiAset {
        val jumlahLot = unitDiminta / ukuranLot
        val jumlahUnit = jumlahLot * ukuranLot
        val nilaiKotor = jumlahUnit * hargaPerUnit
        val biayaBroker = nilaiKotor * biayaBrokerPct / 100.0
        val pajakTransaksi = nilaiKotor * pajakTransaksiPct / 100.0
        return HasilTransaksiAset(
            jumlahLot, jumlahUnit, nilaiKotor, biayaBroker, pajakTransaksi,
            nilaiKotor + biayaBroker + pajakTransaksi
        )
    }

    fun jual(
        hargaPerUnit: Double,
        ukuranLot: Int,
        unitDiminta: Int,
        biayaBrokerPct: Double = 0.15,
        pajakTransaksiPct: Double = 0.1
    ): HasilTransaksiAset {
        val jumlahLot = unitDiminta / ukuranLot
        val jumlahUnit = jumlahLot * ukuranLot
        val nilaiKotor = jumlahUnit * hargaPerUnit
        val biayaBroker = nilaiKotor * biayaBrokerPct / 100.0
        val pajakTransaksi = nilaiKotor * pajakTransaksiPct / 100.0
        return HasilTransaksiAset(
            jumlahLot, jumlahUnit, nilaiKotor, biayaBroker, pajakTransaksi,
            nilaiKotor - biayaBroker - pajakTransaksi
        )
    }
}
