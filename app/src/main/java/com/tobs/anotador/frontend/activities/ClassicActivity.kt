package com.tobs.anotador.frontend.activities

import android.os.Bundle
import android.os.Handler
import android.widget.ImageButton
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tobs.anotador.R
import com.tobs.anotador.backend.ClassicScorekeeper
import com.tobs.anotador.backend.Player
import com.tobs.anotador.frontend.cells.CariocaCellAdapter
import com.tobs.anotador.frontend.cells.CellClickListener
import com.tobs.anotador.frontend.cells.CellGridModal
import com.tobs.anotador.frontend.popUps.OnPopUpClosedListener
import com.tobs.anotador.frontend.popUps.OnScorePopUpListener
import com.tobs.anotador.frontend.popUps.PlayersPopUpClosedListener
import com.tobs.anotador.frontend.popUps.ScorePopUp
import com.tobs.anotador.frontend.popUps.TextPopUp

class ClassicActivity : GameActivity(), CellClickListener, OnScorePopUpListener,
    PlayersPopUpClosedListener, OnPopUpClosedListener {

    private lateinit var cellsAdapter: CariocaCellAdapter
    private lateinit var classicRCV: RecyclerView
    private var round: Boolean = false

    companion object {
        private var cellList: MutableList<CellGridModal> = ArrayList()
        var game: ClassicScorekeeper? = null
        var rows = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_classic)

        classicRCV = findViewById(R.id.grid)

        if(game == null) {
            start()
        } else {
            redraw()
        }

        val layoutManager = GridLayoutManager(this, game?.columns as Int, GridLayoutManager.VERTICAL, false)
        classicRCV.layoutManager = layoutManager

        val backBtn = findViewById<ImageButton>(R.id.backButton)
        backBtn.setOnClickListener {
            finish()
        }
    }

    override fun onCellClick(playerIndex: Int, row: Int, value: String) {
        if(row != 0) {
            if(round && row == game?.getPlayedRounds(playerIndex)) {
                val scorePopUp =
                    ScorePopUp("Reemplazar de " + game?.players?.get(playerIndex) as String, playerIndex, row)
                scorePopUp.setOnPopUpClosedListener(this)
                scorePopUp.show(supportFragmentManager, "score")
            } else if(game?.getPlayedRounds(playerIndex) as Int <= game?.roundsCount as Int/game?.columns as Int) {
                val scorePopUp = ScorePopUp(game?.players?.get(playerIndex) as String, playerIndex, 0)
                scorePopUp.setOnPopUpClosedListener(this)
                scorePopUp.show(supportFragmentManager, "score")
            }
        }
    }

    override fun onAddedScore(score: Int, playerNumber: Int, row: Int) {
        if(row != 0) {
            var aux: Int
            try {
                aux = game?.getScore(playerNumber)?.toInt() as Int
                aux -= if(row == 1) {
                    0
                } else {
                    game?.getScore(playerNumber,row - 1)?.toInt() as Int
                }
                game?.setScore(playerNumber, row, game?.getScore(playerNumber)?.toInt() as Int + score - aux)
            } catch(_: Exception) {}
        } else {
            round = game?.addScore(playerNumber, score) as Boolean
        }
        val index: Int = game?.getPlayedRounds(playerNumber) as Int * game?.players?.size as Int + playerNumber
        cellList[index] = cellList[index].copy(value = game?.getScore(playerNumber) as String)

        if(round && game?.getPlayedRounds(playerNumber) == rows) {
            val prevCells = cellList.size
            val playerCount = (game as ClassicScorekeeper).players.size
            while(cellList.size < prevCells + playerCount) {
                cellList.add(CellGridModal(cellList.size % playerCount, cellList.size / playerCount, ""))
            }
            rows++
        }
        redraw()
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
        game = ClassicScorekeeper(MainActivity.players)
        var value: String
        for(i in 0 until 11 * game?.columns as Int) {
            value = ""
            if(i in 0 ..< game?.columns as Int) {
                value = (game as ClassicScorekeeper).getScore(i, 0)
            }
            cellList.add(CellGridModal(i % game?.columns as Int, i / game?.columns as Int, value))
        }

        rows = 10
        redraw()
    }

    private fun redraw() {
        Handler().postDelayed({
            cellsAdapter = CariocaCellAdapter(cellList as ArrayList<CellGridModal>, this)
            classicRCV.adapter = cellsAdapter
        }, 22)
    }
}