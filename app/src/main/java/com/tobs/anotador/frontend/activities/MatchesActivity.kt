package com.tobs.anotador.frontend.activities

import android.widget.ImageView
import com.tobs.anotador.R

abstract class MatchesActivity: GameActivity() {

    val images: List<Int> = listOf(
        R.drawable.fosforos0,
        R.drawable.fosforos1,
        R.drawable.fosforos2,
        R.drawable.fosforos3,
        R.drawable.fosforos4,
        R.drawable.fosforos5
    )

    lateinit var playersImages: MutableList<ArrayList<ImageView>>

    fun addScore(playerIndex: Int, score: Int) {
        val index = score / 5 - if(score % 5 == 0) { 1 } else { 0 }
        val image = score % 5 + if(score % 5 == 0) { 5 } else { 0 }
        setImage(playerIndex, index, image)
    }

    fun decScore(playerIndex: Int, score: Int) {
        val index = score / 5
        val image = score % 5
        setImage(playerIndex, index, image)
    }

    private fun setImage(playerIndex: Int, index: Int, imageNumber: Int) {
        playersImages[playerIndex][index].setImageResource(images[imageNumber])
    }

}