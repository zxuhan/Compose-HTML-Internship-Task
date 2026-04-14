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
    ghostPlayer: Player? = null,
    shake: Boolean = false,
    onClick: () -> Unit,
    onHover: (() -> Unit)? = null
) {
    val stateLabel = when (player) {
        Player.RED -> "red"
        Player.YELLOW -> "yellow"
        null -> "empty"
    }
    val isGhost = player == null && ghostPlayer != null
    val displayed = player ?: ghostPlayer
    Div(attrs = {
        classes(AppStyles.cell)
        if (shake) classes(AppStyles.cellShake)
        attr("role", "gridcell")
        attr("aria-label", "row ${row + 1} column ${col + 1} $stateLabel")
        onClick { onClick() }
        if (onHover != null) onMouseEnter { onHover() }
    }) {
        Div(attrs = {
            classes(
                AppStyles.cellInner,
                when (displayed) {
                    Player.RED -> AppStyles.cellRed
                    Player.YELLOW -> AppStyles.cellYellow
                    null -> AppStyles.cellEmpty
                }
            )
            if (isGhost) classes(AppStyles.cellGhost)
            if (isWinning) classes(AppStyles.cellWinning)
            if (animate) classes(AppStyles.cellDrop)
        }) {
            val glyph = when (displayed) {
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
