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
import com.tobs.anotador.R
import com.tobs.anotador.backend.ClassicScorekeeper
import com.tobs.anotador.frontend.components.ClickableNumGridItem
import com.tobs.anotador.frontend.components.UnclickableTextGridItem
import com.tobs.anotador.frontend.components.popUps.GetNumberPopup
import com.tobs.anotador.frontend.components.popUps.WinnerPopup
import com.tobs.anotador.frontend.screens.generic.GridScreen

@Composable
fun ClassicScreen(
    modifier: Modifier,
    scorer: ClassicScorekeeper,
    onBack: (String) -> Unit,
    onRestart: (String) -> Unit
) {
    val players = scorer.players
    val cells = remember { mutableStateListOf<@Composable () -> Unit>() }
    var adding: Int? by rememberSaveable { mutableStateOf(null) }
    var finished by rememberSaveable { mutableStateOf(false) }
    var winner by rememberSaveable { mutableStateOf("") }
    var showWon by rememberSaveable { mutableStateOf(false) }

    var minPlayedRounds by rememberSaveable {
       mutableIntStateOf(getMin(scorer.getPlayedRounds(0), scorer.getPlayedRounds(1)))
    }

    if (cells.isEmpty()) {
        val rounds = if (minPlayedRounds > 12) minPlayedRounds + 1 else 13

        for (i in 0 until players.size * rounds) {
            cells += {
                ClickableNumGridItem(content = null) { adding = i % 2 }
            }
        }
        for (i in 0 until players.size) {
            cells[i] = { UnclickableTextGridItem(content = players[i], darken = true) }
        }
        for (j in 0 until players.size) {
            for (k in 1 .. scorer.getPlayedRounds(j)) {
                val updatedIndex = (k * players.size) + j
                if (updatedIndex < cells.size) {
                    cells[updatedIndex] = {
                        ClickableNumGridItem(content = scorer.getScore(j, k).toIntOrNull() ?: 0,) {
                            adding = j
                        }
                    }
                }
            }
        }
    }

    if (adding != null && scorer.getPlayedRounds(adding!!) <= minPlayedRounds && !finished) {
        GetPoints(playerName = players[adding!!]) {
            if(it != null) {
                onAdd(adding!!, it, scorer, cells) { id -> adding = id }
                if(scorer.roundsCount % players.size == 0 && scorer.isLimitReached) {
                    winner = getWinner(players, scorer);
                    finished = true;
                    showWon = true
                }
                if (scorer.roundsCount % players.size == 0) {
                    minPlayedRounds++
                    if (minPlayedRounds >= 12) {
                        for (i in 0 until players.size) {
                            cells += { ClickableNumGridItem(content = null) { adding = i % 2 } }
                        }
                    }
                }
                adding = null
            } else {
                adding = null
            }
        }
    } else if(finished) {
        showWon = true
    } else {
        adding = null
    }

    if(showWon) {
        WinnerPopup(
            winner = winner,
            onOk = { onBack(winner) },
            onRestart = { onRestart(winner)}
        ) { showWon = false }
    }

    GridScreen(
        modifier = modifier,
        columns = players.size,
        cells = cells
    )
}

private fun onAdd(
    column: Int,
    points: Int,
    scorer: ClassicScorekeeper,
    cells: MutableList<@Composable () -> Unit>,
    onClick: (Int) -> Unit
) {
    scorer.addScore(column, points)
    val playedRounds = scorer.getPlayedRounds(column)
    val updatedIndex = (playedRounds * scorer.players.size) + column
    if (updatedIndex < cells.size) {
        cells[updatedIndex] = {
            ClickableNumGridItem(
                content = scorer.getScore(column).toIntOrNull() ?: 0,
                onClick = { onClick(column) }
            )
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

private fun getWinner(players: List<String>, scorer: ClassicScorekeeper): String {
    var minScore = Integer.MAX_VALUE
    var winner = ""
    for(i in players.indices) {
        val auxScore = Integer.parseInt(scorer.getScore(i))
        if(auxScore < minScore) {
            minScore = auxScore;
            winner = players[i];
        }
    }

    return winner
}