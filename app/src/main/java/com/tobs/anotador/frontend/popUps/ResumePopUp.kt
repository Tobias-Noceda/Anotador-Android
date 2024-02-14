package com.tobs.anotador.frontend.popUps

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment
import com.tobs.anotador.R
import com.tobs.anotador.frontend.activities.GameActivity

class ResumePopUp(private val clase: Class<out GameActivity>?) : DialogFragment() {

    private var used = false
    private var command: String = "Cancel"
    private var listener: OnPopUpClosedListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_resume_pop_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val siBtn = view.findViewById<Button>(R.id.si)
        val noBtn = view.findViewById<Button>(R.id.no)

        siBtn.setOnClickListener {
            used = true
            command = "Si"
            dismiss()
        }

        noBtn.setOnClickListener {
            used = true
            command = "No"
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
            listener?.onPopUpClosed(command, clase, 0)
        }
    }

    fun setOnPopUpClosedListener(listener: OnPopUpClosedListener) {
        this.listener = listener
    }
}