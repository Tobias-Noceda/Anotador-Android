package com.tobs.anotador.frontend.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp

@Composable
fun ClickableNumGridItem(
    content: Number?,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onClick() },
        shape = RectangleShape,
        colors = CardColors(
            MaterialTheme.colorScheme.primary,
            Color.Black,
            MaterialTheme.colorScheme.primary,
            MaterialTheme.colorScheme.primary
        ),
        border = BorderStroke(2.dp, Color.Black)
    ) {
        Text(
            text = content?.toString() ?: "",
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            color = MaterialTheme.colorScheme.onPrimary,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge
        )
    }
}

@Composable
fun ClickableTextGridItem(
    content: String,
    fontSize: TextUnit? = null,
    onClick: () -> Unit
) {
    val defaultTextStyle = MaterialTheme.typography.titleLarge
    val fixedHeight = calculateTextHeight(defaultTextStyle)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(fixedHeight)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onClick() },
        shape = RectangleShape,
        colors = CardColors(
            MaterialTheme.colorScheme.primary,
            Color.Black,
            MaterialTheme.colorScheme.primary,
            MaterialTheme.colorScheme.primary
        ),
        border = BorderStroke(2.dp, Color.Black)
    ) {
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = content,
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            color = MaterialTheme.colorScheme.onPrimary,
            textAlign = TextAlign.Center,
            style = if(fontSize == null) {
                MaterialTheme.typography.titleLarge
            } else {
                MaterialTheme.typography.titleLarge.copy(fontSize = fontSize)
            }
        )
        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
fun DropdownTextGridItem(
    content: String,
    options: List<String>,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) { expanded = true },
            shape = RectangleShape,
            colors = CardColors(
                MaterialTheme.colorScheme.primary,
                Color.Black,
                MaterialTheme.colorScheme.primary,
                MaterialTheme.colorScheme.primary
            ),
            border = BorderStroke(2.dp, Color.Black)
        ) {
            Text(
                text = if(content == "0") "-" else content,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                color = MaterialTheme.colorScheme.onPrimary,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            offset = DpOffset(3.dp, 0.dp),
            containerColor = MaterialTheme.colorScheme.primary,
            border = BorderStroke(1.dp, Color.Black)
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    },
                    colors = MenuDefaults.itemColors(textColor = Color.Black)
                )
            }
        }
    }
}

@Composable
fun UnclickableTextGridItem(content: String, fontSize: TextUnit? = null) {
    val defaultTextStyle = MaterialTheme.typography.titleLarge
    val fixedHeight = calculateTextHeight(defaultTextStyle)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(fixedHeight),
        shape = RectangleShape,
        colors = CardColors(
            MaterialTheme.colorScheme.primary,
            Color.Black,
            MaterialTheme.colorScheme.primary,
            MaterialTheme.colorScheme.primary
        ),
        border = BorderStroke(2.dp, Color.Black)
    ) {
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = content,
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            color = MaterialTheme.colorScheme.onPrimary,
            textAlign = TextAlign.Center,
            style = if (fontSize == null) {
                MaterialTheme.typography.titleLarge
            } else {
                MaterialTheme.typography.titleLarge.copy(fontSize = fontSize)
            }
        )
        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
private fun calculateTextHeight(textStyle: TextStyle): Dp {
    return Dp(textStyle.lineHeight.value) + 25.5.dp
}
