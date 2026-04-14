package ui

import androidx.compose.runtime.*
import game.GameState
import org.jetbrains.compose.web.css.Style
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H1
import org.jetbrains.compose.web.dom.Text
import persistence.Storage
import style.AppStyles

@Composable
fun App() {
    Style(AppStyles)

    val initialState = remember {
        Storage.load() ?: GameState.initial()
    }
    var gameState by remember { mutableStateOf(initialState) }

    Div(attrs = { classes(AppStyles.appContainer) }) {
        H1(attrs = { classes(AppStyles.title) }) { Text("Connect Four") }
        Controls(
            config = gameState.config,
            canUndo = gameState.previous != null,
            onNewGame = { newConfig ->
                Storage.clear()
                gameState = GameState.initial(newConfig)
            },
            onUndo = {
                val reverted = gameState.undo()
                if (reverted !== gameState) {
                    Storage.save(reverted)
                    gameState = reverted
                }
            }
        )
        StatusBar(state = gameState)
        Board(
            state = gameState,
            onColumnClick = { col ->
                val newState = gameState.dropPiece(col)
                if (newState !== gameState) {
                    if (newState.board !== gameState.board) Storage.save(newState)
                    gameState = newState
                }
            }
        )
    }
}
