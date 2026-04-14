package ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import game.GameState
import game.GameStatus
import org.jetbrains.compose.web.css.style
import org.jetbrains.compose.web.dom.Div
import style.AppStyles

@Composable
fun Board(state: GameState, onColumnClick: (Int) -> Unit) {
    var cursor by remember { mutableStateOf(0) }
    var hovered by remember { mutableStateOf<Int?>(null) }
    var focused by remember { mutableStateOf(false) }
    val maxCol = state.config.columns - 1
    if (cursor > maxCol) cursor = maxCol

    remember(state.lastMove) {
        state.lastMove?.let { (_, col) -> if (col in 0..maxCol) cursor = col }
        Unit
    }

    val previewColumn = hovered ?: if (focused) cursor else null
    val previewLandingRow = previewColumn?.let { state.landingRow(it) }
    val showGhost = state.status == GameStatus.IN_PROGRESS && previewColumn != null && previewLandingRow != null

    Div(attrs = {
        classes(AppStyles.board)
        if (focused) classes(AppStyles.boardFocused)
        attr("tabindex", "0")
        attr("role", "grid")
        attr(
            "aria-label",
            "Connect Four board, ${state.config.rows} rows by ${state.config.columns} columns, Connect ${state.config.winLength}"
        )
        style {
            property("grid-template-columns", "repeat(${state.config.columns}, 1fr)")
        }
        onFocus { focused = true }
        onBlur { focused = false }
        onMouseLeave { hovered = null }
        onKeyDown { e ->
            when (val k = e.key) {
                "ArrowLeft" -> {
                    cursor = (cursor - 1).coerceAtLeast(0)
                    e.preventDefault()
                }
                "ArrowRight" -> {
                    cursor = (cursor + 1).coerceAtMost(maxCol)
                    e.preventDefault()
                }
                "Enter", " ", "Spacebar" -> {
                    onColumnClick(cursor)
                    e.preventDefault()
                }
                else -> {
                    if (k.length == 1 && k[0].isDigit()) {
                        val digit = k[0].digitToInt()
                        val target = if (digit == 0) 9 else digit - 1
                        if (target in 0..maxCol) {
                            cursor = target
                            onColumnClick(target)
                            e.preventDefault()
                        }
                    }
                }
            }
        }
    }) {
        for (row in 0 until state.config.rows) {
            for (col in 0 until state.config.columns) {
                val shouldAnimate = state.lastMove == (row to col)
                val ghost = if (showGhost && previewColumn == col && previewLandingRow == row) state.currentPlayer else null
                val shake = state.rejectedColumn == col
                val keyToken = when {
                    shouldAnimate -> state.board[row][col]?.name ?: "anim"
                    shake -> "shake-${state.rejectedColumn}"
                    else -> "static"
                }
                key(row, col, keyToken) {
                    Cell(
                        row = row,
                        col = col,
                        player = state.board[row][col],
                        isWinning = (row to col) in state.winningCells,
                        animate = shouldAnimate,
                        ghostPlayer = ghost,
                        shake = shake,
                        onClick = { onColumnClick(col) },
                        onHover = { hovered = col }
                    )
                }
            }
        }
    }
}
