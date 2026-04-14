package game

data class GameConfig(
    val rows: Int = 6,
    val columns: Int = 7,
    val winLength: Int = 4
)

enum class Player {
    RED, YELLOW;

    fun next(): Player = when (this) {
        RED -> YELLOW
        YELLOW -> RED
    }
}

enum class GameStatus {
    IN_PROGRESS, RED_WINS, YELLOW_WINS, DRAW
}

data class GameState(
    val config: GameConfig,
    val board: List<List<Player?>>,
    val currentPlayer: Player,
    val status: GameStatus,
    val winningCells: List<Pair<Int, Int>>,
    val lastMove: Pair<Int, Int>? = null,
    val rejectedColumn: Int? = null,
    val previous: GameState? = null
) {
    fun dropPiece(col: Int): GameState {
        if (status != GameStatus.IN_PROGRESS) return reject(col)
        if (col < 0 || col >= config.columns) return reject(col)

        val targetRow = findLowestEmptyRow(col) ?: return reject(col)

        val newBoard = board.mapIndexed { r, row ->
            if (r == targetRow) {
                row.mapIndexed { c, cell -> if (c == col) currentPlayer else cell }
            } else {
                row
            }
        }

        val winCells = WinChecker.checkWin(newBoard, targetRow, col, currentPlayer, config.winLength)

        val newStatus = when {
            winCells.isNotEmpty() -> if (currentPlayer == Player.RED) GameStatus.RED_WINS else GameStatus.YELLOW_WINS
            newBoard.all { row -> row.all { it != null } } -> GameStatus.DRAW
            else -> GameStatus.IN_PROGRESS
        }

        return GameState(
            config = config,
            board = newBoard,
            currentPlayer = if (newStatus == GameStatus.IN_PROGRESS) currentPlayer.next() else currentPlayer,
            status = newStatus,
            winningCells = winCells,
            lastMove = targetRow to col,
            rejectedColumn = null,
            previous = this.copy(rejectedColumn = null)
        )
    }

    private fun reject(col: Int): GameState =
        if (rejectedColumn == col) this else copy(rejectedColumn = col)

    fun undo(): GameState = previous ?: this

    fun landingRow(col: Int): Int? = findLowestEmptyRow(col)

    fun moveCount(): Int = board.sumOf { row -> row.count { it != null } }

    private fun findLowestEmptyRow(col: Int): Int? {
        for (row in config.rows - 1 downTo 0) {
            if (board[row][col] == null) return row
        }
        return null
    }

    companion object {
        fun initial(config: GameConfig = GameConfig()): GameState = GameState(
            config = config,
            board = List(config.rows) { List(config.columns) { null } },
            currentPlayer = Player.RED,
            status = GameStatus.IN_PROGRESS,
            winningCells = emptyList()
        )
    }
}
