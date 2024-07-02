package com.tobs.anotador.frontend.starters

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.tobs.anotador.R
import com.tobs.anotador.frontend.components.popUps.GetNumberPopup

@Composable
fun GetPlayerCount(onDefined: (Int?) -> Unit) {
    GetNumberPopup(
        placeHolder = stringResource(id = R.string.amount),
        checkNum = { it > 1 },
        outOfBounds = stringResource(id = R.string.positive),
        onOk = { playerCount ->
            onDefined(playerCount)
        }
    ) {
        onDefined(null)
    }
}