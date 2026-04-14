package ui

import androidx.compose.runtime.*
import game.GameConfig
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.dom.*
import style.AppStyles

@Composable
fun Controls(config: GameConfig, onNewGame: (GameConfig) -> Unit) {
    var rows by remember(config) { mutableStateOf(config.rows) }
    var cols by remember(config) { mutableStateOf(config.columns) }
    var winLength by remember(config) { mutableStateOf(config.winLength) }

    Div(attrs = { classes(AppStyles.controlsPanel) }) {
        Div(attrs = { classes(AppStyles.controlGroup) }) {
            Span(attrs = { classes(AppStyles.controlLabel) }) { Text("Rows") }
            NumberInput(value = rows, min = 4, max = 15) { rows = it }
        }
        Div(attrs = { classes(AppStyles.controlGroup) }) {
            Span(attrs = { classes(AppStyles.controlLabel) }) { Text("Columns") }
            NumberInput(value = cols, min = 4, max = 15) { cols = it }
        }
        Div(attrs = { classes(AppStyles.controlGroup) }) {
            Span(attrs = { classes(AppStyles.controlLabel) }) { Text("Connect") }
            NumberInput(value = winLength, min = 3, max = 10) { winLength = it }
        }
        Button(attrs = {
            classes(AppStyles.newGameButton)
            onClick {
                val r = rows.coerceIn(4, 15)
                val c = cols.coerceIn(4, 15)
                val w = winLength.coerceIn(3, minOf(10, r, c))
                onNewGame(GameConfig(r, c, w))
            }
        }) { Text("New Game") }
    }
}

@Composable
private fun NumberInput(value: Int, min: Int, max: Int, onValueChange: (Int) -> Unit) {
    Input(InputType.Number, attrs = {
        classes(AppStyles.controlInput)
        value(value)
        attr("min", min.toString())
        attr("max", max.toString())
        onInput { event ->
            event.value?.toInt()?.let { onValueChange(it.coerceIn(min, max)) }
        }
    })
}
