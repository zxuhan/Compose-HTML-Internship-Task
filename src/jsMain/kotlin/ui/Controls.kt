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
                // Max achievable line in any direction (horizontal, vertical, diagonal) is minOf(r, c).
                val w = winLength.coerceIn(4, minOf(10, r, c))
                onNewGame(GameConfig(r, c, w))
            }
        }) { Text("New Game") }
        Button(attrs = {
            classes(AppStyles.undoButton)
            if (!canUndo) classes(AppStyles.undoDisabled)
            if (!canUndo) attr("disabled", "true")
            attr("title", "Undo last move (can undo repeatedly)")
            attr("aria-label", "Undo last move")
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
    var text by remember(value) { mutableStateOf(value.toString()) }
    var invalid by remember { mutableStateOf(false) }

    Div(attrs = { classes(AppStyles.controlGroup) }) {
        Span(attrs = { classes(AppStyles.controlLabel) }) { Text(label) }
        Div(attrs = { classes(AppStyles.stepperRow) }) {
            Button(attrs = {
                classes(AppStyles.stepperButton)
                if (value <= min) classes(AppStyles.stepperButtonDisabled)
                attr("type", "button")
                attr("aria-label", "Decrease $label")
                if (value <= min) attr("disabled", "true")
                onClick { if (value > min) onValueChange(value - 1) }
            }) { Text("−") }
            Input(InputType.Text, attrs = {
                classes(AppStyles.controlInput)
                if (invalid) classes(AppStyles.controlInputInvalid)
                value(text)
                attr("inputmode", "numeric")
                attr("pattern", "[0-9]*")
                attr("aria-label", ariaLabel)
                onInput { event ->
                    val raw = event.value
                    text = raw
                    val parsed = raw.toIntOrNull()
                    if (parsed == null || parsed < min || parsed > max) {
                        invalid = true
                    } else {
                        invalid = false
                        onValueChange(parsed)
                    }
                }
                onBlur {
                    val clamped = (text.toIntOrNull() ?: min).coerceIn(min, max)
                    text = clamped.toString()
                    invalid = false
                    onValueChange(clamped)
                }
            })
            Button(attrs = {
                classes(AppStyles.stepperButton)
                if (value >= max) classes(AppStyles.stepperButtonDisabled)
                attr("type", "button")
                attr("aria-label", "Increase $label")
                if (value >= max) attr("disabled", "true")
                onClick { if (value < max) onValueChange(value + 1) }
            }) { Text("+") }
        }
        Span(attrs = { classes(AppStyles.controlHint) }) { Text(hint) }
    }
}
