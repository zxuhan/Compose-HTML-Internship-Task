package ui

import androidx.compose.runtime.*
import game.GameConfig
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.dom.*
import style.AppStyles

@Composable
fun Controls(
    config: GameConfig,
    canUndo: Boolean,
    onNewGame: (GameConfig) -> Unit,
    onUndo: () -> Unit
) {
    var rows by remember(config) { mutableStateOf(config.rows) }
    var cols by remember(config) { mutableStateOf(config.columns) }
    var winLength by remember(config) { mutableStateOf(config.winLength) }

    Div(attrs = { classes(AppStyles.controlsPanel) }) {
        LabeledInput(label = "Rows", ariaLabel = "Rows (4 to 15)", hint = "4–15",
            value = rows, min = 4, max = 15) { rows = it }
        LabeledInput(label = "Columns", ariaLabel = "Columns (4 to 15)", hint = "4–15",
            value = cols, min = 4, max = 15) { cols = it }
        LabeledInput(label = "Connect", ariaLabel = "Connect length (4 to 10)", hint = "4–10",
            value = winLength, min = 4, max = 10) { winLength = it }
        Button(attrs = {
            classes(AppStyles.newGameButton)
            onClick {
                val r = rows.coerceIn(4, 15)
                val c = cols.coerceIn(4, 15)
                val w = winLength.coerceIn(4, minOf(10, r, c))
                onNewGame(GameConfig(r, c, w))
            }
        }) { Text("New Game") }
        Button(attrs = {
            classes(AppStyles.undoButton)
            if (!canUndo) classes(AppStyles.undoDisabled)
            if (!canUndo) attr("disabled", "true")
            onClick { if (canUndo) onUndo() }
        }) { Text("Undo") }
    }
}

@Composable
private fun LabeledInput(
    label: String,
    ariaLabel: String,
    hint: String,
    value: Int,
    min: Int,
    max: Int,
    onValueChange: (Int) -> Unit
) {
    var invalid by remember { mutableStateOf(false) }
    var shakeTick by remember { mutableStateOf(0) }
    Div(attrs = { classes(AppStyles.controlGroup) }) {
        Span(attrs = { classes(AppStyles.controlLabel) }) { Text(label) }
        key(shakeTick) {
            Input(InputType.Number, attrs = {
                classes(AppStyles.controlInput)
                if (invalid) classes(AppStyles.controlInputInvalid)
                value(value)
                attr("min", min.toString())
                attr("max", max.toString())
                attr("aria-label", ariaLabel)
                onInput { event ->
                    val raw = event.value?.toInt()
                    if (raw != null) {
                        val coerced = raw.coerceIn(min, max)
                        if (raw != coerced) {
                            invalid = true
                            shakeTick++
                        } else {
                            invalid = false
                        }
                        onValueChange(coerced)
                    }
                }
            })
        }
        Span(attrs = { classes(AppStyles.controlHint) }) { Text(hint) }
    }
}
