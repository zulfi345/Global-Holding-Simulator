package id.holding.sim.domain.engine

/**
 * Kelayakan dividen berdasarkan cum-date/ex-date. Game bertick bulanan (lihat MakroEngine dkk),
 * jadi granularitas di sini bulan, bukan tanggal harian seperti bursa sungguhan.
 * bulanBeli/bulanCum/bulanJual: nomor bulan sejak awal game (naik monoton), bukan 1-12.
 */
object DividenCumEx {
    /**
     * Berhak atas dividen periode ini jika posisi sudah dimiliki pada/sebelum bulanCum
     * (dibeli pada atau sebelum cum-date) DAN masih dipegang melewati bulanCum
     * (belum dijual, atau baru dijual setelah bulanCum -- dijual PADA bulanCum sendiri
     * dianggap sudah ex-date, tidak berhak, sesuai konvensi T+ di bursa sungguhan).
     */
    fun berhakDividen(bulanBeli: Int, bulanCum: Int, bulanJual: Int? = null): Boolean =
        bulanBeli <= bulanCum && (bulanJual == null || bulanJual > bulanCum)
}
