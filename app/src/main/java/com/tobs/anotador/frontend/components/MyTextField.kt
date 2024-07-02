package com.tobs.anotador.frontend.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization

@Composable
fun MyTextField(
    placeholder: String,
    modifier: Modifier,
    onDone: () -> Unit,
    onUse: (String) -> Unit
) {
    var value: String by rememberSaveable { mutableStateOf("") }
    val textFieldColors = TextFieldDefaults.colors(
        focusedTextColor = MaterialTheme.colorScheme.onPrimary,
        unfocusedTextColor = MaterialTheme.colorScheme.onPrimary,
        disabledTextColor = MaterialTheme.colorScheme.onPrimary,
        errorTextColor = MaterialTheme.colorScheme.onPrimary,
        focusedContainerColor = MaterialTheme.colorScheme.primary,
        unfocusedContainerColor = MaterialTheme.colorScheme.primary,
        disabledContainerColor = MaterialTheme.colorScheme.primary,
        errorContainerColor = MaterialTheme.colorScheme.primary,
        cursorColor = MaterialTheme.colorScheme.onPrimary,
        errorCursorColor = MaterialTheme.colorScheme.onPrimary,
        selectionColors = TextSelectionColors(
            handleColor = MaterialTheme.colorScheme.surface,
            backgroundColor = Color(0xff3d6ed4).copy(alpha = 0.3f)
        ),
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        disabledIndicatorColor = Color.Transparent,
        errorIndicatorColor = Color.Transparent,
        focusedPlaceholderColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8F),
        unfocusedPlaceholderColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8F),
        disabledPlaceholderColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8F),
        errorPlaceholderColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8F)
    )

    TextField(
        value = value,
        onValueChange = { value = it; onUse(value) },
        placeholder = { Text(placeholder) },
        modifier = modifier,
        colors = textFieldColors,
        keyboardOptions = KeyboardOptions.Default.copy(
            capitalization = KeyboardCapitalization.Sentences,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = { onDone() }
        )
    )
}