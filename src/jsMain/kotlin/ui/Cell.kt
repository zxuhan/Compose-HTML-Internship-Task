package ui

import androidx.compose.runtime.Composable
import game.Player
import org.jetbrains.compose.web.dom.Div
import style.AppStyles

@Composable
fun Cell(player: Player?, isWinning: Boolean, animate: Boolean, onClick: () -> Unit) {
    Div(attrs = {
        classes(AppStyles.cell)
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
        })
    }
}
