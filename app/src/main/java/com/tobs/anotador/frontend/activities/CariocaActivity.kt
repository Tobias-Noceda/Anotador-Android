package com.tobs.anotador.frontend.activities

import android.os.Bundle
import android.os.Handler
import android.widget.ImageButton
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tobs.anotador.R
import com.tobs.anotador.backend.Carioca
import com.tobs.anotador.backend.Player
import com.tobs.anotador.frontend.cells.CariocaCellAdapter
import com.tobs.anotador.frontend.cells.CellClickListener
import com.tobs.anotador.frontend.cells.CellGridModal
import com.tobs.anotador.frontend.popUps.CashPopUp
import com.tobs.anotador.frontend.popUps.FinishedPopUp
import com.tobs.anotador.frontend.popUps.OnCashPopUpClosedListener
import com.tobs.anotador.frontend.popUps.OnFinishedPopUpListener
import com.tobs.anotador.frontend.popUps.OnPopUpClosedListener
import com.tobs.anotador.frontend.popUps.OnScorePopUpListener
import com.tobs.anotador.frontend.popUps.PlayerCountPopUp
import com.tobs.anotador.frontend.popUps.PlayersPopUpClosedListener
import com.tobs.anotador.frontend.popUps.ScorePopUp
import com.tobs.anotador.frontend.popUps.TextPopUp

class CariocaActivity : GameActivity(), CellClickListener, OnScorePopUpListener,
    OnFinishedPopUpListener, PlayersPopUpClosedListener, OnPopUpClosedListener, OnCashPopUpClosedListener {

    private lateinit var cellsAdapter: CariocaCellAdapter
    private lateinit var cariocaRCV: RecyclerView
    private var finished: Boolean = false
    private var round: Boolean = false

    companion object {
        private var cellList: MutableList<CellGridModal> = ArrayList()
        var carioca: Carioca? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_carioca)

        cariocaRCV = findViewById(R.id.grid)
        val layoutManager = GridLayoutManager(this, 9, GridLayoutManager.HORIZONTAL, false)
        cariocaRCV.layoutManager = layoutManager

        if(carioca == null) {
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
        } else if(playerIndex != 0) {
            if(row == 0) {
                val cashPopUp = CashPopUp(carioca?.players?.get(playerIndex) as String, playerIndex)
                cashPopUp.setOnPopUpClosedListener(this)
                cashPopUp.show(supportFragmentManager, "cash")
            } else {
                if(round && row == carioca?.getPlayedRounds(playerIndex)) {
                    val scorePopUp =
                        ScorePopUp((carioca?.getScore(0, carioca?.getPlayedRounds(playerIndex) as Int) as String).dropLastWhile {
                                c -> c != ' '
                        } + "de " + carioca?.players?.get(playerIndex) as String,
                            playerIndex,
                            row)
                    scorePopUp.setOnPopUpClosedListener(this)
                    scorePopUp.show(supportFragmentManager, "score")
                } else if(carioca?.getPlayedRounds(playerIndex) as Int <= carioca?.roundsCount as Int/(carioca?.columns as Int - 1)) {
                    val scorePopUp = ScorePopUp(
                        (carioca?.getScore(0, carioca?.getPlayedRounds(playerIndex) as Int + 1) as String).dropLastWhile {
                            c -> c != ' '
                        } + "de " + carioca?.players?.get(playerIndex) as String,
                        playerIndex,
                        0)
                    scorePopUp.setOnPopUpClosedListener(this)
                    scorePopUp.show(supportFragmentManager, "score")
                }
            }
        }
    }

    override fun addCash(playerIndex: Int) {
        carioca?.addCash(playerIndex)
        val index = playerIndex * carioca?.rows as Int
        cellList[index] = cellList[index].copy(
            value = carioca?.players?.get(playerIndex) as String + " (${carioca?.getCashCount(playerIndex)})"
        )

        redraw()
    }

    override fun onAddedScore(score: Int, playerNumber: Int, row: Int) {
        var index = 0
        if(row != 0) {
            var aux: Int
            try {
                aux = carioca?.getScore(playerNumber)?.toInt() as Int
                aux -= if(row == 1) {
                    0
                } else {
                    carioca?.getScore(playerNumber,row - 1)?.toInt() as Int
                }
                carioca?.setScore(playerNumber, row, carioca?.getScore(playerNumber)?.toInt() as Int + score - aux)
                index = (playerNumber * carioca?.rows as Int) + row
            } catch(_: Exception) {}
        } else {
            round = carioca?.addScore(playerNumber, score) as Boolean
            if(round && carioca?.getPlayedRounds(playerNumber) == carioca?.roundsLimit) {
                finished = true
            }
            index = playerNumber * carioca?.rows as Int + carioca?.getPlayedRounds(playerNumber) as Int
        }
        cellList[index] = cellList[index].copy(value = carioca?.getScore(playerNumber) as String)

        redraw()
    }

    override fun onFinished(command: String) {
        carioca = null
        finished = false
        if(command == "Ok") {
            finish()
        } else {
            MainActivity.players = ArrayList()
            MainActivity.playerCount = 0
            val popUp = PlayerCountPopUp(CariocaActivity::class.java)
            popUp.setOnPopUpClosedListener(this)
            popUp.show(supportFragmentManager, null)
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
        carioca = Carioca(MainActivity.players, 2)
        var value: String
        for(i in 0 until carioca?.rows as Int * carioca?.columns as Int) {
            value = ""
            if(i in 1..< carioca?.rows as Int) {
                value = (carioca as Carioca).getScore(0, i)
            } else if (i != 0 && i % carioca?.rows as Int == 0) {
                value = (carioca as Carioca).getScore(i / (carioca as Carioca).rows, 0) +
                        " (${getCash(i / carioca?.rows as Int)})"
            }
            cellList.add(CellGridModal(i / carioca?.rows as Int, i % carioca?.rows as Int, value))
        }

        redraw()
    }

    private fun getWinner(): String {
        var toRet = ""
        var minScore = Int.MAX_VALUE
        for(i in 1 until carioca?.columns as Int) {
            try {
                val score: Int = carioca?.getScore(i)?.toInt() as Int
                if(score < minScore) {
                    minScore = score
                    toRet = carioca?.players?.get(i) as String
                }
            } catch (_: Exception) {}
        }

        return toRet
    }

    private fun redraw() {
        Handler().postDelayed({
            cellsAdapter = CariocaCellAdapter(cellList as ArrayList<CellGridModal>, this)
            cariocaRCV.adapter = cellsAdapter
        }, 22)
    }

    private fun getCash(playerIndex: Int): String {
        return carioca?.getCashCount(playerIndex).toString()
    }
}