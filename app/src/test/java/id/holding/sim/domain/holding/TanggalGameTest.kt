package id.holding.sim.domain.holding

import org.junit.Assert.assertEquals
import org.junit.Test

class TanggalGameTest {
    @Test fun awalJanuari2026() = assertEquals("Jan 2026", TanggalGame.teks(0))
    @Test fun akhirTahunPertama() = assertEquals("Des 2026", TanggalGame.teks(11))
    @Test fun gantiTahun() = assertEquals("Jan 2027", TanggalGame.teks(12))
    @Test fun sepuluhTahun() = assertEquals("Mei 2036", TanggalGame.teks(124))
    @Test(expected = IllegalArgumentException::class) fun negatifDitolak() { TanggalGame.teks(-1) }
}
