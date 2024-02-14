package com.tobs.anotador.frontend.popUps

import com.tobs.anotador.frontend.activities.GameActivity

interface PlayersPopUpClosedListener {
    fun getPlayerCount(playerCount: Int, calledClass: Class<out GameActivity>)
}