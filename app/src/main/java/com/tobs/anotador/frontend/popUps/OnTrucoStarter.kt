package com.tobs.anotador.frontend.popUps

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.tobs.anotador.frontend.activities.AdditionalMenuActivity
import com.tobs.anotador.frontend.activities.TrucoActivity

class OnTrucoStarter {
    companion object {
        fun start(limit: Int, caller: AppCompatActivity) {
            AdditionalMenuActivity.limit = limit
            if (caller.javaClass == AdditionalMenuActivity::class.java) {
                val intent = Intent(caller, TrucoActivity::class.java)
                caller.startActivity(intent)
            } else {
                (caller as TrucoActivity).restart()
            }
        }
    }
}