package com.tobs.anotador.frontend.screens

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.tobs.anotador.R
import com.tobs.anotador.backend.history.DatabaseHelper
import com.tobs.anotador.frontend.components.ClickableNumGridItem
import com.tobs.anotador.frontend.components.UnclickableTextGridItem
import com.tobs.anotador.frontend.components.popUps.GetNumberPopup
import com.tobs.anotador.frontend.screens.generic.GridScreen
import com.tobs.anotador.frontend.starters.getFromDb

val names = listOf("Tobi", "Jose")
val games = listOf("Truco", "Escoba", "Chinchón", "Canasta", "Generala", "Batalla")

@Composable
fun HistoryScreen(modifier: Modifier) {
    val cells = remember { mutableStateListOf<@Composable () -> Unit>() }
    var updatingCellIndex: Int? by rememberSaveable { mutableStateOf(null) }
    val context = LocalContext.current

    if (cells.isEmpty()) {
        cells += { UnclickableTextGridItem(content = "", darken = true) }
        for (name in names) {
            cells += { UnclickableTextGridItem(content = name, darken = true) }
        }
        for (i in games.indices) {
            cells += { UnclickableTextGridItem(content = games[i].take(3), darken = true) }
            for (j in names.indices) {
                cells += {
                    ClickableNumGridItem(content = getGameScore(context, i, j)) {
                        val myIndex = (i + 1) * (names.size + 1) + j + 1
                        updatingCellIndex = myIndex
                    }
                }
            }
        }
    }

    GridScreen(modifier = modifier, columns = names.size + 1, cells = cells)

    if (updatingCellIndex != null) {
        val myIndex = updatingCellIndex!!
        GetNewValue(text = "${games[myIndex/3 - 1]} de ${names[myIndex % 3 - 1]}") {
            if(it != null) {
                val dbHelper = DatabaseHelper(context)
                dbHelper.updateResult(games[myIndex/3 - 1].take(3), names[myIndex % 3 - 1].lowercase(), it)
                cells[myIndex] = { ClickableNumGridItem(content = it) { updatingCellIndex = myIndex } }
            }
            updatingCellIndex = null
        }
    }
}

@Composable
private fun GetNewValue(text: String, onComplete: (Int?) -> Unit) {
    GetNumberPopup(
        placeHolder = text,
        checkNum = { it >= 0 },
        outOfBounds = stringResource(id = R.string.positive_zero),
        onOk = { points ->
            onComplete(points)
        }
    ) {
        onComplete(null)
    }
}

private fun getGameScore(context: Context, row: Int, player: Int): Int? {
    return when (player) {
        0 -> { getFromDb(context, "tobi", games[row].take(3)).toIntOrNull() }
        else -> { getFromDb(context, "jose", games[row].take(3)).toIntOrNull() }
    }
}