package com.tobs.anotador.frontend.activities

import android.os.Bundle
import android.widget.GridView
import android.widget.ImageButton
import com.tobs.anotador.frontend.cells.CellClickListener
import com.tobs.anotador.frontend.cells.CellGridAdapter
import com.tobs.anotador.frontend.cells.CellGridModal
import com.tobs.anotador.R
import com.tobs.anotador.backend.Canasta
import com.tobs.anotador.backend.Player
import com.tobs.anotador.frontend.popUps.FinishedPopUp
import com.tobs.anotador.frontend.popUps.OnFinishedPopUpListener
import com.tobs.anotador.frontend.popUps.OnPopUpClosedListener
import com.tobs.anotador.frontend.popUps.OnScorePopUpListener
import com.tobs.anotador.frontend.popUps.ScorePopUp
import com.tobs.anotador.frontend.popUps.TextPopUp

class CanastaActivity : GameActivity(), CellClickListener, OnScorePopUpListener, OnFinishedPopUpListener,
    OnPopUpClosedListener {

    private lateinit var canastaGRV: GridView
    private lateinit var courseAdapter: CellGridAdapter
    private var pops: Int = 0
    private var addScore: Int = 0
    private var won: Boolean = false
    private var finished: Boolean = false

    companion object {
        private var cellList: MutableList<CellGridModal> = ArrayList()
        var canasta: Canasta? = null
        private var rows: Int = 0
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_canasta)

        if(canasta == null) {
            start()
        }

        // initializing variables of grid view with their ids.
        canastaGRV = findViewById(R.id.grid)

        courseAdapter = CellGridAdapter(cellList = cellList, this@CanastaActivity, this)

        // on below line we are setting adapter to our grid view.
        canastaGRV.adapter = courseAdapter

        // Button
        val backBtn = findViewById<ImageButton>(R.id.backButton)
        backBtn.setOnClickListener {
            if(finished) {
                onFinished("Ok")
            }
            finish()
        }
    }

    override fun onCellClick(playerIndex: Int, row: Int, value: String) {
        val aux:Canasta = canasta as Canasta
        if(aux.getPlayedRounds(playerIndex) <= aux.roundsCount/aux.columns && !finished) {
            pops = 1
            addScore = 0
            val popUp = ScorePopUp("Ingrese la base", playerIndex, 0)
            popUp.setOnPopUpClosedListener(this)
            popUp.show(supportFragmentManager, "showPopUp")
        } else if(finished) {
            val finishedPopUp = FinishedPopUp(getWinner())
            finishedPopUp.setOnPopUpClosedListener(this)
            finishedPopUp.show(supportFragmentManager, null)
        }
    }

    override fun onAddedScore(score: Int, playerNumber: Int, row: Int) {
        canasta?.setScore(playerNumber, score)
        val index = ((canasta as Canasta).getPlayedRounds(playerNumber) * 2) + playerNumber
        cellList[index] = cellList[index].copy(value = score.toString())
        courseAdapter.notifyDataSetChanged()
        addScore += score
        if(pops == 1) {
            pops++
            val popUp = ScorePopUp("Ingrese el puntaje", playerNumber, 0)
            popUp.setOnPopUpClosedListener(this)
            popUp.show(supportFragmentManager, "showPopUp")
        } else {
            if((canasta as Canasta).addScore(playerNumber, addScore) || won) {
                if((canasta as Canasta).getPlayedRounds(0) != (canasta as Canasta).getPlayedRounds(1)) {
                    won = true
                } else {
                    finished = true
                }
            }
            cellList[index + 2] = cellList[index + 2].copy(value = canasta?.getScore(playerNumber, (canasta as Canasta).getPlayedRounds(playerNumber) + 1).toString())
            cellList[playerNumber] = cellList[playerNumber].copy(value = "${canasta?.players?.get(playerNumber)} (${canasta?.getFloor(playerNumber)})")
            if(!finished && (canasta as Canasta).getPlayedRounds(playerNumber) == rows && (canasta as Canasta).getPlayedRounds(if(playerNumber == 0) 1 else 0) == rows) {
                val prevCells = cellList.size
                while(cellList.size < prevCells + 6) {
                    cellList.add(CellGridModal(cellList.size % 2, cellList.size / 2, ""))
                }
                rows += 3
            }
            courseAdapter.notifyDataSetChanged()
        }
    }

    private fun getWinner(): String {
        var winner = ""
        var maxScore = 0
        for(i in 0 until canasta?.columns as Int) {
            try {
                val aux: Int = canasta?.getScore(i, (canasta as Canasta).getPlayedRounds(i) + 1)?.toInt() as Int
                if (aux > maxScore) {
                    winner = (canasta as Canasta).players[i]
                    maxScore = aux
                }
            } catch (_: Exception) {}
        }

        return winner
    }

    override fun onFinished(command: String) {
        canasta = null
        won = false
        finished = false
        if(command == "Ok") {
            finish()
        } else {
            MainActivity.players = ArrayList()
            val popUp = TextPopUp(1, CanastaActivity::class.java)
            popUp.setOnPopUpClosedListener(this)
            popUp.show(supportFragmentManager, null)
        }
    }

    override fun onPopUpClosed(result: String, callingClass: Class<out GameActivity>?, playerNumber: Int) {
        MainActivity.players = MainActivity.players + (Player(playerNumber, result))
        if(playerNumber == 2) {
            start()
            courseAdapter = CellGridAdapter(cellList = cellList, this@CanastaActivity, this)
            canastaGRV.adapter = courseAdapter
            courseAdapter.notifyDataSetChanged()
        } else {
            val popUp = TextPopUp(playerNumber + 1, callingClass)
            popUp.setOnPopUpClosedListener(this)
            popUp.show(supportFragmentManager, "showPopUp")
        }
    }

    private fun start() {
        canasta = Canasta(MainActivity.players, 1500, 3000, 5000)

        cellList = ArrayList()

        for (i in 0 until 20) {
            if(i < 2) {
                cellList.add(
                    CellGridModal(i, 0,
                        canasta?.players?.get(i).toString() + "(" + canasta!!.getFloor(i) + ")")
                )
            } else {
                cellList.add(CellGridModal(i % 2, i / 2, ""))
            }
        }
        rows = 9
    }
}