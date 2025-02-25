package com.tobs.anotador.frontend.starters

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.tobs.anotador.R
import com.tobs.anotador.frontend.components.popUps.GetNumberPopup

@Composable
fun GetPointLimit(boundsCheck: (Int) -> Boolean, outBoundsMsg: String, onDefined: (Int?) -> Unit) {
    GetNumberPopup(
        placeHolder = stringResource(id = R.string.free_limit),
        checkNum = boundsCheck,
        outOfBounds = outBoundsMsg,
        onOk = { playerCount ->
            onDefined(playerCount)
        }
    ) {
        onDefined(null)
    }
}