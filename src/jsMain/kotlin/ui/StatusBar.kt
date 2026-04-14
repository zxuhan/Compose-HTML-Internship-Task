package ui

import androidx.compose.runtime.Composable
import game.GameState
import game.GameStatus
import game.Player
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Text
import style.AppStyles

@Composable
fun StatusBar(state: GameState) {
    val (text, colorClass) = when (state.status) {
        GameStatus.IN_PROGRESS -> {
            val name = if (state.currentPlayer == Player.RED) "Red" else "Yellow"
            val cls = if (state.currentPlayer == Player.RED) AppStyles.statusRed else AppStyles.statusYellow
            "$name's Turn" to cls
        }
        GameStatus.RED_WINS -> "Red Wins!" to AppStyles.statusRed
        GameStatus.YELLOW_WINS -> "Yellow Wins!" to AppStyles.statusYellow
        GameStatus.DRAW -> "It's a Draw!" to AppStyles.statusDraw
    }

    Div(attrs = { classes(AppStyles.statusBar, colorClass) }) {
        Text(text)
    }
}
