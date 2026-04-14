package game

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class GameStateTest {

    @Test
    fun pieceFallsToBottomOfEmptyColumn() {
        val state = GameState.initial()
        val next = state.dropPiece(3)
        assertEquals(Player.RED, next.board[5][3])
        for (row in 0..4) {
            assertNull(next.board[row][3])
        }
    }

    @Test
    fun pieceStacksOnExistingPiece() {
        var state = GameState.initial()
        state = state.dropPiece(3) // RED at row 5
        state = state.dropPiece(3) // YELLOW at row 4
        assertEquals(Player.RED, state.board[5][3])
        assertEquals(Player.YELLOW, state.board[4][3])
    }

    @Test
    fun fullColumnRejectsMove() {
        val config = GameConfig(rows = 3, columns = 3, winLength = 10)
        var state = GameState.initial(config)
        // Fill column 0: RED, YELLOW, RED
        state = state.dropPiece(0)
        state = state.dropPiece(0)
        state = state.dropPiece(0)
        val before = state
        // Column 0 is now full — move should be rejected and flag the column
        val after = state.dropPiece(0)
        assertEquals(before.board, after.board)
        assertEquals(before.currentPlayer, after.currentPlayer)
        assertEquals(before.status, after.status)
        assertEquals(0, after.rejectedColumn)
    }

    @Test
    fun playerAlternatesAfterValidMove() {
        val state = GameState.initial()
        assertEquals(Player.RED, state.currentPlayer)
        val next = state.dropPiece(0)
        assertEquals(Player.YELLOW, next.currentPlayer)
        val next2 = next.dropPiece(1)
        assertEquals(Player.RED, next2.currentPlayer)
    }

    @Test
    fun newGameResetsBoard() {
        var state = GameState.initial()
        state = state.dropPiece(0)
        state = state.dropPiece(1)
        val fresh = GameState.initial(state.config)
        assertEquals(Player.RED, fresh.currentPlayer)
        assertEquals(GameStatus.IN_PROGRESS, fresh.status)
        assertTrue(fresh.board.all { row -> row.all { it == null } })
    }

    @Test
    fun moveRejectedAfterGameOver() {
        val config = GameConfig(rows = 4, columns = 4, winLength = 4)
        var state = GameState.initial(config)
        // RED wins vertically in column 0
        state = state.dropPiece(0) // RED
        state = state.dropPiece(1) // YELLOW
        state = state.dropPiece(0) // RED
        state = state.dropPiece(1) // YELLOW
        state = state.dropPiece(0) // RED
        state = state.dropPiece(1) // YELLOW
        state = state.dropPiece(0) // RED — wins
        assertEquals(GameStatus.RED_WINS, state.status)
        val after = state.dropPiece(2)
        assertEquals(state.board, after.board)
        assertEquals(state.status, after.status)
        assertEquals(2, after.rejectedColumn)
    }

    @Test
    fun invalidColumnReturnsUnchangedState() {
        val state = GameState.initial()
        val outLow = state.dropPiece(-1)
        val outHigh = state.dropPiece(7)
        assertEquals(state.board, outLow.board)
        assertEquals(state.board, outHigh.board)
        assertEquals(-1, outLow.rejectedColumn)
        assertEquals(7, outHigh.rejectedColumn)
    }

    @Test
    fun impossibleWinResultsInDraw() {
        // 4x4 with winLength = 10 (impossible to win) — fill board → DRAW
        val config = GameConfig(rows = 4, columns = 4, winLength = 10)
        var state = GameState.initial(config)
        for (col in 0 until 4) {
            repeat(4) { state = state.dropPiece(col) }
        }
        assertEquals(GameStatus.DRAW, state.status)
    }

    @Test
    fun undoRevertsLastMove() {
        val state = GameState.initial()
        val afterMove = state.dropPiece(3)
        assertNotNull(afterMove.previous)
        val reverted = afterMove.undo()
        assertEquals(state.board, reverted.board)
        assertEquals(state.currentPlayer, reverted.currentPlayer)
        assertEquals(state.status, reverted.status)
    }

    @Test
    fun undoOnInitialStateIsNoOp() {
        val state = GameState.initial()
        assertEquals(state, state.undo())
    }
}
