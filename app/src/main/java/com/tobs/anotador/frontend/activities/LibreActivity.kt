package com.tobs.anotador.frontend.activities

import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.tobs.anotador.R
import com.tobs.anotador.backend.Matches
import com.tobs.anotador.backend.Player
import com.tobs.anotador.frontend.popUps.FinishedPopUp
import com.tobs.anotador.frontend.popUps.LimitPopUp
import com.tobs.anotador.frontend.popUps.OnFinishedPopUpListener
import com.tobs.anotador.frontend.popUps.OnPopUpClosedListener
import com.tobs.anotador.frontend.popUps.PlayersPopUpClosedListener
import com.tobs.anotador.frontend.popUps.TextPopUp

class LibreActivity : MatchesActivity(), OnFinishedPopUpListener, OnPopUpClosedListener, PlayersPopUpClosedListener {

    private var finished = false
    private lateinit var gameImages: ArrayList<ArrayList<ImageView>>

    companion object {
        var matches: Matches? = null
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_matches)

        if(matches == null) {
            start(false)
        } else {
            redraw()
        }

        playersImages = gameImages

        val backBtn = findViewById<ImageButton>(R.id.backButton)
        backBtn.setOnClickListener {
            if(finished || (matches?.getScore(0)== "0" && matches?.getScore(1) == "0")) {
                onFinished("Ok")
            } else {
                finish()
            }
        }

        val player1 = findViewById<ConstraintLayout>(R.id.player1)
        player1.setOnClickListener {
            add(0)
        }

        val player2 = findViewById<ConstraintLayout>(R.id.player2)
        player2.setOnClickListener {
            add(1)
        }

        val add1 = findViewById<Button>(R.id.plus1)
        add1.setOnClickListener {
            add(0)
        }

        val add2 = findViewById<Button>(R.id.plus2)
        add2.setOnClickListener {
            add(1)
        }

        val dec1 = findViewById<Button>(R.id.minus1)
        dec1.setOnClickListener {
            dec(0)
        }

        val dec2 = findViewById<Button>(R.id.minus2)
        dec2.setOnClickListener {
            dec(1)
        }
    }

    private fun start(replace: Boolean) {
        matches = Matches(MainActivity.players, AdditionalMenuActivity.limit)
        gameImages = ArrayList()
        for(player in 0 until 2) {
            gameImages.add(ArrayList())
            for(image in 0 until matches?.roundsLimit as Int / 5) {
                gameImages[player].add(getImage(player, image))
                if(replace) {
                    gameImages[player][image].setImageResource(R.drawable.fosforos0)
                }
            }
        }
        drawNames()
    }

    private fun getImage(player: Int, image: Int): ImageView {
        return when(player.toString() + image.toString()) {
            "00" -> {
                findViewById(R.id.matches1_0)
            }

            "01" -> {
                findViewById(R.id.matches1_1)
            }

            "02" -> {
                findViewById(R.id.matches1_2)
            }

            "03" -> {
                findViewById(R.id.matches1_3)
            }

            "04" -> {
                findViewById(R.id.matches1_4)
            }

            "05" -> {
                findViewById(R.id.matches1_5)
            }

            "10" -> {
                findViewById(R.id.matches2_0)
            }

            "11" -> {
                findViewById(R.id.matches2_1)
            }

            "12" -> {
                findViewById(R.id.matches2_2)
            }

            "13" -> {
                findViewById(R.id.matches2_3)
            }

            "14" -> {
                findViewById(R.id.matches2_4)
            }

            else -> {
                findViewById(R.id.matches2_5)
            }
        }
    }

    private fun redraw() {
        gameImages = ArrayList()
        for(player in 0 until 2) {
            gameImages.add(ArrayList())
            try {
                val score = matches?.getScore(player)?.toInt() as Int
                val limit = score / 5
                for(image in 0 until matches?.roundsLimit as Int / 5) {
                    gameImages[player].add(getImage(player, image))
                    if(image < limit) {
                        gameImages[player][image].setImageResource(R.drawable.fosforos5)
                    } else if(image == limit) {
                        gameImages[player][image].setImageResource(images[score % 5])
                    }
                }
            } catch (_: Exception) {}
        }
        drawNames()
    }

    private fun add(playerIndex: Int) {
        try {
            val score = matches?.getScore(playerIndex)?.toInt() as Int
            if(score < matches?.roundsLimit as Int) {
                finished = matches?.addScore(playerIndex, 1) as Boolean
                addScore(playerIndex, score + 1)
            } else {
                finished = true
            }
            if(finished) {
                val finishedPopUp = FinishedPopUp(matches?.players?.get(playerIndex) as String)
                finishedPopUp.setOnPopUpClosedListener(this)
                finishedPopUp.show(supportFragmentManager, "finished")
            }
        } catch (_: Exception) {}
    }

    private fun dec(playerIndex: Int) {
        try {
            if((matches as Matches).getScore(playerIndex).toInt() == matches?.roundsLimit) {
                finished = false
            }
            matches?.addScore(playerIndex, -1)
            decScore(playerIndex, matches?.getScore(playerIndex)?.toInt() as Int)
        } catch (_: Exception) {}
    }

    private fun drawNames() {
        findViewById<TextView>(R.id.p1name).text = matches?.players?.get(0)
        findViewById<TextView>(R.id.p2name).text = matches?.players?.get(1)
    }

    override fun onFinished(command: String) {
        matches = null
        finished = false
        if(command == "Ok") {
            finish()
        } else {
            val popUp = LimitPopUp(this.javaClass)
            popUp.setOnPopUpClosedListener(this)
            popUp.show(supportFragmentManager, "players")
        }
    }

    override fun onPopUpClosed(
        result: String,
        callingClass: Class<out GameActivity>?,
        playerNumber: Int
    ) {
        MainActivity.players = MainActivity.players + Player(playerNumber - 1, result)
        if(playerNumber == 2) {
            start(true)
        } else {
            val popUp = TextPopUp(playerNumber + 1, this.javaClass)
            popUp.setOnPopUpClosedListener(this)
            popUp.show(supportFragmentManager, "player2")
        }
    }

    override fun getPlayerCount(playerCount: Int, calledClass: Class<out GameActivity>) {
        if(playerCount != 0) {
            AdditionalMenuActivity.limit = playerCount
            val showPopUp = TextPopUp(1, calledClass)
            showPopUp.setOnPopUpClosedListener(this)
            showPopUp.show(supportFragmentManager, "player1")
        }
    }
}