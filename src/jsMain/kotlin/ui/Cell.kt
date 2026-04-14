package ui

import androidx.compose.runtime.Composable
import game.Player
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text
import style.AppStyles

@Composable
fun Cell(
    row: Int,
    col: Int,
    player: Player?,
    isWinning: Boolean,
    animate: Boolean,
    onClick: () -> Unit
) {
    val stateLabel = when (player) {
        Player.RED -> "red"
        Player.YELLOW -> "yellow"
        null -> "empty"
    }
    Div(attrs = {
        classes(AppStyles.cell)
        attr("role", "gridcell")
        attr("aria-label", "row ${row + 1} column ${col + 1} $stateLabel")
        onClick { onClick() }
    }) {
        Div(attrs = {
            classes(
                AppStyles.cellInner,
                when (player) {
                    Player.RED -> AppStyles.cellRed
                    Player.YELLOW -> AppStyles.cellYellow
                    null -> AppStyles.cellEmpty
                }
            )
            if (isWinning) classes(AppStyles.cellWinning)
            if (animate) classes(AppStyles.cellDrop)
        }) {
            val glyph = when (player) {
                Player.RED -> "\u25CF"
                Player.YELLOW -> "\u25B2"
                null -> null
            }
            if (glyph != null) {
                Span(attrs = {
                    classes(AppStyles.cellGlyph)
                    attr("aria-hidden", "true")
                }) { Text(glyph) }
            }
        }
    }
}
