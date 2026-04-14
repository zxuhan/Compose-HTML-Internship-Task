package persistence

import game.*
import kotlinx.browser.localStorage
import kotlin.js.JSON as JsJSON

object Storage {
    private const val KEY = "connect-four-state"

    fun save(state: GameState) {
        val obj = js("({})")
        obj.rows = state.config.rows
        obj.columns = state.config.columns
        obj.winLength = state.config.winLength
        obj.board = state.board.map { row ->
            row.map { cell -> cell?.name }.toTypedArray()
        }.toTypedArray()
        obj.currentPlayer = state.currentPlayer.name
        obj.status = state.status.name
        obj.winningCells = state.winningCells.map { (r, c) ->
            val pair = js("({})")
            pair.row = r
            pair.col = c
            pair
        }.toTypedArray()
        if (state.lastMove != null) {
            val lm = js("({})")
            lm.row = state.lastMove.first
            lm.col = state.lastMove.second
            obj.lastMove = lm
        } else {
            obj.lastMove = null
        }
        localStorage.setItem(KEY, JsJSON.stringify(obj))
    }

    fun load(): GameState? {
        val json = localStorage.getItem(KEY) ?: return null
        val parsed: dynamic = try {
            JsJSON.parse(json)
        } catch (_: Throwable) {
            clear()
            return null
        }
        return try {
            parseState(parsed) ?: run { clear(); null }
        } catch (_: Throwable) {
            clear()
            null
        }
    }

    private fun parseState(obj: dynamic): GameState? {
        val rows = (obj.rows as? Number)?.toInt() ?: return null
        val columns = (obj.columns as? Number)?.toInt() ?: return null
        val winLength = (obj.winLength as? Number)?.toInt() ?: return null
        if (rows !in 4..15) return null
        if (columns !in 4..15) return null
        if (winLength !in 4..10) return null
        if (winLength > minOf(rows, columns)) return null

        val rawBoard = obj.board ?: return null
        val boardArr = rawBoard as? Array<Array<String?>> ?: return null
        if (boardArr.size != rows) return null
        val board: List<List<Player?>> = boardArr.map { row ->
            if (row.size != columns) return null
            row.map { cell ->
                when (cell) {
                    null -> null
                    "RED" -> Player.RED
                    "YELLOW" -> Player.YELLOW
                    else -> return null
                }
            }
        }

        val currentPlayer = when (obj.currentPlayer as? String) {
            "RED" -> Player.RED
            "YELLOW" -> Player.YELLOW
            else -> return null
        }
        val status = when (obj.status as? String) {
            "IN_PROGRESS" -> GameStatus.IN_PROGRESS
            "RED_WINS" -> GameStatus.RED_WINS
            "YELLOW_WINS" -> GameStatus.YELLOW_WINS
            "DRAW" -> GameStatus.DRAW
            else -> return null
        }

        val rawWinning = obj.winningCells as? Array<dynamic> ?: return null
        val winningCells: List<Pair<Int, Int>> = rawWinning.map { pair ->
            val r = (pair.row as? Number)?.toInt() ?: return null
            val c = (pair.col as? Number)?.toInt() ?: return null
            if (r !in 0 until rows || c !in 0 until columns) return null
            r to c
        }

        val lastMove: Pair<Int, Int>? = if (obj.lastMove != null && obj.lastMove != undefined) {
            val r = (obj.lastMove.row as? Number)?.toInt() ?: return null
            val c = (obj.lastMove.col as? Number)?.toInt() ?: return null
            if (r !in 0 until rows || c !in 0 until columns) return null
            r to c
        } else null

        return GameState(
            config = GameConfig(rows, columns, winLength),
            board = board,
            currentPlayer = currentPlayer,
            status = status,
            winningCells = winningCells,
            lastMove = lastMove
        )
    }

    fun clear() {
        localStorage.removeItem(KEY)
    }
}
