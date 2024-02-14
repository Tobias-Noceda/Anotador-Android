package com.tobs.anotador.frontend.cells

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tobs.anotador.R
import com.tobs.anotador.frontend.activities.CariocaActivity
import com.tobs.anotador.frontend.activities.GameActivity

// on below line we are creating
// a course rv adapter class.
class GeneralaCellAdapter(
    // on below line we are passing variables
    // as course list and context
    private val cellList: ArrayList<CellGridModal>,
    private val cellClickListener: CellClickListener
) : RecyclerView.Adapter<GeneralaCellAdapter.GeneralaCellHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GeneralaCellHolder {
        // this method is use to inflate the layout file
        // which we have created for our recycler view.
        // on below line we are inflating our layout file.
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.generala_cell,
            parent, false
        )

        // at last we are returning our view holder
        // class with our item View File.
        return GeneralaCellHolder(itemView)
    }

    override fun onBindViewHolder(holder: GeneralaCellHolder, position: Int) {
        // on below line we are setting data to our text view and our image view.
        holder.scoreView.text = cellList[position].value
        holder.scoreView.setOnClickListener {
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

    class GeneralaCellHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // on below line we are initializing our course name text view and our image view.
        val scoreView: TextView = itemView.findViewById(R.id.cell)
    }
}