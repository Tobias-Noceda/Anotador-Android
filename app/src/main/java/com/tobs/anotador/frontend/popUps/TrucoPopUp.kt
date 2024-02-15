package com.tobs.anotador.frontend.popUps

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.tobs.anotador.R

class TrucoPopUp(
    private val caller: AppCompatActivity
) : DialogFragment() {

    private var used = false
    private var limit = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_truco_pop_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val quinceBtn = view.findViewById<Button>(R.id.quince)
        val treintaBtn = view.findViewById<Button>(R.id.treinta)

        quinceBtn.setOnClickListener {
            used = true
            limit = 15
            dismiss()
        }

        treintaBtn.setOnClickListener {
            used = true
            limit = 30
            dismiss()
        }

        val cancelBtn = view.findViewById<Button>(R.id.cancel)
        cancelBtn.setOnClickListener {
            dismiss()
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        if(used) {
            OnTrucoStarter.start(limit, caller)
        }
    }
}