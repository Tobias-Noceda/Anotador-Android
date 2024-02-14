package com.tobs.anotador.frontend.cells

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.tobs.anotador.R
import com.tobs.anotador.frontend.activities.GeneralaActivity

internal class CellGridAdapter(
    private val cellList:List<CellGridModal>,
    private val context:Context,
    private val cellClickListener: CellClickListener
):
    BaseAdapter() {
        // in base adapter class we are creating variables
        // for layout inflater, course image view and course text view.
        private var layoutInflater: LayoutInflater? = null
        private lateinit var cellText:TextView

        // below method is use to return the count of course list
        override fun getCount(): Int {
            return cellList.size
        }

        // below function is use to return the item of grid view.
        override fun getItem(position: Int): Any? {
            return null
        }

        // below function is use to return item id of grid view.
        override fun getItemId(position: Int): Long {
            return 0
        }

        // in below function we are getting individual item of grid view.
        @SuppressLint("InflateParams")
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            var convView = convertView
            // on blow line we are checking if layout inflater
            // is null, if it is null we are initializing it.
            if (layoutInflater == null) {
                layoutInflater =
                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            }
            // on the below line we are checking if convert view is null.
            // If it is null we are initializing it.
            if (convView == null) {
                // on below line we are passing the layout file
                // which we have to inflate for each item of grid view.
                val layout: Int = if(context::class.java == GeneralaActivity::class.java) {
                    R.layout.generala_cell
                } else {
                    R.layout.classic_cell
                }
                convView = layoutInflater!!.inflate(layout, null)
            }

            convView?.setOnClickListener {
                cellClickListener.onCellClick(
                    cellList[position].playerIndex,
                    cellList[position].row,
                    cellList[position].value
                )
            }
            // on below line we are initializing our course image view
            // and course text view with their ids.
            cellText = convView!!.findViewById(R.id.cell)
            // on below line we are setting text in our course text view.
            cellText.text = cellList[position].value
            // at last we are returning our convert view.
            return convView
        }
    }