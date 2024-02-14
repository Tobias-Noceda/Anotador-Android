package com.tobs.anotador.frontend.popUps

import com.tobs.anotador.frontend.activities.GameActivity

interface OnPopUpClosedListener {
    fun onPopUpClosed(result: String, callingClass: Class<out GameActivity>?, playerNumber: Int)
}