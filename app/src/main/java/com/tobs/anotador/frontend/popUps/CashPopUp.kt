package com.tobs.anotador.frontend.popUps

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.tobs.anotador.R

class CashPopUp(private val playerName: String, private val playerIndex: Int) : DialogFragment() {

    private var add = false
    private var listener: OnCashPopUpClosedListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cash_pop_up, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val winnerText: TextView = view.findViewById(R.id.text)
        winnerText.text = "¿Desea agregar una caja a $playerName"
        val cancelBtn = view.findViewById<Button>(R.id.cancel)
        val siBtn = view.findViewById<Button>(R.id.yes)
        val noBtn = view.findViewById<Button>(R.id.no)

        cancelBtn.setOnClickListener {
            dismiss()
        }

        noBtn.setOnClickListener {
            dismiss()
        }

        siBtn.setOnClickListener {
            add = true
            dismiss()
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        if(add) {
            listener?.addCash(playerIndex)
        }
    }

    fun setOnPopUpClosedListener(listener: OnCashPopUpClosedListener) {
        this.listener = listener
    }
}