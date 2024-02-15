package com.tobs.anotador.frontend.popUps

import android.content.DialogInterface
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.tobs.anotador.R
import com.tobs.anotador.frontend.activities.GameActivity
import java.lang.NumberFormatException

class PlayerCountPopUp(private val callingClass: Class<out GameActivity>) : DialogFragment(), DialogInterface.OnDismissListener {

    private var players: Int = 0
    private var used = false
    private var listener: PlayersPopUpClosedListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_player_count_pop_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val cancelBtn = view.findViewById<Button>(R.id.cancel)
        val editText = view.findViewById<EditText>(R.id.textBox)
        val buttonET = view.findViewById<Button>(R.id.confirm)

        cancelBtn.setOnClickListener {
            dismiss()
        }
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
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        if(used) {
            listener?.getPlayerCount(players, callingClass)
        }
    }

    fun setOnPopUpClosedListener(listener: PlayersPopUpClosedListener) {
        this.listener = listener
    }

    private fun use(text: String): Boolean {
        if(text.isEmpty() || text == "0") {
            Toast.makeText(context, "Entrada inválida", Toast.LENGTH_LONG).show()
        } else {
            try {
                players = text.toInt()
                used = true
                dismiss()
                return true
            } catch (e: NumberFormatException) {
                Toast.makeText(context, "Entrada inválida", Toast.LENGTH_LONG).show()
            }
        }
        return false
    }
}