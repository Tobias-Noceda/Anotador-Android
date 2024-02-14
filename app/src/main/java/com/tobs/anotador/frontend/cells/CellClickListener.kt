package com.tobs.anotador.frontend.cells

interface CellClickListener {
    fun onCellClick(playerIndex: Int, row: Int, value: String)
}