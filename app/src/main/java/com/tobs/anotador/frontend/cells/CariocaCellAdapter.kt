package com.tobs.anotador.frontend.cells

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.tobs.anotador.R

class CariocaCellAdapter(private val cellList: ArrayList<CellGridModal>,
        private val cellClickListener: CellClickListener
) : RecyclerView.Adapter<CariocaCellAdapter.CariocaCellHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CariocaCellHolder {
        // this method is use to inflate the layout file
        // which we have created for our recycler view.
        // on below line we are inflating our layout file.
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.carioca_cell,
            parent, false
        )

        // at last we are returning our view holder
        // class with our item View File.
        return CariocaCellHolder(itemView)
    }

    override fun onBindViewHolder(holder: CariocaCellHolder, position: Int) {
        // on below line we are setting data to our text view and our image view.
        holder.scoreView.text = cellList[position].value
        holder.cell.setOnClickListener {
            cellClickListener.onCellClick(
                cellList[position].playerIndex,
                cellList[position].row,
                cellList[position].value
            )
        }
    }

    override fun getItemCount(): Int {
        // on below line we are
        // returning our size of our list
        return cellList.size
    }

    class CariocaCellHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // on below line we are initializing our course name text view and our image view.
        val scoreView: TextView = itemView.findViewById(R.id.text)
        val cell: ConstraintLayout = itemView.findViewById(R.id.cell)
    }
}