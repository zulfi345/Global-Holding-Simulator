package id.holding.sim.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import id.holding.sim.data.Negara
import id.holding.sim.domain.holding.Holding
import id.holding.sim.domain.holding.ModalAwal

internal const val NAMA_HOLDING_MAKS = 40

/** Nama holding dirapikan (trim, dibatasi 40 huruf); kosong -> null. */
internal fun namaHoldingValid(mentah: String): String? {
    val n = mentah.trim().take(NAMA_HOLDING_MAKS)
    return if (n.isEmpty()) null else n
}

/** Negara basis awal: "ID" bila ada, selain itu negara pertama; daftar kosong -> null. */
internal fun negaraBasisAwal(daftar: List<Negara>): Negara? =
    daftar.firstOrNull { it.kode == "ID" } ?: daftar.firstOrNull()

/** Layar awal: pemain memilih nama, negara basis, dan modal awal (5 pilihan). */
@Composable
fun PilihModalScreen(negara: List<Negara>, onMulai: (Holding) -> Unit) {
    val urut = remember(negara) { negara.sortedBy { it.nama } }
    var nama by remember { mutableStateOf("Holding Saya") }
    var kodeBasis by remember { mutableStateOf(negaraBasisAwal(urut)?.kode) }
    var modalMiliar by remember { mutableStateOf(ModalAwal.PILIHAN_MILIAR[1]) }
    var menuTerbuka by remember { mutableStateOf(false) }
    val basis = urut.firstOrNull { it.kode == kodeBasis }
    val namaValid = namaHoldingValid(nama)

    Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp)) {
        Text("Mulai Holding Baru", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = nama,
            onValueChange = { nama = it.take(NAMA_HOLDING_MAKS) },
            label = { Text("Nama holding") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text("Negara basis", style = MaterialTheme.typography.titleMedium)
        OutlinedButton(onClick = { menuTerbuka = true }, modifier = Modifier.fillMaxWidth()) {
            Text(if (basis == null) "Pilih negara" else basis.nama + " (" + basis.kode + ")")
        }
        DropdownMenu(expanded = menuTerbuka, onDismissRequest = { menuTerbuka = false }) {
            for (n in urut) {
                DropdownMenuItem(
                    text = { Text(n.nama + " (" + n.kode + ")") },
                    onClick = {
                        kodeBasis = n.kode
                        menuTerbuka = false
                    }
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text("Modal awal", style = MaterialTheme.typography.titleMedium)
        for (m in ModalAwal.PILIHAN_MILIAR) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .selectable(selected = modalMiliar == m, onClick = { modalMiliar = m }, role = Role.RadioButton)
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                RadioButton(selected = modalMiliar == m, onClick = null)
                Text("Rp" + m + " miliar", modifier = Modifier.padding(start = 12.dp))
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = {
                if (namaValid != null && basis != null) {
                    onMulai(Holding.mulai(namaValid, basis.kode, modalMiliar))
                }
            },
            enabled = namaValid != null && basis != null,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Mulai")
        }
    }
}
