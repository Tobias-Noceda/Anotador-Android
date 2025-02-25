package com.tobs.anotador.frontend

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tobs.anotador.R
import com.tobs.anotador.backend.Canasta
import com.tobs.anotador.backend.Carioca
import com.tobs.anotador.backend.ClassicScorekeeper
import com.tobs.anotador.backend.Cocktail
import com.tobs.anotador.backend.Generala
import com.tobs.anotador.backend.Matches
import com.tobs.anotador.backend.Player
import com.tobs.anotador.frontend.components.popUps.ResumePopUp
import com.tobs.anotador.frontend.components.popUps.TrucoPopUp
import com.tobs.anotador.frontend.starters.WantLimitPopUp
import com.tobs.anotador.frontend.screens.CanastaScreen
import com.tobs.anotador.frontend.screens.CariocaScreen
import com.tobs.anotador.frontend.screens.ClassicScreen
import com.tobs.anotador.frontend.screens.CocktailScreen
import com.tobs.anotador.frontend.screens.GeneralaScreen
import com.tobs.anotador.frontend.screens.HistoryScreen
import com.tobs.anotador.frontend.screens.generic.MatchesScreen
import com.tobs.anotador.frontend.screens.generic.MenuScreen
import com.tobs.anotador.frontend.starters.GetPlayerCount
import com.tobs.anotador.frontend.starters.GetPlayersList
import com.tobs.anotador.frontend.starters.GetPointLimit
import com.tobs.anotador.frontend.starters.updateDb
import com.tobs.anotador.viewModel.GameViewModel

enum class AppDestinations(
    @StringRes val nameRef: Int,
    val route: String
) {
    HOME(R.string.app_name, "home"),
    CLASSIC(R.string.classic, "classic"),
    MATCHES(R.string.matches, "matches"),
    TRUCO(R.string.truco, "truco"),
    ESCOBA(R.string.escoba, "escoba"),
    FREE(R.string.free, "free"),
    GENERALA(R.string.generala, "generala"),
    CANASTA(R.string.canasta, "canasta"),
    CARIOCA(R.string.carioca, "carioca"),
    COCKTAIL(R.string.cocktail, "cocktail"),
    HISTORY(R.string.history, "history")
}

@Composable
fun AnotadorApp(
    navController: NavHostController = rememberNavController(),
    gameViewModel: GameViewModel = viewModel()
) {
    with(gameViewModel) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primary)
        ) {
            Router(
                navController,
                classic,
                truco,
                escoba,
                free,
                generala,
                canasta,
                carioca,
                cocktail,
                setClicked = { clicked = it },
                restart = { restart = true }
            ) {
                when (it) {
                    AppDestinations.CLASSIC -> {
                        classic = null
                    }

                    AppDestinations.TRUCO -> {
                        truco = null
                    }

                    AppDestinations.ESCOBA -> {
                        escoba = null
                    }

                    AppDestinations.FREE -> {
                        free = null
                    }

                    AppDestinations.GENERALA -> {
                        generala = null
                    }

                    AppDestinations.CANASTA -> {
                        canasta = null
                    }

                    AppDestinations.CARIOCA -> {
                        carioca = null
                    }

                    AppDestinations.COCKTAIL -> {
                        cocktail = null
                    }

                    else -> {}
                }
            }
            when (clicked) {
                AppDestinations.CLASSIC -> {
                    if (classic == null || restart) {
                        var hasLimit by rememberSaveable { mutableStateOf<Boolean?>(null) }
                        var limit by rememberSaveable { mutableIntStateOf(0) }
                        if(hasLimit == null) {
                            WantLimitPopUp(
                                onYes = { hasLimit = true },
                                onNo = { hasLimit = false }
                            ) {
                                clicked = null
                            }
                        } else if(hasLimit!! && limit == 0) {
                            GetPointLimit(
                                boundsCheck = { it > 0 },
                                outBoundsMsg = stringResource(id = R.string.positive)
                            ) {
                                if (it == null) {
                                    clicked = null
                                    restart = false
                                } else {
                                    limit = it
                                }
                            }
                        } else {
                            var playerCount by rememberSaveable { mutableIntStateOf(0) }
                            if (playerCount == 0) {
                                GetPlayerCount {
                                    if (it == null) {
                                        clicked = null
                                        restart = false
                                    } else {
                                        playerCount = it
                                    }
                                }
                            } else {
                                GetPlayersList(playerCount) { collectedPlayers ->
                                    if (collectedPlayers.isNotEmpty()) {
                                        playersList = collectedPlayers
                                        classic = ClassicScorekeeper(playersList, limit)
                                        clicked?.let {
                                            if (restart) {
                                                navController.navigateUp()
                                            }
                                            navController.navigate(it.route)
                                        }
                                    }
                                    clicked = null
                                    restart = false
                                }
                            }
                        }
                    } else {
                        ResumePopUp(
                            onYes = {
                                clicked?.let { navController.navigate(it.route) }; clicked = null
                            },
                            onNo = { classic = null }
                        ) { clicked = null }
                    }
                }

                AppDestinations.MATCHES -> {
                    navController.navigate(AppDestinations.MATCHES.route)
                }

                AppDestinations.TRUCO -> {
                    if (truco == null || restart) {
                        playersList = listOf(
                            Player(1, stringResource(id = R.string.us)),
                            Player(2, stringResource(id = R.string.them))
                        )
                        TrucoPopUp { limit ->
                            if (limit != null) {
                                truco = Matches(playersList, limit)
                                clicked?.let {
                                    if (restart) {
                                        navController.navigateUp()
                                    }
                                    navController.navigate(it.route)
                                }
                            }
                            clicked = null
                            restart = false
                        }
                    } else {
                        ResumePopUp(
                            onYes = {
                                clicked?.let { navController.navigate(it.route) }; clicked = null
                            },
                            onNo = { truco = null }
                        ) { clicked = null }
                    }
                }

                AppDestinations.ESCOBA -> {
                    if (escoba == null || restart) {
                        GetPlayersList { collectedPlayers ->
                            if (collectedPlayers.isNotEmpty()) {
                                playersList = collectedPlayers
                                escoba = Matches(playersList, 15)
                                clicked?.let {
                                    if (restart) {
                                        navController.navigateUp()
                                    }
                                    navController.navigate(it.route)
                                }
                            }
                            clicked = null
                            restart = false
                        }
                    } else {
                        ResumePopUp(
                            onYes = {
                                clicked?.let { navController.navigate(it.route) }; clicked = null
                            },
                            onNo = { escoba = null }
                        ) { clicked = null }
                    }
                }

                AppDestinations.FREE -> {
                    if (free == null || restart) {
                        var limit by rememberSaveable { mutableIntStateOf(0) }
                        if (limit == 0) {
                            GetPointLimit(
                                boundsCheck = { it in 1..30 },
                                outBoundsMsg = stringResource(id = R.string.matches_limit)
                            ) {
                                if (it == null) {
                                    clicked = null
                                    restart = false
                                } else {
                                    limit = it
                                }
                            }
                        } else {
                            GetPlayersList { collectedPlayers ->
                                if (collectedPlayers.isNotEmpty()) {
                                    playersList = collectedPlayers
                                    free = Matches(playersList, limit)
                                    clicked?.let {
                                        if (restart) {
                                            navController.navigateUp()
                                        }
                                        navController.navigate(it.route)
                                    }
                                }
                                clicked = null
                                restart = false
                            }
                        }
                    } else {
                        ResumePopUp(
                            onYes = {
                                clicked?.let { navController.navigate(it.route) }; clicked = null
                            },
                            onNo = { free = null }
                        ) { clicked = null }
                    }
                }

                AppDestinations.GENERALA -> {
                    if (generala == null || restart) {
                        var playerCount by rememberSaveable { mutableIntStateOf(0) }
                        if (playerCount == 0) {
                            GetPlayerCount {
                                if (it == null) {
                                    clicked = null
                                    restart = false
                                } else {
                                    playerCount = it
                                }
                            }
                        } else {
                            GetPlayersList(playerCount) { collectedPlayers ->
                                if (collectedPlayers.isNotEmpty()) {
                                    playersList = collectedPlayers
                                    generala = Generala(playersList, playerCount)
                                    clicked?.let {
                                        if (restart) {
                                            navController.navigateUp()
                                        }
                                        navController.navigate(it.route)
                                    }
                                }
                                clicked = null
                                restart = false
                            }
                        }
                    } else {
                        ResumePopUp(
                            onYes = {
                                clicked?.let { navController.navigate(it.route) }; clicked = null
                            },
                            onNo = { generala = null }
                        ) { clicked = null }
                    }
                }

                AppDestinations.CANASTA -> {
                    if (canasta == null || restart) {
                        GetPlayersList { collectedPlayers ->
                            if (collectedPlayers.isNotEmpty()) {
                                playersList = collectedPlayers
                                canasta = Canasta(playersList, 1500, 3000, 5000)
                                clicked?.let {
                                    if (restart) {
                                        navController.navigateUp()
                                    }
                                    navController.navigate(it.route)
                                }
                            }
                            clicked = null
                            restart = false
                        }
                    } else {
                        ResumePopUp(
                            onYes = {
                                clicked?.let { navController.navigate(it.route) }; clicked = null
                            },
                            onNo = { canasta = null }
                        ) { clicked = null }
                    }
                }

                AppDestinations.CARIOCA -> {
                    if (carioca == null || restart) {
                        var playerCount by rememberSaveable { mutableIntStateOf(0) }
                        if (playerCount == 0) {
                            GetPlayerCount {
                                if (it == null) {
                                    clicked = null
                                    restart = false
                                } else {
                                    playerCount = it
                                }
                            }
                        } else {
                            GetPlayersList(playerCount) { collectedPlayers ->
                                if (collectedPlayers.isNotEmpty()) {
                                    playersList = collectedPlayers
                                    carioca = Carioca(playersList, 2)
                                    clicked?.let {
                                        if (restart) {
                                            navController.navigateUp()
                                        }
                                        navController.navigate(it.route)
                                    }
                                }
                                clicked = null
                                restart = false
                            }
                        }
                    } else {
                        ResumePopUp(
                            onYes = {
                                clicked?.let { navController.navigate(it.route) }; clicked = null
                            },
                            onNo = { carioca = null }
                        ) { clicked = null }
                    }
                }

                AppDestinations.COCKTAIL -> {
                    if (cocktail == null || restart) {
                        GetPlayersList(4) { collectedPlayers ->
                            if (collectedPlayers.isNotEmpty()) {
                                playersList = collectedPlayers
                                cocktail = Cocktail(playersList)
                                clicked?.let {
                                    if (restart) {
                                        navController.navigateUp()
                                    }
                                    navController.navigate(it.route)
                                }
                            }
                            clicked = null
                            restart = false
                        }
                    } else {
                        ResumePopUp(
                            onYes = {
                                clicked?.let { navController.navigate(it.route) }; clicked = null
                            },
                            onNo = { cocktail = null }
                        ) { clicked = null }
                    }
                }

                else -> {}
            }
        }
    }
}

@Composable
private fun Router(
    navController: NavHostController,
    classic: ClassicScorekeeper?,
    truco: Matches?,
    escoba: Matches?,
    free: Matches?,
    generala: Generala?,
    canasta: Canasta?,
    carioca: Carioca?,
    cocktail: Cocktail?,
    setClicked: (AppDestinations?) -> Unit,
    restart: () -> Unit,
    reset: (AppDestinations) -> Unit
) {
    val modifier = Modifier.fillMaxSize()
    val context = LocalContext.current

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable(AppDestinations.HOME.route) {
            MenuScreen(
                modifier = modifier,
                navController = navController,
                buttons = listOf(
                    AppDestinations.CLASSIC,
                    AppDestinations.MATCHES,
                    AppDestinations.GENERALA,
                    AppDestinations.CANASTA,
                    AppDestinations.CARIOCA,
                    AppDestinations.COCKTAIL
                ),
                history = true
            ) { setClicked(it) }
        }

        composable(AppDestinations.CLASSIC.route) {
            ClassicScreen(
                modifier = modifier,
                scorer = classic!!,
                onBack = { winner ->
                    if(classic.limit == 100 && classic.players.size == 2) {
                        updateDb(context,"Chi", classic.players, winner)
                    }
                    navController.navigateUp()
                    reset(AppDestinations.CLASSIC)
                }
            ) { winner ->
                if(classic.limit == 100 && classic.players.size == 2) {
                    updateDb(context,"Chi", classic.players, winner)
                }
                restart()
                setClicked(AppDestinations.CLASSIC)
            }
        }

        composable(AppDestinations.MATCHES.route) {
            MenuScreen(
                modifier = modifier,
                navController = navController,
                buttons = listOf(
                    AppDestinations.TRUCO,
                    AppDestinations.ESCOBA,
                    AppDestinations.FREE
                ),
                history = false,
                onBack = { navController.navigateUp(); setClicked(null) }
            ) { setClicked(it) }
        }

        composable(AppDestinations.TRUCO.route) {
            MatchesScreen(
                modifier = modifier,
                matches = truco!!,
                onBack = { _, _ -> navController.navigateUp(); reset(AppDestinations.TRUCO) }
            ) { _, _ ->
                restart()
                setClicked(AppDestinations.TRUCO)
            }
        }

        composable(AppDestinations.ESCOBA.route) {
            MatchesScreen(
                modifier = modifier,
                matches = escoba!!,
                onBack = { winner, _ ->
                    updateDb(context, "Esc", escoba.players, winner)
                    navController.navigateUp()
                    reset(AppDestinations.ESCOBA)
                }
            ) { winner, _ ->
                updateDb(context, "Esc", escoba.players, winner)
                restart()
                setClicked(AppDestinations.ESCOBA)
            }
        }

        composable(AppDestinations.FREE.route) {
            MatchesScreen(
                modifier = modifier,
                matches = free!!,
                onBack = { winner, limit ->
                    if (limit == 15 || limit == 30) {
                        updateDb(context, "Tru", free.players, winner)
                    }
                    navController.navigateUp()
                    reset(AppDestinations.FREE)
                }
            ) { winner, limit ->
                if (limit == 15 || limit == 30) {
                    updateDb(context, "Tru", free.players, winner)
                }
                restart()
                setClicked(AppDestinations.FREE)
            }
        }

        composable(AppDestinations.GENERALA.route) {
            GeneralaScreen(
                modifier = modifier,
                generala = generala!!,
                onBack = { winner ->
                    if(generala.players.size == 3) {
                        val myPlayers = generala.players.drop(1).toMutableList()
                        updateDb(context, "Gen", myPlayers, winner)
                    }
                    navController.navigateUp()
                    reset(AppDestinations.GENERALA)
                }
            ) { winner ->
                if(generala.players.size == 3) {
                    val myPlayers = generala.players.drop(1).toMutableList()
                    updateDb(context, "Gen", myPlayers, winner)
                }
                restart()
                setClicked(AppDestinations.GENERALA)
            }
        }

        composable(AppDestinations.CANASTA.route) {
            CanastaScreen(
                modifier = modifier,
                canasta = canasta!!,
                onBack = { winner ->
                    updateDb(context, "Can", canasta.players, winner)
                    navController.navigateUp()
                    reset(AppDestinations.CANASTA)
                }
            ) { winner ->
                updateDb(context, "Can", canasta.players, winner)
                restart()
                setClicked(AppDestinations.CANASTA)
            }
        }

        composable(AppDestinations.CARIOCA.route) {
            CariocaScreen(
                modifier = modifier,
                carioca = carioca!!,
                onBack = { navController.navigateUp(); reset(AppDestinations.CARIOCA) }
            ) {
                restart()
                setClicked(AppDestinations.CANASTA)
            }
        }

        composable(AppDestinations.COCKTAIL.route) {
            CocktailScreen(
                modifier = modifier,
                cocktail = cocktail!!,
                onBack = { navController.navigateUp(); reset(AppDestinations.COCKTAIL) }
            ) {
                restart()
                setClicked(AppDestinations.COCKTAIL)
            }
        }
        
        composable(AppDestinations.HISTORY.route) {
            HistoryScreen(modifier = modifier)
        }
    }
}
