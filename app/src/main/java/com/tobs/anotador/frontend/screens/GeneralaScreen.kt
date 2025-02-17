package com.tobs.anotador.frontend.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.tobs.anotador.backend.Generala
import com.tobs.anotador.frontend.components.DropdownTextGridItem
import com.tobs.anotador.frontend.components.UnclickableTextGridItem
import com.tobs.anotador.frontend.components.popUps.WinnerPopup
import com.tobs.anotador.frontend.screens.generic.GridScreen

@Composable
fun GeneralaScreen(
    modifier: Modifier,
    generala: Generala,
    onBack: (String) -> Unit,
    onRestart: (String) -> Unit
) {
    val players = generala.players
    val cells = remember { mutableStateListOf<@Composable () -> Unit>() }
    var winner by rememberSaveable { mutableStateOf("") }
    var showWon by rememberSaveable { mutableStateOf(false) }

    if (cells.isEmpty()) {
        cells.addAll(List(generala.rows * generala.columns) { { UnclickableTextGridItem("") } })
        var updatedIndex: Int
        for (i in 0 until generala.rows) {
            updatedIndex = i * generala.columns
            cells[updatedIndex] = {
                UnclickableTextGridItem(content = generala.getScore(0, i), darken = true)
            }
        }
        for (j in 1 until players.size) {
            cells[j] = { UnclickableTextGridItem(content = players[j], darken = true) }
            for (k in 1 until generala.rows - 1) {
                val index = k * generala.columns + j
                cells[index] = {
                    DropdownTextGridItem(
                        content = generala.getScore(j, k),
                        options = getOptions(k)
                    ) {
                        onSelected(
                            cells = cells,
                            generala = generala,
                            cellIndex = index,
                            playerIndex = j,
                            row = k,
                            newScore = it
                        ) {
                            winner = getWinner(generala)
                            showWon = true
                        }
                    }
                }
            }
        }
        for (playerIndex in 1 until players.size) {
            val index = generala.columns * (generala.rows - 1) + playerIndex
            cells[index] = {
                UnclickableTextGridItem(content = generala.getScore(playerIndex), darken = true)
            }
        }
    }

    GridScreen(
        modifier = modifier,
        columns = players.size,
        cells = cells
    )

    if (showWon) {
        WinnerPopup(
            winner = winner,
            onOk = { onBack(winner) },
            onRestart = { onRestart(winner) }
        ) { showWon = false }
    }
}

private fun getOptions(row: Int): List<String> {
    val toRet = when(row) {
        1 -> listOf("0", "1", "2", "3", "4", "5")
        2 -> listOf("0", "2", "4", "6", "8", "10")
        3 -> listOf("0", "3", "6", "9", "12", "15")
        4 -> listOf("0", "4", "8", "12", "16", "20")
        5 -> listOf("0", "5", "10", "15", "20", "25")
        6 -> listOf("0", "6", "12", "18", "24", "30")
        7 -> listOf("0", "20", "25")
        8 -> listOf("0", "30", "35")
        9 -> listOf("0", "40", "45")
        10 -> listOf("0", "50")
        11 -> listOf("0", "100")
        else -> emptyList()
    }

    return toRet
}

private fun onSelected(
    cells: MutableList<@Composable () -> Unit>,
    generala: Generala,
    cellIndex: Int,
    playerIndex: Int,
    row: Int,
    newScore: String,
    onFinished: () -> Unit
) {
    if(generala.getScore(playerIndex, row) != "") {
        generala.decRoundsCount()
    }
    generala.setScore(playerIndex, row, newScore.toIntOrNull() ?: 0)
    generala.addScore(playerIndex, newScore.toIntOrNull() ?: 0)
    if(generala.roundsCount == generala.roundsLimit) {
        onFinished()
    }
    cells[cellIndex] = {
        DropdownTextGridItem(content = newScore, options = getOptions(row)) {
            onSelected(cells, generala, cellIndex, playerIndex, row, it, onFinished)
        }
    }
    val index = generala.columns * (generala.rows - 1) + playerIndex
    cells[index] = { UnclickableTextGridItem(content = generala.getScore(playerIndex), darken = true) }
}

private fun getWinner(generala: Generala): String {
    var winner = ""
    var maxScore = 0
    for(playerIndex in 1 until generala.players.size) {
        val playerScore = generala.getScore(playerIndex).toIntOrNull() ?: 0
        if(playerScore > maxScore) {
            maxScore = playerScore
            winner = generala.players[playerIndex]
        }
    }

    return winner
}
