package com.tobs.anotador.frontend.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.tobs.anotador.backend.Canasta
import com.tobs.anotador.frontend.components.ClickableNumGridItem
import com.tobs.anotador.frontend.components.UnclickableTextGridItem
import com.tobs.anotador.frontend.components.popUps.GetNumberPopup
import com.tobs.anotador.frontend.components.popUps.WinnerPopup
import com.tobs.anotador.frontend.screens.generic.GridScreen

@Composable
fun CanastaScreen(
    modifier: Modifier,
    canasta: Canasta,
    onBack: (String) -> Unit,
    onRestart: (String) -> Unit
) {
    val players = canasta.players
    val cells = remember { mutableStateListOf<@Composable () -> Unit>() }
    var adding: Int? by rememberSaveable { mutableStateOf(null) }
    var finished by rememberSaveable { mutableStateOf(false) }
    var winner by rememberSaveable { mutableStateOf("") }
    var showWon by rememberSaveable { mutableStateOf(false) }

    if (cells.isEmpty()) {
        val minPlayedRounds = getMin(canasta.getPlayedRounds(0), canasta.getPlayedRounds(1))
        val rounds = if (minPlayedRounds >= 12) minPlayedRounds + 4 else 13

        for (i in 0 until players.size * rounds) {
            cells += { ClickableNumGridItem(content = null) { adding = i % 2 } }
        }
        for (i in 0 until players.size) {
            cells[i] = { UnclickableTextGridItem(content = "${players[i]} (${canasta.getFloor(i)})") }
        }
        for (j in 0 until players.size) {
            for (k in 1 .. canasta.getPlayedRounds(j)) {
                val updatedIndex = (k * players.size) + j
                if (updatedIndex < cells.size) {
                    cells[updatedIndex] = {
                        ClickableNumGridItem(content = canasta.getScore(j, k).toIntOrNull() ?: 0) {
                            adding = j
                        }
                    }
                }
            }
        }
    }

    if (adding != null) {
        if (winner != "") {
            showWon = true
            adding = null
        } else {
            if (canasta.getPlayedRounds(adding!!) <= canasta.getPlayedRounds(1 - adding!!)) {
                GetPoints(players[adding!!]) {
                    if (it != null) {
                        val finish = onAdd(adding!!, it, canasta, cells) { id -> adding = id }
                        if (finish && canasta.getPlayedRounds(0) == canasta.getPlayedRounds(1)) {
                            winner = players[adding!!]
                            showWon = true
                        } else if (finish) {
                            finished = true
                        } else if (finished) {
                            winner = players[1 - adding!!]
                            showWon = true
                        } else if (canasta.getPlayedRounds(0) == canasta.getPlayedRounds(1)) {
                            if (canasta.getPlayedRounds(0) >= 12) {
                                for(i in 0 until 6) {
                                    cells += { ClickableNumGridItem(content = null) { adding = i%2 } }
                                }
                            }
                        }
                    }
                    adding = null
                }
            } else {
                adding = null
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

fun getMin(a: Int, b: Int): Int {
    return if (a > b) b else a
}

private fun onAdd(
    column: Int,
    pointsPair: Pair<Int, Int>,
    canasta: Canasta,
    cells: MutableList<@Composable () -> Unit>,
    onClick: (Int) -> Unit
): Boolean {
    for(point in pointsPair.toList()) {
        canasta.setScore(column, point)
        val playedRounds = canasta.getPlayedRounds(column)
        val updatedIndex = (playedRounds * canasta.players.size) + column
        if (updatedIndex < cells.size) {
            cells[updatedIndex] = {
                ClickableNumGridItem(
                    content = canasta.getScore(column, playedRounds).toIntOrNull() ?: 0,
                    onClick = { onClick(column) }
                )
            }
        }
    }
    val finished = canasta.addScore(column, pointsPair.first + pointsPair.second)
    val playedRounds = canasta.getPlayedRounds(column)
    val updatedIndex = (playedRounds * canasta.players.size) + column
    if (updatedIndex < cells.size) {
        cells[updatedIndex] = {
            ClickableNumGridItem(
                content = canasta.getScore(column, playedRounds).toIntOrNull() ?: 0,
                onClick = { onClick(column) }
            )
        }
        cells[column] = { UnclickableTextGridItem(content = "${canasta.players[column]} (${canasta.getFloor(column)})") }
    }

    return finished
}

@Composable
private fun GetPoints(playerName: String, onComplete: (Pair<Int, Int>?) -> Unit) {
    var toRet by rememberSaveable { mutableStateOf(Pair(0, 0)) }
    var closed by rememberSaveable { mutableStateOf(false) }
    var showPopUp by rememberSaveable { mutableStateOf(true) }
    var pointIndex by rememberSaveable { mutableIntStateOf(0) }
    val placeHolders = listOf(R.string.base, R.string.score)

    if (showPopUp && pointIndex < 2) {
        GetNumberPopup(
            placeHolder = "${stringResource(id = placeHolders[pointIndex])} $playerName",
            checkNum = { true },
            outOfBounds = stringResource(id = R.string.integer),
            onOk = { points ->
                toRet = if(pointIndex == 0)
                    toRet.copy(first = points)
                else
                    toRet.copy(second = points)
                showPopUp = false
                pointIndex++
            }
        ) {
            closed = true
            showPopUp = false
        }
    }

    if (!showPopUp) {
        LaunchedEffect(Unit) {
            var finished = false
            if (closed) {
                onComplete(null)
                finished = true
            } else if (pointIndex >= 2) {
                onComplete(toRet)
                finished = true
            } else {
                showPopUp = true
            }
            if (finished) {
                toRet = Pair(0, 0)
                closed = false
                showPopUp = true
                pointIndex = 0
            }
        }
    }
}
