package id.holding.sim.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.graphics.Color

private val SkemaGelap = darkColorScheme(
    primary = Color(0xFF5EEAD4),
    background = Color(0xFF0B1F1E),
    surface = Color(0xFF0B1F1E),
    onBackground = Color(0xFFE6F4F1),
    onSurface = Color(0xFFE6F4F1)
)

@Composable
fun TemaGame(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = SkemaGelap) {
        Surface(modifier = Modifier.fillMaxSize()) {
            content()
        }
    }
}
