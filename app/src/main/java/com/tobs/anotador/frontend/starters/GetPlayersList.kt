package com.tobs.anotador.frontend.starters

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.tobs.anotador.backend.Player
import com.tobs.anotador.frontend.components.popUps.PlayerPopup

@Composable
fun GetPlayersList(playerCount: Int = 2, onComplete: (List<Player>) -> Unit) {
    var toRet by rememberSaveable { mutableStateOf(emptyList<Player>()) }
    var closed by rememberSaveable { mutableStateOf(false) }
    var showPopUp by rememberSaveable { mutableStateOf(true) }
    var playerIndex by rememberSaveable { mutableIntStateOf(1) }

    if (showPopUp && playerIndex <= playerCount) {
        PlayerPopup(
            index = playerIndex,
            onOk = { playerName ->
                toRet += Player(playerIndex, playerName)
                showPopUp = false
                playerIndex++
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
                onComplete(emptyList())
                finished = true
            } else if (playerIndex > playerCount) {
                onComplete(toRet)
                finished = true
            } else {
                showPopUp = true
            }
            if (finished) {
                toRet = emptyList()
                closed = false
                showPopUp = true
                playerIndex = 1
            }
        }
    }
}