package com.tobs.anotador.frontend.screens.generic

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.tobs.anotador.R
import com.tobs.anotador.backend.Matches
import com.tobs.anotador.frontend.components.popUps.WinnerPopup

@Composable
fun MatchesScreen(
    modifier: Modifier,
    matches: Matches,
    onBack: (String, Int) -> Unit,
    onRestart: (String, Int) -> Unit
) {
    val scores = remember {
        mutableStateListOf(
            matches.getScore(0).toIntOrNull() ?: 0,
            matches.getScore(1).toIntOrNull() ?: 0
        )
    }
    var finished by rememberSaveable { mutableStateOf(false) }
    var winner by rememberSaveable { mutableStateOf("") }

    MatchesAux(
        modifier = modifier,
        players = matches.players,
        scores = scores,
        onDec = { if(scores[it] > 0) { matches.addScore(it, -1); scores[it]-- } }
    ) {
        if(scores[it] < matches.roundsLimit) {
            finished = matches.addScore(it, 1);
            scores[it]++
        }
        if (finished || scores[it] == matches.roundsLimit) {
            finished = true
            winner = matches.players[it]
        }
    }

    if (finished) {
        WinnerPopup(
            winner = winner,
            onOk = { onBack(winner, matches.roundsLimit) },
            onRestart = { onRestart(winner, matches.roundsLimit) }
        ) { finished = false }
    }
}

@Composable
fun MatchesAux(
    modifier: Modifier,
    players: List<String>,
    scores: List<Int>,
    onDec: (Int) -> Unit,
    onAdd: (Int) -> Unit
) {
    Box(modifier.padding(8.dp)) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 70.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // First Column
            ButtonsColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(top = 35.dp, end = 8.dp),
                onAdd = { onAdd(0) },
                onDec = { onDec(0) }
            )
            // Second Column
            MatchesColumn(
                modifier = Modifier
                    .weight(2f)
                    .fillMaxHeight(),
                playerName = players[0],
                count = scores[0]
            ) { onAdd(0) }
            VerticalDivider(
                thickness = 4.dp,
                color = MaterialTheme.colorScheme.onPrimary
            )
            // Third Column
            MatchesColumn(
                modifier = Modifier
                    .weight(2f)
                    .fillMaxHeight(),
                playerName = players[1],
                count = scores[1]
            ) { onAdd(1) }
            // fourth column
            ButtonsColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(top = 35.dp, start = 8.dp),
                onAdd = { onAdd(1) },
                onDec = { onDec(1) }
            )
        }
    }
}

private fun getImage(number: Int): Int {
    return when(number) {
        0 -> R.drawable.fosforos0
        1 -> R.drawable.fosforos1
        2 -> R.drawable.fosforos2
        3 -> R.drawable.fosforos3
        4 -> R.drawable.fosforos4
        else -> R.drawable.fosforos5
    }
}

@Composable
private fun ButtonsColumn(modifier: Modifier, onAdd: () -> Unit, onDec: () -> Unit) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            onClick = onAdd,
            shape = CircleShape,
            color = MaterialTheme.colorScheme.secondary,
            contentColor = MaterialTheme.colorScheme.onSecondary,
            shadowElevation = 6.dp,
            modifier = Modifier
                .size(48.dp)
                .padding(2.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                modifier = Modifier.padding(2.dp)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Surface(
            onClick = onDec,
            shape = CircleShape,
            color = MaterialTheme.colorScheme.secondary,
            contentColor = MaterialTheme.colorScheme.onSecondary,
            shadowElevation = 6.dp,
            modifier = Modifier
                .size(48.dp)
                .padding(2.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.remove),
                contentDescription = null,
                modifier = Modifier.padding(2.dp)
            )
        }
    }
}

@Composable
private fun MatchesColumn(modifier: Modifier, playerName: String, count: Int, onClick: () -> Unit) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = playerName,
            style = MaterialTheme.typography.titleLarge
        )
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 4.dp,
            color = MaterialTheme.colorScheme.onPrimary
        )
        val images = count / 5
        Column(
            modifier = Modifier
                .fillMaxSize()
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) { onClick() },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (images > 2) {
                for (i in 0 until 3) {
                    Image(
                        painter = painterResource(id = R.drawable.fosforos5),
                        contentDescription = null,
                        modifier = Modifier
                            .weight(1f)
                            .padding(6.dp)
                    )
                }
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    thickness = 4.dp,
                    color = MaterialTheme.colorScheme.onPrimary
                )
                for (i in 3 until images) {
                    Image(
                        painter = painterResource(id = R.drawable.fosforos5),
                        contentDescription = null,
                        modifier = Modifier
                            .weight(1f)
                            .padding(6.dp)
                    )
                }
                if (images < 6) {
                    Image(
                        painter = painterResource(id = getImage(count % 5)),
                        contentDescription = null,
                        modifier = Modifier
                            .weight(1f)
                            .padding(6.dp)
                    )
                    for (i in images + 1 until 6) {
                        Image(
                            painter = painterResource(id = R.drawable.fosforos0),
                            contentDescription = null,
                            modifier = Modifier
                                .weight(1f)
                                .padding(6.dp)
                        )
                    }
                }
            } else {
                for (i in 0 until images) {
                    Image(
                        painter = painterResource(id = R.drawable.fosforos5),
                        contentDescription = null,
                        modifier = Modifier
                            .weight(1f)
                            .padding(6.dp)
                    )
                }
                Image(
                    painter = painterResource(id = getImage(count % 5)),
                    contentDescription = null,
                    modifier = Modifier
                        .weight(1f)
                        .padding(6.dp)
                )
                for (i in images + 1 until 3) {
                    Image(
                        painter = painterResource(id = R.drawable.fosforos0),
                        contentDescription = null,
                        modifier = Modifier
                            .weight(1f)
                            .padding(6.dp)
                    )
                }
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    thickness = 4.dp,
                    color = MaterialTheme.colorScheme.onPrimary
                )
                for (i in 3 until 6) {
                    Image(
                        painter = painterResource(id = R.drawable.fosforos0),
                        contentDescription = null,
                        modifier = Modifier
                            .weight(1f)
                            .padding(6.dp)
                    )
                }
            }
        }
    }
}
