package com.tobs.anotador.frontend.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.tobs.anotador.R
import com.tobs.anotador.backend.Carioca
import com.tobs.anotador.frontend.components.ClickableNumGridItem
import com.tobs.anotador.frontend.components.ClickableTextGridItem
import com.tobs.anotador.frontend.components.UnclickableTextGridItem
import com.tobs.anotador.frontend.components.popUps.ConfirmCashPopUp
import com.tobs.anotador.frontend.components.popUps.GetNumberPopup
import com.tobs.anotador.frontend.components.popUps.WinnerPopup
import com.tobs.anotador.frontend.screens.generic.GridScreen

private val fontSize: TextUnit = 18.sp

@Composable
fun CariocaScreen(modifier: Modifier, carioca: Carioca, onBack: () -> Unit, onRestart: () -> Unit) {
    val players = carioca.players
    val cells = remember { mutableStateListOf<@Composable () -> Unit>() }
    var adding: Int? by rememberSaveable { mutableStateOf(null) }
    var cash: Int? by rememberSaveable { mutableStateOf(null) }
    var winner by rememberSaveable { mutableStateOf("") }
    var showWon by rememberSaveable { mutableStateOf(false) }

    var minPlayedRounds by rememberSaveable {
        mutableIntStateOf(carioca.roundsCount / players.size)
    }
    val playedRound: Int

    if (cells.isEmpty()) {
        var updatedIndex: Int
        for (i in 0 until carioca.rows * carioca.columns) {
            cells += { ClickableNumGridItem(content = null) { adding = i % players.size } }
        }
        for (j in 0 until carioca.rows) {
            updatedIndex = j * carioca.columns
            cells[updatedIndex] = {
                UnclickableTextGridItem(
                    content = carioca.getScore(0, j),
                    fontSize = fontSize,
                    darken = true
                )
            }
        }
        for (k in 1 until players.size) {
            cells[k] = {
                ClickableTextGridItem(
                    content = "${players[k]} (${carioca.getCashCount(k)})",
                    fontSize = fontSize,
                    darken = true
                ) {
                    cash = k
                }
            }
            for (r in 1 until carioca.getPlayedRounds(k) + 1) {
                val index = r * carioca.columns + k
                cells[index] = {
                    ClickableNumGridItem(
                        content = carioca.getScore(k, r).toIntOrNull() ?: 0,
                    ) {
                        adding = r % players.size
                    }
                }
            }
        }
    }

    GridScreen(
        modifier = modifier,
        columns = players.size,
        cells = cells
    )

    if (adding != null) {
        if (winner != "") {
            showWon = true
            adding = null
        } else {
            playedRound = carioca.getPlayedRounds(adding!!)
            if (playedRound <= minPlayedRounds + 1) {
                GetPoints(playerName = players[adding!!]) {
                    if (it != null) {
                        if (playedRound == minPlayedRounds + 1) {
                            onUpdate(adding!!, it, carioca, cells) { id -> adding = id }
                        } else {
                            onAdd(adding!!, it, carioca, cells) { id -> adding = id }
                            if (carioca.roundsCount % (players.size - 1) == 0) {
                                minPlayedRounds++
                                if (minPlayedRounds == 8) {
                                    winner = getWinner(carioca)
                                    showWon = true
                                }
                            }
                        }
                        adding = null
                    } else {
                        adding = null
                    }
                }
            } else {
                adding = null
            }
        }
    } else {
        adding = null
    }

    if (cash != null) {
        AddCash(
            cells = cells,
            carioca = carioca,
            playerIndex = cash!!
        ) { cash = it }
    }

    if (showWon) {
        WinnerPopup(winner = winner, onOk = onBack, onRestart = onRestart) { showWon = false }
    }
}

@Composable
private fun AddCash(
    cells: MutableList<@Composable () -> Unit>,
    carioca: Carioca,
    playerIndex: Int,
    setCash: (Int?) -> Unit
) {
    ConfirmCashPopUp(
        player = carioca.players[playerIndex],
        onDismiss = { setCash(null) }
    ) {
        carioca.addCash(playerIndex)
        cells[playerIndex] = {
            ClickableTextGridItem(
                content = "${carioca.players[playerIndex]} (${carioca.getCashCount(playerIndex)})",
                fontSize = fontSize,
                darken = true
            ) {
                setCash(playerIndex)
            }
        }
    }
}

@Composable
private fun GetPoints(playerName: String, onComplete: (Int?) -> Unit) {
    GetNumberPopup(
        placeHolder = "${stringResource(id = R.string.score)} $playerName",
        checkNum = { true },
        outOfBounds = stringResource(id = R.string.integer),
        onOk = { points ->
            onComplete(points)
        }
    ) {
        onComplete(null)
    }
}

private fun onAdd(
    column: Int,
    points: Int,
    carioca: Carioca,
    cells: MutableList<@Composable () -> Unit>,
    onClick: (Int) -> Unit
) {
    carioca.addScore(column, points)
    val playedRounds = carioca.getPlayedRounds(column)
    val updatedIndex = (playedRounds * carioca.players.size) + column
    if (updatedIndex < cells.size) {
        cells[updatedIndex] = {
            ClickableNumGridItem(
                content = carioca.getScore(column).toIntOrNull() ?: 0,
                onClick = { onClick(column) }
            )
        }
    }
}

private fun onUpdate(
    column: Int,
    points: Int,
    carioca: Carioca,
    cells: MutableList<@Composable () -> Unit>,
    onClick: (Int) -> Unit
) {
    val previousScore = carioca.getScore(column,carioca.getPlayedRounds(column) - 1).toIntOrNull() ?: 0
    carioca.setScore(column, carioca.getPlayedRounds(column), points + previousScore)
    val playedRounds = carioca.getPlayedRounds(column)
    val updatedIndex = (playedRounds * carioca.players.size) + column
    if (updatedIndex < cells.size) {
        cells[updatedIndex] = {
            ClickableNumGridItem(
                content = carioca.getScore(column).toIntOrNull() ?: 0,
                onClick = { onClick(column) }
            )
        }
    }
}

private fun getWinner(carioca: Carioca): String {
    var winner = ""
    var minScore = Integer.MAX_VALUE
    for(playerIndex in 1 until carioca.players.size) {
        val playerScore = carioca.getScore(playerIndex).toIntOrNull() ?: Integer.MAX_VALUE
        if(playerScore < minScore) {
            minScore = playerScore
            winner = carioca.players[playerIndex]
        }
    }

    return winner
}
