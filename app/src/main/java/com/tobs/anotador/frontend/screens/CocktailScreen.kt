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
import com.tobs.anotador.backend.Cocktail
import com.tobs.anotador.frontend.components.ClickableNumGridItem
import com.tobs.anotador.frontend.components.ClickableTextGridItem
import com.tobs.anotador.frontend.components.UnclickableTextGridItem
import com.tobs.anotador.frontend.components.popUps.ConfirmCashPopUp
import com.tobs.anotador.frontend.components.popUps.GetNumberPopup
import com.tobs.anotador.frontend.components.popUps.WinnerPopup
import com.tobs.anotador.frontend.screens.generic.GridScreen

private val fontSize: TextUnit = 18.sp

@Composable
fun CocktailScreen(modifier: Modifier, cocktail: Cocktail, onBack: () -> Unit, onRestart: () -> Unit) {
    val players = cocktail.players
    val cells = remember { mutableStateListOf<@Composable () -> Unit>() }
    var adding: Int? by rememberSaveable { mutableStateOf(null) }
    var winner by rememberSaveable { mutableStateOf("") }
    var showWon by rememberSaveable { mutableStateOf(false) }

    var minPlayedRounds by rememberSaveable {
        mutableIntStateOf(cocktail.roundsCount / players.size)
    }
    val playedRound: Int

    if (cells.isEmpty()) {
        var updatedIndex: Int
        for (i in 0 until cocktail.rows * cocktail.columns) {
            cells += { ClickableNumGridItem(content = null) { adding = i % players.size } }
        }
        for ( j in 0 until cocktail.rows) {
            updatedIndex = j * cocktail.columns
            cells[updatedIndex] = {
                UnclickableTextGridItem(
                    content = cocktail.getScore(0, j),
                    fontSize = fontSize
                )
            }
        }
        for (k in 1 until players.size) {
            cells[k] = {
                UnclickableTextGridItem(
                    content = players[k],
                    fontSize = fontSize
                )
            }
            for (r in 1 until cocktail.getPlayedRounds(k) + 1) {
                val index = r * cocktail.columns + k
                cells[index] = {
                    ClickableNumGridItem(
                        content = cocktail.getScore(k, r).toIntOrNull() ?: 0,
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
            playedRound = cocktail.getPlayedRounds(adding!!)
            if (playedRound <= minPlayedRounds) {
                GetPoints(playerName = players[adding!!]) {
                    if (it != null) {
                        if (playedRound == minPlayedRounds + 1) {
                            onUpdate(adding!!, it, cocktail, cells) { id -> adding = id }
                        } else {
                            onAdd(adding!!, it, cocktail, cells) { id -> adding = id }
                            if (cocktail.roundsCount % (players.size - 1) == 0) {
                                minPlayedRounds++
                                if (minPlayedRounds == 8) {
                                    winner = getWinner(cocktail)
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

    if (showWon) {
        WinnerPopup(winner = winner, onOk = onBack, onRestart = onRestart) { showWon = false }
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
    cocktail: Cocktail,
    cells: MutableList<@Composable () -> Unit>,
    onClick: (Int) -> Unit
) {
    cocktail.addScore(column, points)
    val playedRounds = cocktail.getPlayedRounds(column)
    val updatedIndex = (playedRounds * cocktail.players.size) + column
    if (updatedIndex < cells.size) {
        cells[updatedIndex] = {
            ClickableNumGridItem(
                content = cocktail.getScore(column).toIntOrNull() ?: 0,
                onClick = { onClick(column) }
            )
        }
    }
}

private fun onUpdate(
    column: Int,
    points: Int,
    cocktail: Cocktail,
    cells: MutableList<@Composable () -> Unit>,
    onClick: (Int) -> Unit
) {
    val previousScore = cocktail.getScore(column,cocktail.getPlayedRounds(column) - 1).toIntOrNull() ?: 0
    cocktail.setScore(column, cocktail.getPlayedRounds(column), points + previousScore)
    val playedRounds = cocktail.getPlayedRounds(column)
    val updatedIndex = (playedRounds * cocktail.players.size) + column
    if (updatedIndex < cells.size) {
        cells[updatedIndex] = {
            ClickableNumGridItem(
                content = cocktail.getScore(column).toIntOrNull() ?: 0,
                onClick = { onClick(column) }
            )
        }
    }
}

private fun getWinner(cocktail: Cocktail): String {
    var winner = ""
    var minScore = Integer.MAX_VALUE
    for(playerIndex in 1 until cocktail.players.size) {
        val playerScore = cocktail.getScore(playerIndex).toIntOrNull() ?: Integer.MAX_VALUE
        if(playerScore < minScore) {
            minScore = playerScore
            winner = cocktail.players[playerIndex]
        }
    }

    return winner
}
