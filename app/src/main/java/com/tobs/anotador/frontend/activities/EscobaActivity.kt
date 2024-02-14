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
import com.tobs.anotador.frontend.popUps.DismissibleTextPopUp
import com.tobs.anotador.frontend.popUps.FinishedPopUp
import com.tobs.anotador.frontend.popUps.OnFinishedPopUpListener
import com.tobs.anotador.frontend.popUps.OnPopUpClosedListener
import com.tobs.anotador.frontend.popUps.TextPopUp

class EscobaActivity : MatchesActivity(), OnFinishedPopUpListener, OnPopUpClosedListener {

    private var finished = false
    private lateinit var escobaImages: ArrayList<ArrayList<ImageView>>

    companion object {
        var escoba: Matches? = null
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_matches)

        if(escoba == null) {
            start(false)
        } else {
            redraw()
        }

        playersImages = escobaImages

        val backBtn = findViewById<ImageButton>(R.id.backButton)
        backBtn.setOnClickListener {
            if(finished || (escoba?.getScore(0)== "0" && escoba?.getScore(1) == "0")) {
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
        escoba = Matches(MainActivity.players, 15)
        escobaImages = ArrayList()
        for(player in 0 until 2) {
            escobaImages.add(ArrayList())
            for(image in 0 until escoba?.roundsLimit as Int / 5) {
                escobaImages[player].add(getImage(player, image))
                if(replace) {
                    escobaImages[player][image].setImageResource(R.drawable.fosforos0)
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

            "10" -> {
                findViewById(R.id.matches2_0)
            }

            "11" -> {
                findViewById(R.id.matches2_1)
            }

            else -> {
                findViewById(R.id.matches2_2)
            }
        }
    }

    private fun redraw() {
        escobaImages = ArrayList()
        for(player in 0 until 2) {
            escobaImages.add(ArrayList())
            try {
                val score = escoba?.getScore(player)?.toInt() as Int
                val limit = score / 5
                for(image in 0 until escoba?.roundsLimit as Int / 5) {
                    escobaImages[player].add(getImage(player, image))
                    if(image < limit) {
                        escobaImages[player][image].setImageResource(R.drawable.fosforos5)
                    } else if(image == limit) {
                        escobaImages[player][image].setImageResource(images[score % 5])
                    }
                }
            } catch (_: Exception) {}
        }
        drawNames()
    }

    private fun add(playerIndex: Int) {
        try {
            val score = escoba?.getScore(playerIndex)?.toInt() as Int
            if(score < escoba?.roundsLimit as Int) {
                finished = escoba?.addScore(playerIndex, 1) as Boolean
                addScore(playerIndex, score + 1)
            }
            if(finished) {
                val finishedPopUp = FinishedPopUp(escoba?.players?.get(playerIndex) as String)
                finishedPopUp.setOnPopUpClosedListener(this)
                finishedPopUp.show(supportFragmentManager, "finished")
            }
        } catch (_: Exception) {}
    }

    private fun dec(playerIndex: Int) {
        finished = false
        escoba?.addScore(playerIndex, -1)
        try {
            decScore(playerIndex, escoba?.getScore(playerIndex)?.toInt() as Int)
        } catch (_: Exception) {}
    }

    private fun drawNames() {
        findViewById<TextView>(R.id.p1name).text = escoba?.players?.get(0)
        findViewById<TextView>(R.id.p2name).text = escoba?.players?.get(1)
    }

    override fun onFinished(command: String) {
        escoba = null
        finished = false
        if(command == "Ok") {
            finish()
        } else {
            val popUp = DismissibleTextPopUp(1, this.javaClass)
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
}