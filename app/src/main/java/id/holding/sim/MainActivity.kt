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
import id.holding.sim.data.NegaraLoader
import id.holding.sim.data.RivalLoader
import id.holding.sim.domain.holding.Holding
import id.holding.sim.ui.DashboardScreen
import id.holding.sim.ui.RankingPdbScreen
import id.holding.sim.ui.RankingRivalScreen
import id.holding.sim.ui.TemaGame

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TemaGame {
                val negara = remember { NegaraLoader.muatSemua(this@MainActivity) }
                val rival = remember { RivalLoader.muatSemua(this@MainActivity) }
                // Sementara: holding contoh sampai ada layar pilih modal awal.
                val holding = remember { Holding.mulai("Holding Saya", "ID", 40) }
                var tab by remember { mutableStateOf(0) }
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
                            0 -> DashboardScreen(holding)
                            1 -> RankingPdbScreen(negara)
                            else -> RankingRivalScreen(rival)
                        }
                    }
                }
            }
        }
    }
}
