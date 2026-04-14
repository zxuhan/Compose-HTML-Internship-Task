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
        // Column 0 is now full — move should be rejected
        val after = state.dropPiece(0)
        assertEquals(before, after)
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
        assertEquals(state, after)
    }

    @Test
    fun invalidColumnReturnsUnchangedState() {
        val state = GameState.initial()
        assertEquals(state, state.dropPiece(-1))
        assertEquals(state, state.dropPiece(7))
    }
}
