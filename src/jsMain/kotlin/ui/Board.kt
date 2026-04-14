package ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import game.GameState
import org.jetbrains.compose.web.css.style
import org.jetbrains.compose.web.dom.Div
import style.AppStyles

@Composable
fun Board(state: GameState, onColumnClick: (Int) -> Unit) {
    Div(attrs = {
        classes(AppStyles.board)
        style {
            property("grid-template-columns", "repeat(${state.config.columns}, 1fr)")
        }
    }) {
        for (row in 0 until state.config.rows) {
            for (col in 0 until state.config.columns) {
                val shouldAnimate = state.lastMove == (row to col)
                key(row, col, if (shouldAnimate) state.board[row][col]?.name else "static") {
                    Cell(
                        player = state.board[row][col],
                        isWinning = (row to col) in state.winningCells,
                        animate = shouldAnimate,
                        onClick = { onColumnClick(col) }
                    )
                }
            }
        }
    }
}
