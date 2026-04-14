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

    Div(attrs = {
        classes(AppStyles.statusBar, colorClass)
        attr("role", "status")
        attr("aria-live", "polite")
    }) {
        Text("$text — Moves: ${state.moveCount()}")
    }
}

@Composable
fun MoveAnnouncer(state: GameState) {
    val message = when (state.status) {
        GameStatus.RED_WINS -> "Red wins with ${state.winningCells.size} in a row."
        GameStatus.YELLOW_WINS -> "Yellow wins with ${state.winningCells.size} in a row."
        GameStatus.DRAW -> "Game ended in a draw."
        GameStatus.IN_PROGRESS -> state.lastMove?.let { (row, col) ->
            val mover = if (state.currentPlayer == Player.RED) "Yellow" else "Red"
            "$mover dropped in column ${col + 1}, row ${row + 1}."
        } ?: ""
    }
    Div(attrs = {
        classes(AppStyles.visuallyHidden)
        attr("role", "status")
        attr("aria-live", "polite")
        attr("aria-atomic", "true")
    }) {
        Text(message)
    }
}
