package game

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class WinCheckerTest {

    @Test
    fun horizontalWin() {
        var state = GameState.initial()
        // RED: cols 0,1,2,3 — YELLOW: cols 0,1,2 (stacking on row above)
        state = state.dropPiece(0) // RED row 5
        state = state.dropPiece(0) // YELLOW row 4
        state = state.dropPiece(1) // RED row 5
        state = state.dropPiece(1) // YELLOW row 4
        state = state.dropPiece(2) // RED row 5
        state = state.dropPiece(2) // YELLOW row 4
        state = state.dropPiece(3) // RED row 5 — horizontal win
        assertEquals(GameStatus.RED_WINS, state.status)
        assertEquals(4, state.winningCells.size)
    }

    @Test
    fun verticalWin() {
        var state = GameState.initial()
        state = state.dropPiece(0) // RED
        state = state.dropPiece(1) // YELLOW
        state = state.dropPiece(0) // RED
        state = state.dropPiece(1) // YELLOW
        state = state.dropPiece(0) // RED
        state = state.dropPiece(1) // YELLOW
        state = state.dropPiece(0) // RED — vertical win
        assertEquals(GameStatus.RED_WINS, state.status)
        assertEquals(4, state.winningCells.size)
    }

    @Test
    fun diagonalDownRightWin() {
        var state = GameState.initial()
        // Build a diagonal: RED at (5,0), (4,1), (3,2), (2,3)
        state = state.dropPiece(0) // RED row 5, col 0
        state = state.dropPiece(1) // YELLOW row 5, col 1
        state = state.dropPiece(1) // RED row 4, col 1
        state = state.dropPiece(2) // YELLOW row 5, col 2
        state = state.dropPiece(2) // RED row 4, col 2
        state = state.dropPiece(3) // YELLOW row 5, col 3
        state = state.dropPiece(2) // RED row 3, col 2
        state = state.dropPiece(3) // YELLOW row 4, col 3
        state = state.dropPiece(3) // RED row 3, col 3
        state = state.dropPiece(6) // YELLOW row 5, col 6 (avoid horizontal win)
        state = state.dropPiece(3) // RED row 2, col 3 — diagonal win
        assertEquals(GameStatus.RED_WINS, state.status)
        assertEquals(4, state.winningCells.size)
    }

    @Test
    fun diagonalDownLeftWin() {
        var state = GameState.initial()
        // Build a diagonal: RED at (5,3), (4,2), (3,1), (2,0)
        state = state.dropPiece(3) // RED row 5, col 3
        state = state.dropPiece(2) // YELLOW row 5, col 2
        state = state.dropPiece(2) // RED row 4, col 2
        state = state.dropPiece(1) // YELLOW row 5, col 1
        state = state.dropPiece(1) // RED row 4, col 1
        state = state.dropPiece(0) // YELLOW row 5, col 0
        state = state.dropPiece(1) // RED row 3, col 1
        state = state.dropPiece(0) // YELLOW row 4, col 0
        state = state.dropPiece(0) // RED row 3, col 0
        state = state.dropPiece(4) // YELLOW row 5, col 4
        state = state.dropPiece(0) // RED row 2, col 0 — diagonal win
        assertEquals(GameStatus.RED_WINS, state.status)
        assertEquals(4, state.winningCells.size)
    }

    @Test
    fun noWinWithThreeInARow() {
        var state = GameState.initial()
        state = state.dropPiece(0) // RED
        state = state.dropPiece(0) // YELLOW
        state = state.dropPiece(1) // RED
        state = state.dropPiece(1) // YELLOW
        state = state.dropPiece(2) // RED
        // Only 3 in a row for RED
        assertEquals(GameStatus.IN_PROGRESS, state.status)
        assertTrue(state.winningCells.isEmpty())
    }

    @Test
    fun drawWhenBoardIsFull() {
        val config = GameConfig(rows = 2, columns = 2, winLength = 3)
        var state = GameState.initial(config)
        state = state.dropPiece(0) // RED row 1, col 0
        state = state.dropPiece(1) // YELLOW row 1, col 1
        state = state.dropPiece(1) // RED row 0, col 1
        state = state.dropPiece(0) // YELLOW row 0, col 0
        assertEquals(GameStatus.DRAW, state.status)
    }

    @Test
    fun configurableWinLengthConnect5() {
        val config = GameConfig(rows = 6, columns = 7, winLength = 5)
        var state = GameState.initial(config)
        // RED plays cols 0-4, YELLOW plays cols 0-3 (stacking)
        state = state.dropPiece(0) // RED
        state = state.dropPiece(0) // YELLOW
        state = state.dropPiece(1) // RED
        state = state.dropPiece(1) // YELLOW
        state = state.dropPiece(2) // RED
        state = state.dropPiece(2) // YELLOW
        state = state.dropPiece(3) // RED
        state = state.dropPiece(3) // YELLOW
        // 4 in a row — not enough for connect 5
        assertEquals(GameStatus.IN_PROGRESS, state.status)
        state = state.dropPiece(4) // RED — 5 in a row!
        assertEquals(GameStatus.RED_WINS, state.status)
        assertEquals(5, state.winningCells.size)
    }

    @Test
    fun configurableWinLengthConnect6() {
        val config = GameConfig(rows = 6, columns = 7, winLength = 6)
        var state = GameState.initial(config)
        // RED: 0,1,2,3,4,5 — YELLOW: 0,1,2,3,4 (stacking)
        for (i in 0..4) {
            state = state.dropPiece(i) // RED
            state = state.dropPiece(i) // YELLOW
        }
        assertEquals(GameStatus.IN_PROGRESS, state.status)
        state = state.dropPiece(5) // RED — 6 in a row
        assertEquals(GameStatus.RED_WINS, state.status)
        assertEquals(6, state.winningCells.size)
    }

    @Test
    fun winOnLargeBoard() {
        val config = GameConfig(rows = 10, columns = 10, winLength = 4)
        var state = GameState.initial(config)
        // RED horizontal win at bottom row: cols 5,6,7,8
        state = state.dropPiece(5) // RED
        state = state.dropPiece(5) // YELLOW
        state = state.dropPiece(6) // RED
        state = state.dropPiece(6) // YELLOW
        state = state.dropPiece(7) // RED
        state = state.dropPiece(7) // YELLOW
        state = state.dropPiece(8) // RED — wins
        assertEquals(GameStatus.RED_WINS, state.status)
        assertEquals(4, state.winningCells.size)
    }
}
