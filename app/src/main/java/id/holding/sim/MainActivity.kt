package id.holding.sim

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.remember
import id.holding.sim.data.NegaraLoader
import id.holding.sim.ui.RankingPdbScreen
import id.holding.sim.ui.TemaGame

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TemaGame {
                val negara = remember { NegaraLoader.muatSemua(this@MainActivity) }
                RankingPdbScreen(negara)
            }
        }
    }
}
