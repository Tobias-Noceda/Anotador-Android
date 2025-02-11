package com.tobs.anotador.frontend.screens.generic

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.tobs.anotador.R
import com.tobs.anotador.frontend.AppDestinations

@Composable
fun MenuScreen(
    modifier: Modifier,
    navController: NavController,
    buttons: List<AppDestinations>,
    history: Boolean = false,
    onBack: (() -> Unit)? = null,
    onClick: (AppDestinations?) -> Unit
) {
    if(onBack != null) {
        BackHandler(onBack = onBack)
    }

    BoxWithConstraints(modifier = modifier) {
        val boxHeight = maxHeight
        val colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.secondary,
            contentColor = MaterialTheme.colorScheme.onSecondary
        )

        Column(
            modifier = Modifier
                .padding(4.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.End
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(boxHeight * 0.25f)
            )
            Column(modifier = Modifier.fillMaxWidth()) {
                for (button in buttons) {
                    Button(
                        onClick = { onClick(button) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 4.dp)
                            .height(boxHeight * 0.09f),
                        shape = RoundedCornerShape(12.dp),
                        colors = colors
                    ) {
                        Text(
                            text = stringResource(id = button.nameRef),
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }
            if (history) {
                Spacer(modifier = Modifier.weight(1f))
                Button(
                    onClick = { navController.navigate(AppDestinations.HISTORY.route) },
                    modifier = Modifier
                        .height(boxHeight * 0.09f)
                        .padding(horizontal = 12.dp, vertical = 4.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = colors
                ) {
                    Text(
                        text = stringResource(id = AppDestinations.HISTORY.nameRef),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    }
}