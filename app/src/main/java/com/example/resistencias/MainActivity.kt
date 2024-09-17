import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResistorCalculatorScreen() {
    val context = LocalContext.current

    // Estados para almacenar los valores seleccionados
    var banda1 by remember { mutableStateOf("") }
    var banda2 by remember { mutableStateOf("") }
    var multiplicador by remember { mutableStateOf("") }
    var tolerancia by remember { mutableStateOf("") }

    // Listas de opciones para las bandas y el multiplicador
    val coloresBanda1y2 = listOf(
        "Negro - 0" to 0,
        "Marrón - 1" to 1,
        "Rojo - 2" to 2,
        "Naranja - 3" to 3,
        "Amarillo - 4" to 4,
        "Verde - 5" to 5,
        "Azul - 6" to 6,
        "Violeta - 7" to 7,
        "Gris - 8" to 8,
        "Blanco - 9" to 9
    )

    val coloresMultiplicador = listOf(
        "Negro x1" to 1.0,
        "Marrón x10" to 10.0,
        "Rojo x100" to 100.0,
        "Naranja x1,000" to 1_000.0,
        "Amarillo x10,000" to 10_000.0,
        "Verde x100,000" to 100_000.0,
        "Azul x1,000,000" to 1_000_000.0,
        "Dorado x0.1" to 0.1,
        "Plateado x0.01" to 0.01
    )

    val tolerancias = listOf(
        "Dorado ±5%" to "5%",
        "Plateado ±10%" to "10%"
    )

    // Función para calcular el valor de la resistencia
    fun calcularResistencia(): String {
        if (banda1.isNotEmpty() && banda2.isNotEmpty() && multiplicador.isNotEmpty() && tolerancia.isNotEmpty()) {
            val valorBanda1 = coloresBanda1y2.first { it.first == banda1 }.second
            val valorBanda2 = coloresBanda1y2.first { it.first == banda2 }.second
            val valorMultiplicador = coloresMultiplicador.first { it.first == multiplicador }.second
            val valorTolerancia = tolerancias.first { it.first == tolerancia }.second

            val resistencia = (valorBanda1 * 10 + valorBanda2) * valorMultiplicador
            return "$resistencia Ω con una tolerancia de ±$valorTolerancia"
        }
        return "Por favor, selecciona todas las bandas y la tolerancia"
    }

    // UI
    Scaffold(
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Banda 1
                DropdownSelector(
                    label = "Seleccionar Banda 1",
                    options = coloresBanda1y2.map { it.first },
                    selectedOption = banda1,
                    onOptionSelected = { banda1 = it }
                )

                // Banda 2
                DropdownSelector(
                    label = "Seleccionar Banda 2",
                    options = coloresBanda1y2.map { it.first },
                    selectedOption = banda2,
                    onOptionSelected = { banda2 = it }
                )

                // Multiplicador
                DropdownSelector(
                    label = "Seleccionar Multiplicador",
                    options = coloresMultiplicador.map { it.first },
                    selectedOption = multiplicador,
                    onOptionSelected = { multiplicador = it }
                )

                // Tolerancia
                DropdownSelector(
                    label = "Seleccionar Tolerancia",
                    options = tolerancias.map { it.first },
                    selectedOption = tolerancia,
                    onOptionSelected = { tolerancia = it }
                )

                // Botón para calcular la resistencia
                Button(onClick = {
                    val resultado = calcularResistencia()
                    Toast.makeText(context, resultado, Toast.LENGTH_LONG).show()
                }) {
                    Text("Calcular Resistencia")
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownSelector(
    label: String,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        TextField(
            value = selectedOption.ifEmpty { label },
            onValueChange = {}, // No necesitas manejar `onValueChange` ya que es solo lectura
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
            modifier = Modifier.menuAnchor()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}



