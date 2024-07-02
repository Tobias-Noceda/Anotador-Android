package com.tobs.anotador.frontend.starters

import android.content.Context
import com.tobs.anotador.backend.history.DatabaseHelper

private fun hasToUpdate(players: List<String>): Boolean {
    if(players.contains("Jo") || players.contains("Jose")) {
        if(players.size == 2 && (players.contains("Tou") || players.contains("Tobi") || players.contains("To"))) {
            return true
        }
    }

    return false
}

private fun getHistoryName(winner: String): String {
    val toRet = if(winner == "Jo" || winner == "Jose") {
        "jose"
    } else {
        "tobi"
    }

    return toRet
}

fun updateDb(context: Context, game: String, players: List<String>, winner: String) {
    if(hasToUpdate(players)) {
        val dbHelper = DatabaseHelper(context)
        dbHelper.incrementResult(game, getHistoryName(winner))
    }
}

fun getFromDb(context: Context, player: String , game: String): String {
    val dbHelper = DatabaseHelper(context)
    return dbHelper.getResult(player, game)
}