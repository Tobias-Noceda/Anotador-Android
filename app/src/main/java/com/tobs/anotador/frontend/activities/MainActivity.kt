package com.tobs.anotador.frontend.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.tobs.anotador.R
import com.tobs.anotador.backend.Player
import com.tobs.anotador.frontend.popUps.DismissibleTextPopUp
import com.tobs.anotador.frontend.popUps.OnPopUpClosedListener
import com.tobs.anotador.frontend.popUps.PlayerCountPopUp
import com.tobs.anotador.frontend.popUps.PlayersPopUpClosedListener
import com.tobs.anotador.frontend.popUps.ResumePopUp
import com.tobs.anotador.frontend.popUps.TextPopUp
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity(), OnPopUpClosedListener, PlayersPopUpClosedListener {

    companion object {
        var players: List<Player> = ArrayList()
        var playerCount: Int = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // botones
        val classicBtn = findViewById<Button>(R.id.classic)
        classicBtn.setOnClickListener {
            if(ClassicActivity.game != null) {
                resume(ClassicActivity::class.java)
            } else {
                start(ClassicActivity::class.java)
            }
        }
        val matchesBtn = findViewById<Button>(R.id.matches)
        matchesBtn.setOnClickListener {
            val intent = Intent(this, AdditionalMenuActivity::class.java)
            startActivity(intent)
        }

        val canastaBtn = findViewById<Button>(R.id.canasta)
        canastaBtn.setOnClickListener {
            if(CanastaActivity.canasta != null) {
                resume(CanastaActivity::class.java)
            } else {
                start(CanastaActivity::class.java)
            }
        }

        val generalaBtn = findViewById<Button>(R.id.generala)
        generalaBtn.setOnClickListener {
            if(GeneralaActivity.generala != null) {
                resume(GeneralaActivity::class.java)
            } else {
                start(GeneralaActivity::class.java)
            }
        }

        val cariocaBtn = findViewById<Button>(R.id.carioca)
        cariocaBtn.setOnClickListener {
            if(CariocaActivity.carioca != null) {
                resume(CariocaActivity::class.java)
            } else {
                start(CariocaActivity::class.java)
            }
        }
    }

    override fun onPopUpClosed(result: String, callingClass: Class<out GameActivity>?, playerNumber: Int) {
        if(playerNumber == 0) {
            if(result == "Si") {
                val intent = Intent(this, callingClass)
                startActivity(intent)
            } else {
                players = ArrayList()
                start(callingClass)
            }
        } else {
            players = players + (Player(playerNumber, result))
            if(playerNumber == playerCount) {
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
            Companion.playerCount = playerCount
            val showPopUp = TextPopUp(1, calledClass)
            showPopUp.setOnPopUpClosedListener(this)
            showPopUp.show(supportFragmentManager, "showPopUp")
        }
    }

    private fun resume(calledActivity: Class<out GameActivity>) {
        val popUp = ResumePopUp(calledActivity)
        popUp.setOnPopUpClosedListener(this)
        popUp.show(supportFragmentManager, "Resume")
    }

    private fun askPlayerCount(calledActivity: Class<out GameActivity>) {
        val playerPopUp = PlayerCountPopUp(calledActivity)
        playerPopUp.setOnPopUpClosedListener(this)
        playerPopUp.show(supportFragmentManager, "playerCount")
    }

    private fun start(clase: Class<out GameActivity>?) {
        players = ArrayList()
        playerCount = 0
        when (clase) {
            ClassicActivity::class.java -> {
                ClassicActivity.game = null
                askPlayerCount(ClassicActivity::class.java)
            }

            CanastaActivity::class.java -> {
                CanastaActivity.canasta = null
                playerCount = 2
                val showPopUp = DismissibleTextPopUp(1, CanastaActivity::class.java)
                showPopUp.setOnPopUpClosedListener(this)
                showPopUp.show(supportFragmentManager, "showPopUp")
            }
            GeneralaActivity::class.java -> {
                GeneralaActivity.generala = null
                askPlayerCount(GeneralaActivity::class.java)
            }
            CariocaActivity::class.java -> {
                CariocaActivity.carioca = null
                askPlayerCount(CariocaActivity::class.java)
            }
        }
    }
}