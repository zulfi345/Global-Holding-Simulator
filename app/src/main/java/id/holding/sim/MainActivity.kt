package id.holding.sim

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import id.holding.sim.data.HoldingPenyimpanan
import id.holding.sim.data.NegaraLoader
import id.holding.sim.data.RivalLoader
import id.holding.sim.data.UsahaLoader
import id.holding.sim.domain.holding.BeliUsaha
import id.holding.sim.domain.holding.JualUsaha
import id.holding.sim.domain.holding.LanjutBulan
import id.holding.sim.domain.holding.Pinjam
import id.holding.sim.domain.engine.Kredit
import id.holding.sim.domain.holding.Holding
import id.holding.sim.domain.rival.DuniaRival
import kotlinx.coroutines.delay
import id.holding.sim.ui.DashboardScreen
import id.holding.sim.ui.KecepatanJam
import id.holding.sim.ui.PetaScreen
import id.holding.sim.ui.TickerPasar
import id.holding.sim.ui.teksTicker
import id.holding.sim.ui.PilihModalScreen
import id.holding.sim.ui.RankingPdbScreen
import id.holding.sim.ui.RankingRivalScreen
import id.holding.sim.ui.TemaGame
import id.holding.sim.ui.UsahaScreen
import id.holding.sim.ui.teksAkuisisi

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TemaGame {
                val negara = remember { NegaraLoader.muatSemua(this@MainActivity) }
                val usahaSemua = remember { UsahaLoader.muatSemua(this@MainActivity) }
                val rival = remember { RivalLoader.muatSemua(this@MainActivity) }
                // null = belum mulai -> layar pilih modal awal. Disimpan di SharedPreferences.
                var holding by remember { mutableStateOf<Holding?>(HoldingPenyimpanan.muat(this@MainActivity)) }
                var tab by remember { mutableStateOf(0) }
                var kecepatan by remember { mutableStateOf(KecepatanJam.JEDA) }
                val lanjutBulan: () -> Unit = {
                    val h = holding
                    if (h != null) {
                        val tarif = negara.firstOrNull { it.kode == h.negaraBasis }?.pphBadanPct ?: 0.0
                        val baru = LanjutBulan.maju(h, tarif)
                        HoldingPenyimpanan.simpan(this@MainActivity, baru)
                        holding = baru
                    }
                }
                // Jam game otomatis: tiap jedaMs maju 1 bulan; Jeda = tidak berjalan.
                LaunchedEffect(kecepatan) {
                    if (kecepatan.jedaMs > 0L) {
                        while (true) {
                            delay(kecepatan.jedaMs)
                            lanjutBulan()
                        }
                    }
                }
                val holdingAktif = holding
                val bulanSaatIni = holdingAktif?.bulan ?: 0
                val berita = remember(rival, bulanSaatIni) {
                    DuniaRival.akuisisiBulan(rival, bulanSaatIni).map { teksAkuisisi(it, rival) }
                }
                val negaraBasis = negara.firstOrNull { it.kode == holdingAktif?.negaraBasis }
                val tarifPph = negaraBasis?.pphBadanPct ?: 0.0
                // SEMENTARA: bunga pinjaman = bunga acuan negara basis + premi tetap 3 poin (belum PremiRisiko).
                val bungaPinjam = Kredit.bungaPinjamanPct(negaraBasis?.bungaAcuanPct ?: 0.0, 3.0)
                if (holdingAktif == null) {
                    PilihModalScreen(negara) {
                        HoldingPenyimpanan.simpan(this@MainActivity, it)
                        holding = it
                        tab = 0
                    }
                } else {
                    Scaffold(
                        bottomBar = {
                            NavigationBar {
                                NavigationBarItem(
                                    selected = tab == 0,
                                    onClick = { tab = 0 },
                                    icon = { },
                                    label = { Text("Dashboard") }
                                )
                                NavigationBarItem(
                                    selected = tab == 4,
                                    onClick = { tab = 4 },
                                    icon = { },
                                    label = { Text("Peta") }
                                )
                                NavigationBarItem(
                                    selected = tab == 3,
                                    onClick = { tab = 3 },
                                    icon = { },
                                    label = { Text("Usaha") }
                                )
                                NavigationBarItem(
                                    selected = tab == 1,
                                    onClick = { tab = 1 },
                                    icon = { },
                                    label = { Text("Negara") }
                                )
                                NavigationBarItem(
                                    selected = tab == 2,
                                    onClick = { tab = 2 },
                                    icon = { },
                                    label = { Text("Rival") }
                                )
                            }
                        }
                    ) { padding ->
                        Column(modifier = Modifier.padding(padding)) {
                            TickerPasar(teksTicker(negaraBasis, holdingAktif))
                            Box(modifier = Modifier.weight(1f)) {
                            when (tab) {
                                0 -> DashboardScreen(holdingAktif, berita = berita, kecepatan = kecepatan, onUbahKecepatan = { kecepatan = kecepatan.berikut() }, bungaPinjamPct = bungaPinjam, onPinjam = {
                                    val baru = Pinjam.pinjam(holdingAktif, 1e9, bungaPinjam)
                                    if (baru != null) {
                                        HoldingPenyimpanan.simpan(this@MainActivity, baru)
                                        holding = baru
                                    }
                                }, onLunasi = {
                                    val baru = Pinjam.lunasi(holdingAktif, 1e9)
                                    if (baru != null) {
                                        HoldingPenyimpanan.simpan(this@MainActivity, baru)
                                        holding = baru
                                    }
                                }, onLanjutBulan = lanjutBulan, onMulaiUlang = {
                                    HoldingPenyimpanan.hapus(this@MainActivity)
                                    holding = null
                                })
                                3 -> UsahaScreen(usahaSemua, holdingAktif, onBeli = { u ->
                                    val hasil = BeliUsaha.beli(holdingAktif, u)
                                    val baru = hasil.holding
                                    if (baru != null) {
                                        HoldingPenyimpanan.simpan(this@MainActivity, baru)
                                        holding = baru
                                    }
                                }, onJual = { u ->
                                    val baru = JualUsaha.jual(holdingAktif, u.id)
                                    if (baru != null) {
                                        HoldingPenyimpanan.simpan(this@MainActivity, baru)
                                        holding = baru
                                    }
                                })
                                1 -> RankingPdbScreen(negara)
                                4 -> PetaScreen(negara, holdingAktif.negaraBasis)
                                else -> RankingRivalScreen(rival, holdingAktif.bulan)
                            }
                            }
                        }
                    }
                }
            }
        }
    }
}
