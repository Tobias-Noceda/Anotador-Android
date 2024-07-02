package com.tobs.anotador.frontend.components.popUps

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.tobs.anotador.R
import com.tobs.anotador.frontend.components.MyTextField

@Composable
fun GetNumberPopup(
    placeHolder: String,
    checkNum: (Int) -> Boolean,
    outOfBounds: String,
    onOk: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    var numberString: String by rememberSaveable { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }
    val context = LocalContext.current

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(dismissOnClickOutside = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .imePadding()
                .background(Color.Transparent),
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.primary,
            shadowElevation = 8.dp,
            border = BorderStroke(3.7.dp, Color.Black)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Spacer(modifier = Modifier.weight(1f))
                    Button(
                        onClick = { onDismiss() },
                        shape = RoundedCornerShape(topEnd = 16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Red,
                            contentColor = Color.White
                        )
                    ) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = null
                        )
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
                val onDone = {
                    val number = numberString.toIntOrNull()
                    if (number != null && checkNum(number))
                        onOk(number)
                    else
                        Toast.makeText(context, outOfBounds, Toast.LENGTH_SHORT).show()
                }
                Row(modifier = Modifier.padding(12.dp)) {
                    MyTextField(
                        placeholder = placeHolder,
                        onDone = onDone,
                        modifier = Modifier
                            .weight(1f)
                            .background(MaterialTheme.colorScheme.primary)
                            .focusRequester(focusRequester)
                    ) { numberString = it }
                    Button(
                        onClick = onDone,
                        modifier = Modifier.padding(start = 8.dp, top = 4.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondary,
                            contentColor = Color.White
                        )
                    ) {
                        Text(stringResource(id = R.string.ok))
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}
