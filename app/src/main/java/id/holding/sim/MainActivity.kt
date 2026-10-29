package id.holding.sim

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import id.holding.sim.domain.holding.Holding
import id.holding.sim.domain.rival.DuniaRival
import id.holding.sim.ui.DashboardScreen
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
                val holdingAktif = holding
                val bulanSaatIni = holdingAktif?.bulan ?: 0
                val berita = remember(rival, bulanSaatIni) {
                    DuniaRival.akuisisiBulan(rival, bulanSaatIni).map { teksAkuisisi(it, rival) }
                }
                val tarifPph = negara.firstOrNull { it.kode == holdingAktif?.negaraBasis }?.pphBadanPct ?: 0.0
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
                        Box(modifier = Modifier.padding(padding)) {
                            when (tab) {
                                0 -> DashboardScreen(holdingAktif, berita = berita, onLanjutBulan = {
                                    val baru = LanjutBulan.maju(holdingAktif, tarifPph)
                                    HoldingPenyimpanan.simpan(this@MainActivity, baru)
                                    holding = baru
                                }, onMulaiUlang = {
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
                                else -> RankingRivalScreen(rival, holdingAktif.bulan)
                            }
                        }
                    }
                }
            }
        }
    }
}
