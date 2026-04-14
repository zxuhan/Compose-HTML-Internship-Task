package persistence

import game.GameConfig
import game.GameState
import game.Player
import kotlinx.browser.localStorage
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class StorageTest {

    private val key = "connect-four-state"

    @BeforeTest
    fun setUp() { localStorage.removeItem(key) }

    @AfterTest
    fun tearDown() { localStorage.removeItem(key) }

    @Test
    fun roundTripPreservesBoardAndMeta() {
        var state = GameState.initial(GameConfig(6, 7, 4))
        state = state.dropPiece(3)
        state = state.dropPiece(3)
        Storage.save(state)
        val loaded = Storage.load()
        assertNotNull(loaded)
        assertEquals(state.config, loaded.config)
        assertEquals(state.board, loaded.board)
        assertEquals(state.currentPlayer, loaded.currentPlayer)
        assertEquals(state.status, loaded.status)
        assertEquals(state.lastMove, loaded.lastMove)
        // previous is never persisted
        assertNull(loaded.previous)
    }

    @Test
    fun roundTripPreservesWinningCells() {
        var state = GameState.initial(GameConfig(4, 4, 4))
        // RED wins vertically in column 0
        state = state.dropPiece(0); state = state.dropPiece(1)
        state = state.dropPiece(0); state = state.dropPiece(1)
        state = state.dropPiece(0); state = state.dropPiece(1)
        state = state.dropPiece(0)
        Storage.save(state)
        val loaded = Storage.load()
        assertNotNull(loaded)
        assertEquals(state.status, loaded.status)
        assertEquals(state.winningCells.toSet(), loaded.winningCells.toSet())
    }

    @Test
    fun clearRemovesKey() {
        Storage.save(GameState.initial())
        assertNotNull(localStorage.getItem(key))
        Storage.clear()
        assertNull(localStorage.getItem(key))
        assertNull(Storage.load())
    }

    @Test
    fun corruptJsonReturnsNullAndClears() {
        localStorage.setItem(key, "{not json")
        assertNull(Storage.load())
        assertNull(localStorage.getItem(key))
    }

    @Test
    fun loadWithoutSavedStateReturnsNull() {
        assertNull(Storage.load())
    }

    @Test
    fun outOfRangeRowsRejected() {
        localStorage.setItem(
            key,
            """{"rows":100,"columns":7,"winLength":4,"board":[],"currentPlayer":"RED","status":"IN_PROGRESS","winningCells":[],"lastMove":null}"""
        )
        assertNull(Storage.load())
        assertNull(localStorage.getItem(key))
    }

    @Test
    fun outOfRangeWinLengthRejected() {
        localStorage.setItem(
            key,
            """{"rows":6,"columns":7,"winLength":11,"board":[],"currentPlayer":"RED","status":"IN_PROGRESS","winningCells":[],"lastMove":null}"""
        )
        assertNull(Storage.load())
        assertNull(localStorage.getItem(key))
    }

    @Test
    fun boardSizeMismatchRejected() {
        // rows=4 but board only has 2 rows
        localStorage.setItem(
            key,
            """{"rows":4,"columns":4,"winLength":4,"board":[[null,null,null,null],[null,null,null,null]],"currentPlayer":"RED","status":"IN_PROGRESS","winningCells":[],"lastMove":null}"""
        )
        assertNull(Storage.load())
        assertNull(localStorage.getItem(key))
    }

    @Test
    fun unknownPlayerTokenRejected() {
        localStorage.setItem(
            key,
            """{"rows":4,"columns":4,"winLength":4,"board":[[null,null,null,null],[null,null,null,null],[null,null,null,null],[null,null,null,null]],"currentPlayer":"BLUE","status":"IN_PROGRESS","winningCells":[],"lastMove":null}"""
        )
        assertNull(Storage.load())
        assertNull(localStorage.getItem(key))
    }

    @Test
    fun redPieceColorSurvivesRoundTrip() {
        var state = GameState.initial()
        state = state.dropPiece(0)
        Storage.save(state)
        val loaded = Storage.load()
        assertNotNull(loaded)
        assertEquals(Player.RED, loaded.board[loaded.config.rows - 1][0])
    }
}
