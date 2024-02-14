package com.tobs.anotador.frontend.popUps

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.tobs.anotador.R
import java.lang.NumberFormatException

class ScorePopUp(private var descriptor: String, private var playerNumber: Int, private var row: Int) : DialogFragment(), DialogInterface.OnDismissListener {
    private var score: Int = 0
    private var listener: OnScorePopUpListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_text_pop_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isCancelable = false

        val editText = view.findViewById<EditText>(R.id.textBox)
        editText.hint = descriptor
        val buttonET = view.findViewById<Button>(R.id.confirm)

        buttonET.setOnClickListener {
            use(editText.text.toString())
        }
        editText.setOnKeyListener(View.OnKeyListener{_, keyCode, event ->
            if(event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                if(use(editText.text.toString())) {
                    return@OnKeyListener true
                }
            }
            false
        })
        editText.requestFocus()
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        listener?.onAddedScore(score, playerNumber, row)
    }

    fun setOnPopUpClosedListener(listener: OnScorePopUpListener) {
        this.listener = listener
    }

    private fun use(text: String): Boolean {
        if(text.isEmpty()) {
            Toast.makeText(context, "Entrada inválida", Toast.LENGTH_LONG).show()
        } else {
            try {
                score = text.toInt()
                dismiss()
                return true
            } catch (e: NumberFormatException) {
                Toast.makeText(context, "Entrada inválida", Toast.LENGTH_LONG).show()
            }
        }
        return false
    }
}