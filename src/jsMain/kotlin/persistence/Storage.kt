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
        return try {
            val obj: dynamic = JsJSON.parse(json)
            val config = GameConfig(
                rows = (obj.rows as Number).toInt(),
                columns = (obj.columns as Number).toInt(),
                winLength = (obj.winLength as Number).toInt()
            )
            val board: List<List<Player?>> = (obj.board as Array<Array<String?>>).map { row ->
                row.map { cell ->
                    when (cell) {
                        "RED" -> Player.RED
                        "YELLOW" -> Player.YELLOW
                        else -> null
                    }
                }
            }
            val currentPlayer = Player.valueOf(obj.currentPlayer as String)
            val status = GameStatus.valueOf(obj.status as String)
            val winningCells: List<Pair<Int, Int>> = (obj.winningCells as Array<dynamic>).map { pair ->
                (pair.row as Number).toInt() to (pair.col as Number).toInt()
            }
            val lastMove: Pair<Int, Int>? = if (obj.lastMove != null && obj.lastMove != undefined) {
                (obj.lastMove.row as Number).toInt() to (obj.lastMove.col as Number).toInt()
            } else null

            GameState(config, board, currentPlayer, status, winningCells, lastMove)
        } catch (_: Throwable) {
            clear()
            null
        }
    }

    fun clear() {
        localStorage.removeItem(KEY)
    }
}
