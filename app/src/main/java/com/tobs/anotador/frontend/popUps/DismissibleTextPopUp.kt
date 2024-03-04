package com.tobs.anotador.frontend.popUps

import android.content.DialogInterface
import android.os.Bundle
import android.text.InputType
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

class DismissibleTextPopUp(private var playerNumber: Int, private var callingClass: Class<out GameActivity>) : DialogFragment(), DialogInterface.OnDismissListener {

    private var result:String = ""
    private var used = false
    private var listener: OnPopUpClosedListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment.
        return inflater.inflate(R.layout.fragment_dismisable_text_pop_up, container, true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val cancelBtn = view.findViewById<Button>(R.id.cancel)
        val editText = view.findViewById<EditText>(R.id.textBox)
        editText.hint = "Jugador $playerNumber"
        val buttonET = view.findViewById<Button>(R.id.confirm)

        cancelBtn.setOnClickListener {
            result = "Cancel"
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

        // Configura el EditText para que la primera letra esté en mayúscula.
        editText.inputType = InputType.TYPE_TEXT_FLAG_CAP_SENTENCES
        editText.requestFocus()
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        if(used) {
            listener?.onPopUpClosed(result, callingClass, playerNumber)
        }
    }

    fun setOnPopUpClosedListener(listener: OnPopUpClosedListener) {
        this.listener = listener
    }

    private fun use(text: String): Boolean {
        if(text.trim().isEmpty()) {
            Toast.makeText(context, "Entrada inválida", Toast.LENGTH_LONG).show()
        } else {
            result = text.trim()
            used = true
            dismiss()
            return true
        }
        return false
    }
}