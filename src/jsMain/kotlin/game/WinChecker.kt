package game

object WinChecker {

    private val directions = listOf(
        0 to 1,   // horizontal
        1 to 0,   // vertical
        1 to 1,   // diagonal down-right
        1 to -1   // diagonal down-left
    )

    fun checkWin(
        board: List<List<Player?>>,
        row: Int,
        col: Int,
        player: Player,
        winLength: Int
    ): List<Pair<Int, Int>> {
        val rows = board.size
        val cols = board[0].size

        for ((dr, dc) in directions) {
            val cells = mutableListOf(row to col)

            // Count forward
            var r = row + dr
            var c = col + dc
            while (r in 0 until rows && c in 0 until cols && board[r][c] == player) {
                cells.add(r to c)
                r += dr
                c += dc
            }

            // Count backward
            r = row - dr
            c = col - dc
            while (r in 0 until rows && c in 0 until cols && board[r][c] == player) {
                cells.add(r to c)
                r -= dr
                c -= dc
            }

            if (cells.size >= winLength) return cells
        }

        return emptyList()
    }
}
