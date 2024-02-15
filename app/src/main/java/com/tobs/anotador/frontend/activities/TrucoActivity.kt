package com.tobs.anotador.frontend.activities

import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.tobs.anotador.R
import com.tobs.anotador.backend.Matches
import com.tobs.anotador.backend.Player
import com.tobs.anotador.frontend.popUps.FinishedPopUp
import com.tobs.anotador.frontend.popUps.OnFinishedPopUpListener
import com.tobs.anotador.frontend.popUps.TrucoPopUp

class TrucoActivity : MatchesActivity(), OnFinishedPopUpListener {

    private var finished = false
    private lateinit var trucoImages: ArrayList<ArrayList<ImageView>>

    companion object {
        var truco: Matches? = null
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_matches)

        if(truco == null) {
            start(false)
        } else {
            redraw()
        }

        playersImages = trucoImages

        val backBtn = findViewById<ImageButton>(R.id.backButton)
        backBtn.setOnClickListener {
            if(finished || (truco?.getScore(0)== "0" && truco?.getScore(1) == "0")) {
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
        truco = Matches(listOf(Player(0, "Nos"), Player(1, "Ellos")), AdditionalMenuActivity.limit)
        trucoImages = ArrayList()
        for(player in 0 until 2) {
            trucoImages.add(ArrayList())
            for(image in 0 until truco?.roundsLimit as Int / 5) {
                trucoImages[player].add(getImage(player, image))
                if(replace) {
                    trucoImages[player][image].setImageResource(R.drawable.fosforos0)
                }
            }
        }
    }

    fun restart() {
        start(true)
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
        trucoImages = ArrayList()
        for(player in 0 until 2) {
            trucoImages.add(ArrayList())
            try {
                val score = truco?.getScore(player)?.toInt() as Int
                val limit = score / 5
                for(image in 0 until truco?.roundsLimit as Int / 5) {
                    trucoImages[player].add(getImage(player, image))
                    if(image < limit) {
                        trucoImages[player][image].setImageResource(R.drawable.fosforos5)
                    } else if(image == limit) {
                        trucoImages[player][image].setImageResource(images[score % 5])
                    }
                }
            } catch (_: Exception) {}
        }
    }

    private fun add(playerIndex: Int) {
        try {
            val score = truco?.getScore(playerIndex)?.toInt() as Int
            if(score < truco?.roundsLimit as Int) {
                finished = truco?.addScore(playerIndex, 1) as Boolean
                addScore(playerIndex, score + 1)
            } else {
                finished = true
            }
            if(finished) {
                val finishedPopUp = FinishedPopUp(truco?.players?.get(playerIndex) as String)
                finishedPopUp.setOnPopUpClosedListener(this)
                finishedPopUp.show(supportFragmentManager, "finished")
            }
        } catch (_: Exception) {}
    }

    private fun dec(playerIndex: Int) {
        try {
            if((truco as Matches).getScore(playerIndex).toInt() == truco?.roundsLimit) {
                finished = false
            }
            truco?.addScore(playerIndex, -1)
            decScore(playerIndex, truco?.getScore(playerIndex)?.toInt() as Int)
        } catch (_: Exception) {}
    }

    override fun onFinished(command: String) {
        truco = null
        finished = false
        if(command == "Ok") {
            finish()
        } else {
            TrucoPopUp(this).show(supportFragmentManager, "restart")
        }
    }
}