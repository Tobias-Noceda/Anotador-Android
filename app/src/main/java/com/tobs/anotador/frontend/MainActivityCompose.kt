package com.tobs.anotador.frontend

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.tobs.anotador.ui.theme.AnotadorTheme

class MainActivityCompose : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AnotadorTheme {
                AnotadorApp()
            }
        }
    }
}