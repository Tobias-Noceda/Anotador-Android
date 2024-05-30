package com.tobs.anotador.frontend.activities

import android.os.Bundle
import android.os.Handler
import android.widget.ImageButton
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tobs.anotador.frontend.cells.CellClickListener
import com.tobs.anotador.frontend.cells.CellGridModal
import com.tobs.anotador.R
import com.tobs.anotador.backend.Generala
import com.tobs.anotador.backend.Player
import com.tobs.anotador.frontend.cells.GeneralaCellAdapter
import com.tobs.anotador.frontend.popUps.FinishedPopUp
import com.tobs.anotador.frontend.popUps.OnFinishedPopUpListener
import com.tobs.anotador.frontend.popUps.OnPopUpClosedListener
import com.tobs.anotador.frontend.popUps.OnScorePopUpListener
import com.tobs.anotador.frontend.popUps.PlayerCountPopUp
import com.tobs.anotador.frontend.popUps.PlayersPopUpClosedListener
import com.tobs.anotador.frontend.popUps.ScorePopUp
import com.tobs.anotador.frontend.popUps.TextPopUp

class GeneralaActivity : GameActivity(), CellClickListener, OnScorePopUpListener, OnFinishedPopUpListener, PlayersPopUpClosedListener, OnPopUpClosedListener {

    private lateinit var cellsAdapter: GeneralaCellAdapter
    private lateinit var generalaRCV: RecyclerView
    private var finished: Boolean = false

    companion object {
        private var cellList: MutableList<CellGridModal> = ArrayList()
        var generala: Generala? = null
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_generala)

        generalaRCV = findViewById(R.id.grid)
        val layoutManager = GridLayoutManager(this, 13, GridLayoutManager.HORIZONTAL, false)
        generalaRCV.layoutManager = layoutManager

        if(generala == null) {
            start()
        } else {
            redraw()
        }

        val backBtn = findViewById<ImageButton>(R.id.backButton)
        backBtn.setOnClickListener {
            if(finished) {
                onFinished("Ok")
            }
            finish()
        }
    }

    override fun onCellClick(playerIndex: Int, row: Int, value: String) {
        if(finished) {
            val finishedPopUp = FinishedPopUp(getWinner())
            finishedPopUp.setOnPopUpClosedListener(this)
            finishedPopUp.show(supportFragmentManager, "finished")
        } else if(playerIndex != 0 && row != 0 && row != generala?.rows as Int - 1) {
            val popUp = ScorePopUp("${generala?.getScore(0, row)} de ${generala?.players?.get(playerIndex - 1)}", playerIndex, row)
            popUp.setOnPopUpClosedListener(this)
            popUp.show(supportFragmentManager, "score")
        }
    }

    override fun onAddedScore(score: Int, playerNumber: Int, row: Int) {
        var addScore = score
        val scoreStr = if(score == 0) {
            "-"
        } else {
            score.toString()
        }
        var index: Int = (playerNumber * generala?.rows as Int) + row
        cellList[index] = cellList[index].copy(value = scoreStr)
        if(generala?.getScore(playerNumber, row) != "") {
            generala?.decRoundsCount()
            try {
                addScore -= generala?.getScore(playerNumber, row)?.toInt() as Int
            } catch (_: Exception) {}
        }
        generala?.setScore(playerNumber, row, score)
        finished = (generala as Generala).addScore(playerNumber, addScore)
        index = ((playerNumber + 1) * generala?.rows as Int) -1
        cellList[index] = cellList[index].copy(value = (generala as Generala).getScore(playerNumber))

        redraw()
    }

    override fun onFinished(command: String) {
        if(command != "Cancel") {
            generala = null
            finished = false
            if(command == "Ok") {
                finish()
            } else {
                MainActivity.players = ArrayList()
                MainActivity.playerCount = 0
                val popUp = PlayerCountPopUp(GeneralaActivity::class.java)
                popUp.setOnPopUpClosedListener(this)
                popUp.show(supportFragmentManager, null)
            }
        }
    }

    override fun getPlayerCount(playerCount: Int, calledClass: Class<out GameActivity>) {
        if(playerCount != 0) {
            MainActivity.playerCount = playerCount
            val showPopUp = TextPopUp(1, calledClass)
            showPopUp.setOnPopUpClosedListener(this)
            showPopUp.show(supportFragmentManager, "showPopUp")
        }
    }

    override fun onPopUpClosed(result: String, callingClass: Class<out GameActivity>?, playerNumber: Int) {
        if(result != "") {
            MainActivity.players = MainActivity.players + (Player(playerNumber, result))
            if(playerNumber == MainActivity.playerCount) {
                start()
            } else {
                val popUp = TextPopUp(playerNumber + 1, callingClass)
                popUp.setOnPopUpClosedListener(this)
                popUp.show(supportFragmentManager, "showPopUp")
            }
        }
    }

    private fun start() {
        cellList = ArrayList()
        generala = Generala(MainActivity.players, MainActivity.playerCount)
        var value: String
        for(i in 0 until generala?.rows as Int * generala?.columns as Int) {
            value = ""
            if(i in 1..< generala?.rows as Int) {
                value = (generala as Generala).getScore(0, i)
            } else if (i != 0 && i % 13 == 0) {
                value = (generala as Generala).getScore(i / (generala as Generala).rows, 0)
            }
            cellList.add(CellGridModal(i / generala?.rows as Int, i % generala?.rows as Int, value))
        }

        redraw()
    }

    private fun getWinner(): String {
        var toRet = ""
        var maxScore = 0
        for(i in 1 until generala?.rows as Int) {
            try {
                val score: Int = generala?.getScore(i)?.toInt() as Int
                if(score > maxScore) {
                    maxScore = score
                    toRet = generala?.players?.get(i) as String
                }
            } catch (_: Exception) {}
        }

        return toRet
    }

    private fun redraw() {
        Handler().postDelayed({
            cellsAdapter = GeneralaCellAdapter(cellList as ArrayList<CellGridModal>, this)
            generalaRCV.adapter = cellsAdapter
        }, 22)
    }
}
