package com.tobs.anotador.viewModel

import androidx.lifecycle.ViewModel
import com.tobs.anotador.backend.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.tobs.anotador.frontend.AppDestinations

class GameViewModel : ViewModel() {
    var clicked: AppDestinations? by mutableStateOf(null)
    var playersList: List<Player> by mutableStateOf(emptyList())
    var restart: Boolean by mutableStateOf(false)

    // Games
    var classic: ClassicScorekeeper? by mutableStateOf(null)
    var truco: Matches? by mutableStateOf(null)
    var escoba: Matches? by mutableStateOf(null)
    var free: Matches? by mutableStateOf(null)
    var generala: Generala? by mutableStateOf(null)
    var canasta: Canasta? by mutableStateOf(null)
    var carioca: Carioca? by mutableStateOf(null)
    var cocktail: Cocktail? by mutableStateOf(null)
}