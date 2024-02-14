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

class FinishedPopUp(private val winner: String) : DialogFragment() {

    private var used = false
    private var command: String = "Cancel"
    private var listener: OnFinishedPopUpListener? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_finished_pop_up, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val winnerText: TextView = view.findViewById(R.id.winner)
        winnerText.text = "El juego ha terminado, ganó $winner"
        val cancelBtn = view.findViewById<Button>(R.id.cancel)
        val okBtn = view.findViewById<Button>(R.id.accept)
        val restartBtn = view.findViewById<Button>(R.id.restart)

        cancelBtn.setOnClickListener {
            dismiss()
        }

        okBtn.setOnClickListener {
            used = true
            command = "Ok"
            dismiss()
        }

        restartBtn.setOnClickListener {
            used = true
            command = "Restart"
            dismiss()
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        if(used) {
            listener?.onFinished(command)
        }
    }

    fun setOnPopUpClosedListener(listener: OnFinishedPopUpListener) {
        this.listener = listener
    }
}