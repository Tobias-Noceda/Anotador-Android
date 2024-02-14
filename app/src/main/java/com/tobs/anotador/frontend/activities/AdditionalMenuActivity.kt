package com.tobs.anotador.frontend.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import com.tobs.anotador.R
import com.tobs.anotador.backend.Player
import com.tobs.anotador.frontend.popUps.DismissibleTextPopUp
import com.tobs.anotador.frontend.popUps.LimitPopUp
import com.tobs.anotador.frontend.popUps.OnPopUpClosedListener
import com.tobs.anotador.frontend.popUps.PlayersPopUpClosedListener
import com.tobs.anotador.frontend.popUps.ResumePopUp
import com.tobs.anotador.frontend.popUps.TextPopUp
import com.tobs.anotador.frontend.popUps.TrucoPopUp

class AdditionalMenuActivity : AppCompatActivity(), OnPopUpClosedListener, PlayersPopUpClosedListener {

    companion object {
        var limit = 0
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aditional_menu)

        // botones
        val trucoBtn = findViewById<Button>(R.id.truco)
        trucoBtn.setOnClickListener {
            if(TrucoActivity.truco != null) {
                resume(TrucoActivity::class.java)
            } else {
                start(TrucoActivity::class.java)
            }
        }

        val escobaBtn = findViewById<Button>(R.id.escoba)
        escobaBtn.setOnClickListener {
            if(EscobaActivity.escoba != null) {
                resume(EscobaActivity::class.java)
            } else {
                start(EscobaActivity::class.java)
            }
        }

        val libreBtn = findViewById<Button>(R.id.libre)
        libreBtn.setOnClickListener {
            if(LibreActivity.matches != null) {
                resume(LibreActivity::class.java)
            } else {
                start(LibreActivity::class.java)
            }
        }

        val backBtn = findViewById<ImageButton>(R.id.backButton)
        backBtn.setOnClickListener {
            finish()
        }
    }

    private fun start(clase: Class<out GameActivity>?) {
        MainActivity.players = ArrayList()
        MainActivity.playerCount = 0
        when (clase) {
            TrucoActivity::class.java -> {
                TrucoActivity.truco = null
                TrucoPopUp(this).show(supportFragmentManager, "elegir")
            }

            EscobaActivity::class.java -> {
                EscobaActivity.escoba = null
                val popUp = DismissibleTextPopUp(1, EscobaActivity::class.java)
                popUp.setOnPopUpClosedListener(this)
                popUp.show(supportFragmentManager, "escoba")
            }

            LibreActivity::class.java -> {
                LibreActivity.matches = null
                val  popUp = LimitPopUp(LibreActivity::class.java)
                popUp.setOnPopUpClosedListener(this)
                popUp.show(supportFragmentManager, "libre")
            }
        }
    }

    private fun resume(calledActivity: Class<out GameActivity>) {
        val popUp = ResumePopUp(calledActivity)
        popUp.setOnPopUpClosedListener(this)
        popUp.show(supportFragmentManager, "Resume")
    }

    override fun onPopUpClosed(
        result: String,
        callingClass: Class<out GameActivity>?,
        playerNumber: Int
    ) {
        if(playerNumber == 0) {
            if(result == "Si") {
                val intent = Intent(this, callingClass)
                startActivity(intent)
            } else {
                MainActivity.players = ArrayList()
                start(callingClass)
            }
        } else {
            MainActivity.players = MainActivity.players + (Player(playerNumber - 1, result))
            if(playerNumber == 2) {
                val intent = Intent(this, callingClass)
                startActivity(intent)
            } else {
                val popUp = TextPopUp(playerNumber + 1, callingClass)
                popUp.setOnPopUpClosedListener(this)
                popUp.show(supportFragmentManager, "showPopUp")
            }
        }
    }

    override fun getPlayerCount(playerCount: Int, calledClass: Class<out GameActivity>) {
        if(playerCount != 0) {
            limit = playerCount
            val showPopUp = TextPopUp(1, calledClass)
            showPopUp.setOnPopUpClosedListener(this)
            showPopUp.show(supportFragmentManager, "player1")
        }
    }
}